var List = function(tableId) {
	"use strict";

	//private fields
	var main = null,
	table = null,
	header = null,
	foot = null,
	sort = {
		panel: null,
		target: null
	},
	option = {
		pagable: false,
		wrap: false,
		selectable: true
	},
	info = {
		shape: {
			body: null
		},
		sort: {
			items: []
		},
		load: {
			data: null,
			loadAfterFocus: false,
			url: location.pathname + "/list",
			search: null,
			editor: null,
			etc: null
		},
		merge: {
			primary: [],
			columns: []
		},
		page: {
			index: 1,
			totalPage: 0,
			rowCount: 0,
			rowPerPage: 10,
			pageCountPerList: 5,
			pages: null,
			prevPage: null,
			nextPage: null
		},
		reduce: {},
		fn: {},
		wrap: 0,
		cellsize: 0,
		hiddens: []
	},
	events = {
		onload: null,
		ondataload: null,
		onselect: null,
		ondblclick: null,
		onrow: null,
		oncell: null
	},
	message = {
		empty: "조회된 데이터가 없습니다."//"Posting does not exist."
	},

	//private methods
	rendering = function() {
		if(table && table.length > 0) {
			erasing();
			adjustInit();
			putting();
			merging();
			wrapping();
			hiddening();
			paginating();
			adjustWidth();

			if(events.onload) {
				events.onload(info.load.data, main);
			}
			
			if(events.oncell) {
				for(var i in events.oncell) {
					$("td[data-name='" + i + "']").click(function(col){
						return function(e){
							if(typeof events.oncell[col] === "function") {
								var returnData = null;
								if(info.load.data) {
									returnData = info.load.data[this.parentNode.parentNode.getAttribute("data-index")];
								}

								if(!events.oncell[col](returnData)) {
									e.stopPropagation();
								};
							}
						};
					}(i));
				}
			}
		}
		else {
			setTimeout(function(){
				rendering();
			});
		}
	},
	hiddening = function() {
		if(info.hiddens.length > 0) {
			for(var i = 0; i < info.hiddens.length; i++) {
				main.find("th[data-name=" + info.hiddens[i] + "]").hide();
				table.find("td[data-name=" + info.hiddens[i] + "]").hide();
			}
		}
	},
	adjustInit = function() {
		table.add(header).css({
			tableLayout: "auto",
			width: "100%"
		})
		.find("col").removeAttr("width");
	},
	adjustWidth = function() {
		var trs = table.find("thead > tr"),
		setMx = function(v, row, col) {
			if(mx[row][col] === undefined) mx[row][col] = v;
			else setMx(v, row, ++col);
		};

		var mx = [];
		$.each(trs, function(i,tr) { mx[i] = []; });
		$.each(trs, function(trIndex,tr) {
			var index = 0;
			$.each(tr.cells, function(i,th) {
				var $th = $(th),
				colspan = parseInt($th.attr("colSpan")||1),
				rowspan = parseInt($th.attr("rowSpan")||1),
				w = colspan === 1 ? $th.width() : 0;

				if(colspan > 1 && rowspan > 1) {
					for(var j = index; j < index + colspan; j++) {
						for(var k = trIndex; k < trIndex + rowspan; k++)
							setMx(w, k, j);
					}
				}
				else if(colspan > 1) {
					for(var j = index; j < index + colspan; j++)
						setMx(w, trIndex, j);
				}
				else if(rowspan > 1) {
					for(var k = trIndex; k < trIndex + rowspan; k++)
						setMx(w, k, index);
				}
				else {
					setMx(w, trIndex, index);
				}

				index += colspan;
			});
		});

		var cols = [];
		for(var i = 0; i < mx.length; i++) {
			for(var j = 0; j < mx[i].length; j++) {
				if((cols[j]||0) < mx[i][j]) cols[j] = mx[i][j];
			}
		}

		setTimeout(function(cols){
			return function() {
				var tw = table.width(),
				addition = table.attr("id") === "dlbr" ? 10 : 0;
				
//				if(navigator.userAgent.indexOf("Trident") === -1) {
//					addition = 0;
//				} 
				
				table.find("col").each(function(i, col){
					col.setAttribute("width", cols[i] + addition);
				});

				header.find("col").each(function(i, col){
					col.setAttribute("width", cols[i] + addition);
				});

				table.add(header).css({
					tableLayout: "fixed",
					width: tw
				});
			};
		}(cols));
	},
	unselecting = function() {
		$("tbody.selected",table).removeClass("selected");

		if(info.load.editor) {
			$("input,select,textarea",info.load.editor).filter("[name]").each(function(i, el){
				if(/radio|checkbox/i.test(el.type)) el.checked = false;
				else if(/select/i.test(el.nodeName)) el.selectedIndex = 0;
				else el.value = "";
			});
			$("[data-name]",info.load.editor).each(function(i, el){
				var value = $(el).text();
				el.innerHTML = value.replace(/./g, "&nbsp;");
			});
		}
	},
	erasing = function() {
		main.scrollTop(0);
		unselecting();

		header.find("input[type=checkbox]").checked(false);
		foot.children().remove();
		$("tbody.cloner",table).remove();
	},
	formatting = function(value, css, format) {
		if(css) {
			$.each(css.toLowerCase().split(/\s/), function(i, c){
				if(value) {
					if(c === "date") {
						value = new Date(value).format(format||"yyyy-MM-dd");
					}
				}
			});
		}

		return value;
	},
	putting = function() {
		if(!info.load.data || info.load.data.length < 1) {
			var tbody = "<tbody class='cloner'><tr><td colspan='" + info.cellsize + "'>" + message.empty + "</td></tr></tbody>";
			table.append(tbody);
		}
		else {
			$.each(info.load.data, function(i, row){
				var tbody = info.shape.body.clone(true).attr("data-index", i);
				
				if(option.selectable) {
					tbody.click(function(i, row){
						return function(e){
							$("tbody.selected",table).removeClass("selected");
							$(this).addClass("selected");
	
							if(info.load.editor) {
								$("input,select,textarea",info.load.editor).filter("[name]").each(function(i, el) {
									if(row.hasOwnProperty(el.name)) {
										var value = row[el.name];
	
										if(value !== null) {
											if(el.type === "checkbox" || el.type === "radio") el.checked = (el.value == value);
											else el.value = formatting(value, el.className, el.getAttribute("data-format"));
										}
									}
								});
								$("[data-name]",info.load.editor).each(function(i, el){
									var name = el.getAttribute("data-name");
									if(row.hasOwnProperty(name)) {
										var value = row[name];
										
										if(value) {
											el.innerHTML = formatting(value, el.className, el.getAttribute("data-format"));
										}
										else {
											el.innerHTML = "";
										}
									}
								});
							}
	
							if(events.onselect) events.onselect(row, i, this);
						};
					}(i, row)).dblclick(function(i, row) {
						return function(e) {
							if(events.ondblclick) events.ondblclick(row, i, this);
						};
					}(i, row));
				}

				$("td[data-define=ROWNUM]",tbody).html(i + 1 + ((info.page.index - 1) * info.page.rowPerPage));
				$("td[data-define=CHECKBOX]",tbody).each(function(index, td){
					$(td).append("<input type='checkbox' class='cei-row-checkbox' data-index='" + i + "'>").click(function(e){
						e.stopPropagation();
					});
				});
				$("[data-name]",tbody).each(function(index, el){
					var name = el.getAttribute("data-name"),
					value = info.load.data[i][name];

					if(/INPUT|SELECT/.test(el.nodeName)) el.value = value;
					else {
						if(info.fn[name]) {
							info.fn[name](el, value, info.load.data[i]);
						}
						else if(value != null) {
							value = formatting(value, el.className, el.getAttribute("data-format")); 

							if(info.reduce[name] < value.length)
								value = value.substring(0, info.reduce[name]) + "..";

							el.innerHTML = value;
						}
					}
				});

				table.append(tbody);

				if(events.onrow) events.onrow(info.load.data[i], i, tbody);
			});
		}
	},
	merging = function() {
		if(info.merge && info.merge.primary && info.merge.columns && info.merge.columns.length > 0) {
			if(info.merge.primary[0] && info.merge.columns[0]) {
				var savedValue = null,
				savedTbody = null;
	
				table.find("tbody").each(function(i, tbody){
					var primaryValue = "";
					for(i = 0; i < info.merge.primary.length; i++)
						primaryValue += $(tbody).find("td[data-name=" + info.merge.primary[i] + "]").text();
	
					if(savedValue !== primaryValue) {
						savedValue = primaryValue;
						savedTbody = tbody;
					}
					else if(savedTbody) {
						savedTbody.setAttribute("merge", "true");
	
						$(savedTbody).append($(tbody).find("tr"));
						$(tbody).remove();
					}
				});
	
				table.find("tbody[merge=true]").each(function(x, tbody){
					var column = null;

					for(var i = 0; i < info.merge.columns.length; i++) {
						column = {};

						$(tbody).find("tr > td[data-name=" + info.merge.columns[i] + "]").each(function(y, td){
							var value = $(td).text();

							if(column.value !== value) {
								column.value = value;
								column.savedTd = $(td);
							}
							else {
								column.savedTd.attr("rowspan", parseInt(column.savedTd.attr("rowspan") || 1) + parseInt(td.getAttribute("rowSpan") || 1));
								$(td).remove();
							}
						});
					}
				});
			}
		}
	},
	paginating = function() {
		if(option.pagable) {
			var html = "<div class='cei-list-page'><ol>";

			if(info.page.prevPage) {
				html += "<li class='home' data-page='" + 1 + "'>&lt;&lt;</li>"
					+ "<li class='prev' data-page='" + (info.page.prevPage) + "'>&lt</li>";
			}

			if(info.page.pages) {
				for(var i = info.page.pages[0]; i < info.page.pages[0] + info.page.pageCountPerList && i <= info.page.totalPage; i++) {
					html += (i != info.page.index) ? "<li data-page='" + i + "'>" + i + "</li>"
							: "<li class='selected'>" + i + "</li>";
				}
			}

			if(info.page.nextPage) {
				html += "<li class='next' data-page='" + info.page.nextPage + "'>&gt;</li>"
					+ "<li class='end' data-page='" + info.page.totalPage + "'>&gt;&gt;</li>";
			}
			
			console.log(html);

			foot.html(html + "</ol><div style='clear:both'/></div>")
			.find("li[data-page]").click(function(e) {
				loading(this.getAttribute("data-page"));
			});
		}
	},
	wrapping = function() {
		if(option.wrap) {
			if(info.load.data && info.load.data.length < info.wrap) {
				for(var i = info.load.data.length; i < info.wrap; i++)
					table.append(info.shape.body.clone(true));
			}
		}
	},
	loading = function(pageIndex, rowIndex) {
		var param = info.load.search.serialize();
		if(option.pagable) param += "&pageCountPerList=" + (info.page.pageCountPerList||5)  + "&rowPerPage=" + (info.page.rowPerPage||15) + "&page=" + (pageIndex || 1);

		if(info.load.addParams) {
			for(var i in info.load.addParams) {
				if(i === "orderBy" && info.sort.orderBy) {
					param += "&orderBy=" + info.sort.orderBy;
				}
				else if(i === "orderMethod" && info.sort.orderMethod) {
					param += "&orderMethod=" + info.sort.orderMethod;
				}
				else {
					param += "&" + i + "=" + encodeURIComponent(info.load.addParams[i]);
				}
			}
		}

		if($(".list-loading").size() === 0) {
			$(document.body).append("<div class='list-loading'></div>");
		} 
		
		$.ajax({
			type: "post",
			url: info.load.url,
			data: param,
			failure: function(r) {
				$(".waiting").hide()
			},
			success: function(r) {
				try {
					if(r == null) {
						info.load.data = [];
					}
					else if(!r.hasOwnProperty("list")) {
						info.load.data = r;
					}
					else {
						info.load.data = r.list || [];
		
						if(r.param) {
							info.load.etc = r.param ? r.param : null;
							info.page.index = r.param.page||1;
							info.page.rowCount = r.param.rowCount;
							info.page.pageCountPerList = r.param.pageCountPerList;
							info.page.totalPage = r.param.totalPage;
							info.page.pages = r.param.pages;
							info.page.prevPage = r.param.prevPage;
							info.page.nextPage = r.param.nextPage;
						}
					}
				}
				catch(e){  console.error(e.message + "\nerror occurred!"); }

				if(events.ondataload) events.ondataload(info.load); //be ondataload(loaded)

				$(".list-loading").remove();
				rendering();

				if(info.load.loadAfterFocus) {
					info.load.loadAfterFocus = false;							

					if(rowIndex > -1 && info.load.data && info.load.data.length > 0) {
						if(rowIndex > info.load.data.length - 1) rowIndex = info.load.data.length - 1;
						table.find("tbody.cloner")[rowIndex].click();
					}
				}

				$(".waiting").hide()
			}
		});
	},
	registrySearch = function(selector, enterKeyFn) {
		if(typeof selector !== "function") info.load.search = $(selector);
		else enterKeyFn = selector;

		if(info.load.search.has("[type=submit],[type=image],button[type=submit]").length == 0)
			info.load.search.append("<input type='submit' style='position:absolute;top:-999px;left:-999px;opacity:0;filter:alpha(opacity=0)'>");

		if(enterKeyFn === undefined) info.load.search.submit(function() { loading();return false; });
		else {
			info.load.search.submit(function() {
				if(fn(info.load.search) !== false) loading();
				return false;
			});
		}
	},
	registryEditor = function(selector, enterKeyFn) {
		if(typeof selector !== "function") info.load.editor = $(selector);
		else enterKeyFn = selector;

		if(info.load.editor.has("[type=submit],[type=image],button[type=submit]").length == 0)
			info.load.editor.append("<input type='submit' style='position:absolute;top:-999px;left:-999px;opacity:0;filter:alpha('opacity=0')'>");

		if(enterKeyFn === undefined) info.load.editor.submit(function(){ return false; });
		else {
			info.load.editor.submit(function() {
				enterKeyFn();
				return false;
			});
		}
	},

	//public methods

	publicMethod = {
		load: function(pageIndex, rowIndex){
			if(!table) {
				$(document).ready(function(){
					loading(pageIndex, rowIndex);
				});
			}
			else {
				loading(pageIndex, rowIndex);
			}

			return this;
		},
		update: function(rowIndex) {
			info.load.loadAfterFocus = true;
			loading(info.page.index, rowIndex !== undefined ? rowIndex : (parseInt(table.find("tbody.selected").attr("data-index") || -1)));
			
			if(rowIndex > -1 && info.load.data[rowIndex]) {
				try {
					if(events.onselect) events.onselect(info.load.data[rowIndex], rowIndex, table.find("tbody").eq(rowIndex));
				}
				catch(e) {}
			}
			
			return this;
		},
		spreadsheet: function(_title, url, addCondition) {
			var title = _title || $(".cei-info > h2").text() || "엑셀다운로드",
			headerHtml = header.html(),
			bodyHtml = "<tbody>" + info.shape.body.html() + "</tbody>",
			groomHtml = function(str) {
				return str.replace(/<(th|td) [\w\d\s\"\'\:\;\=\-><]*data-define="CHECKBOX"[\w\d\s\"\'\:\;\=\-><]*\/(th|td)>/ig, "")
						.replace(/rowSpan=\"?([0-9]+)\"?/ig, "rowspan=\"$1\"")
						.replace(/colSpan=\"?([0-9]+)\"?/ig, "colspan=\"$1\"")
						.replace(/class\=([\w\d\-]+)/ig, "")
						.replace(/<br[\s|\/]*>/ig, "")
						.replace(/<colgroup[\w\d\s\"\'\-\=\:\;\/<><]+\/colgroup>/ig, "");
			};

			var conditions = {};
			if(addCondition) {
				$("input,select", info.load.search).filter("[name]").not("[type=hidden]").each(function(i, el){
					var $el = $(el),
					value = null;
	
					if($el.is("select")) value = $el.selectedText();
					else if($el.is("[type=checkbox]")) value = $el.checkedVal();
					else value = $el.val();
	
					if(value) {
						conditions[$el.text().trim()] = value;
					}
				});
			}
			
			var param = info.load.search.toParamObject();
			param["columnSize"] = (info.cellsize - (/data-define=.CHECKBOX/.test(headerHtml) ? 1 : 0));
			param["ss_title"] = title.replace(/\.xls$/, "");
			param["conditions"] = (JSON.stringify(conditions));
			param["information"] = ($("> .info",info.load.search).text());
			param["headerHtml"] = (groomHtml(headerHtml));
			param["bodyHtml"] = (groomHtml(bodyHtml));

			param["page"] = 0;

			$ajaxPost((url||(location.pathname + "/spreadsheet")), param);
		},
		clear: function(){
			unselecting();
			return this;
		},
		sizeInit: function() {
			adjustInit();
			return this;
		},
		resize: function() {
			adjustInit();
			adjustWidth();
			return this;
		},
		render: function() {
			rendering();
			return this;
		},
		unselect: unselecting,
		select: function(index) {
			$("tbody.selected", table).removeClass("selected");
			$("tbody", table).eq(index).click();
		},

		//setter

		setParamValue: function(params) {
			info.load.addParams = params;
			return this;
		},
		setMerge: function(value) {
			info.merge = value;
			return this;
		},
		setFn: function(fn) {
			info.fn = fn;
			return this;
		},
		setReduce: function(value) {
			info.reduce = value;
			return this;
		},
		setUrl: function(url) {
			info.load.url = url;
			return this;
		},
		setSearch: function(selector, enterKeyFn) {
			registrySearch(selector, enterKeyFn);
			return this;
		},
		setEditor: function(selector, enterKeyFn) {
			registryEditor(selector, enterKeyFn);
			return this;
		},
		setPage: function(value) {
			option.pagable = true;
			if(value) for(var i in value) info.page[i] = value[i];
			return this;
		},
		setWrap: function(value) {
			option.wrap = true;
			if(value > 0) info.wrap = value;
			return this;
		},
		setData: function(data) {
			info.load.data = data;
			return this;
		},
		setHidden: function(args) {
			info.hiddens = args;
			return this;
		},
		setSort: function() {
			info.sort.items = arguments;

			for(var i = 0; i < arguments.length; i++) {
				header.find("th[data-name=" + arguments[i] + "]")
				.addClass("sortable")
				.click(function(e){
					var mainOffset = main.offset();
					sort.panel.css({
						top: e.clientY - mainOffset.top - 10,
						left: e.clientX - mainOffset.left - 50
					}).show();

					sort.target = $(this);
				});
			}
			
			return this;
		},

		//getter

		getCheckedIndex: function() {
			var checked = new Array();
			table.find("tbody input.cei-row-checkbox").each(function(i, input){
				if(input.checked) checked.push(input.getAttribute("data-index"));
			});

			return checked;
		},
		getCheckedData: function() {
			var checked = new Array(),
			checkedData = null;
			table.find("tbody input.cei-row-checkbox").each(function(i, input){
				if(input.checked) {
					checkedData = info.load.data[input.getAttribute("data-index")];
					$(input.parentNode.parentNode).find("input[data-name]").each(function(j, input){
						checkedData[input.getAttribute("data-name")] = input.value;
					});

					checked.push(checkedData);
				}
			});

			return checked;
		},
		getSelectedData: function() {
			var index = parseInt(table.find("tbody.selected").attr("data-index") || -1);
			return info.load.data && index > -1 ? info.load.data[index] : null;
		},
		getSelectedIndex: function() {
			return parseInt(table.find("tbody.selected").attr("data-index") || -1);
		},
		getPage: function(item) {
			return item ? info.page[item] : info.page;
		},
		getData: function() {
			return info.load.data;
		},
		getElements: function() {
			return {
				main:	main[0],
				table:	table[0],
				header:	header[0]
			};
		},
		//event

		onload: function(fn) {
			if(typeof fn === "function") events.onload = fn;
			return this;
		},
		ondataload: function(fn) {
			if(typeof fn === "function") events.ondataload = fn;
			return this;
		},
		onselect: function(fn) {
			if(fn === false) option.selectable = false;
			else if(typeof fn === "function") events.onselect = fn;
			return this;
		},
		ondblclick: function(fn) {
			if(typeof fn === "function") events.ondblclick = fn;
			return this;
		},
		onrow: function(fn) {
			if(typeof fn === "function") events.onrow = fn;
			return this;
		},
		oncell: function(fns) {
			events.oncell = fns;
			return this;
		},
		getInfo: function(fn) {
			return info;
		}
		
	};


	//constructor
	$(document).ready(function(){
		table = $("#" + tableId);
		if(table.length === 0) {
			console.error("The table not found!");
			return;
		}

		if(!info.load.search)
			registrySearch("form.search[data-bind=" + tableId + "]");

		if(!info.load.editor)
			registryEditor("form.editor[data-bind=" + tableId + "]");

		if(info.load.editor && info.load.editor.is("[data-fn]")) {
			info.load.editor.append("<div style='clear:both'/>");

			var enterKeyFn = eval(info.load.editor.attr("data-fn"));
			if(typeof enterKeyFn === "function") {
				info.load.editor.submit(function() {
					enterKeyFn();
					return false;
				});

				if(info.load.editor.has("[type=submit],[type=image],button[type=submit]").length == 0)
					info.load.editor.append("<input type='submit' style='position:absolute;top:-999px;left:-999px;opacity:0;filter:alpha('opacity=0')'>");
			}
		}

		main = $("<div class='cei-list'></div>");
		table.before(main)
		.find("tr:eq(0) > th").each(function(i, th){ //cell(column) counting
			info.cellsize += parseInt(th.getAttribute("colSpan") || 1);
		});

		//create colgroup for adjust width size
		var colgroup = $("<colgroup/>");
		for(var i = 0; i < info.cellsize; i++) colgroup.append("<col/>");
		$("thead",table).before(colgroup);

		main.append(table)
		.append("<div class='cei-list-header'><table></table></div>")
		.append("<div class='cei-list-sort'><div class='asc'>오름차순</div><div class='desc'>내림차순</div><input></div>")
		.width(table.style("width") || "100%")
		.height("100%")
		.scroll(function(e){
			header.parent().css("top", this.scrollTop);
		});

		header = $(".cei-list-header > table", main);
		header.append(table.find("colgroup,thead").clone())
		.find("th[data-define=CHECKBOX]").html("<input type='checkbox'>")
		.find("> input").click(function(){
			table.find("input[type=checkbox].cei-row-checkbox").not("[disabled]").checked(this.checked);
		});

		sort.panel = $(".cei-list-sort", main);
		sort.panel
		.mouseleave(function(){
			sort.panel.fadeOut()
		})
		.find("> div").click(function(e){
			var method = $(this).is(".asc") ? "asc" : "desc";
			
			info.sort.orderBy = sort.target.attr("data-name");
			info.sort.orderMethod = method;

			//remove before sort option 
			$("th.asc,th.desc", header).removeClass("asc").removeClass("desc");

			sort.target.addClass(method);
			sort.panel.fadeOut()

			loading();
		});
		

		//tfoot will be paginate or print empty message
		if(table.has("tfoot").length === 0)
			table.append("<tfoot><tr><td colspan='" + info.cellsize + "'></td></tr></tfoot>");

		foot = table.find("tfoot td");

		//remove caption
		var caption = $("caption", table);
		if(caption.length > 0) {
			table.attr("data-caption", caption.text());
			caption.remove();
		}

		//clone body
		info.shape.body = table.find("tbody").addClass("cloner").clone(true);
		table.find("tbody").remove();

		$(window).on("resize", function(){
			if(main.is(":visible")) {
				adjustInit();
				adjustWidth();
			}
		});
	});
	return publicMethod;
};