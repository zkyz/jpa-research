(function(factory){
	if(typeof define === 'function' && define.amd){
		define(['jquery'], factory);
	}else{
        factory(window.jQuery);
    }
}(function($){
	"use strict"
	
	$.fn.selectedText = function() {
		if(this[0] && this[0].nodeName.toLowerCase() === "select")
			return this[0].options[this[0].selectedIndex].text;
		return "";
	};
	$.fn.checked = function(flag) {
		if(flag === undefined) {
			return (this[0] && this[0].checked);
		} 
		else {
			this.each(function(i, el){
				el.checked = flag;
			});
	
			return this;
		}
	};
	$.fn.checkedVal = function() {
		var r = "";
		this.each(function(i, el){
			if(el.checked) r += "," + el.value;
		});
		return r.substring(1);
	};
	$.fn.unselection = function() {
		this.each(function(i, el){
			el.style.MozUserSelect = el.style.WebkitUserSelect = "none";
			if($.browser === "ie") el.onselectstart = function(){return false};
		});
		return this;
	};
	$.fn.toParam = function() {
		var p = "";
		this.each(function(i, form) {
			$(form).find("input,select,textarea").filter("[name]").each(function(j, el){
				var value = $(el).val(),
				classes = el.className;
	
				if(classes) {
					if(classes.indexOf("date") > -1 || classes.indexOf("tel") > -1) value = value.replace(/-/g, "");
					else if(classes.indexOf("number") > -1) value = value.replace(/,/g, "");
				}
	
				if(el.type !== "checkbox" && el.type !== "radio") p += "&" + el.name + "=" + encodeURIComponent(value);
				else if(el.checked) p += "&" + el.name + "=" + encodeURIComponent(value);
			});
		});
		return p.substring(1);
	};
	$.fn.toParamObject = function(fn) {
		var paramObject = {};
		this.find("input,select,textarea").filter("[name]").each(function(i, el){
			var value = $(el).val(),
			classes = el.className;
	
			if(classes) {
				if(classes.indexOf("date") > -1 || classes.indexOf("tel") > -1) value = value.replace(/-/g, "");
				else if(classes.indexOf("number") > -1) value = value.replace(/,/g, "");
			}
	
			if(el.type !== "checkbox" && el.type !== "radio") paramObject[el.name] = encodeURIComponent(value);
			else if(el.checked) paramObject[el.name] = encodeURIComponent(value);
		});
	
		return paramObject;
	};
	$.fn.disabled = function(b) {
	    this.each(function(i, el) {
	        if(/^INPUT$/i.test(el.nodeName)) {
	            el.disabled = b;
	        }
	    });
	};
	$.fn.style = function(key) {
		var result = null;
	
		if(this[0]) {
			if(result = eval("/" + key + ":([a-zA-Z0-9\\s\\-\\_\\%]+);?/").exec(this.attr("style")))
				return result[1].trim();
		}
	
		return "";
	};
	$.fn.post = function(fn) {
		if(this.length === 1 && this[0].nodeName.toLowerCase() === "form") {
			if(this[0].getAttribute("action")) {
				this.submit(function(){
					$.post(form.getAttribute("action"), $(form).toParam(), function(r){
						try {
							fn(r && r.nodeType === 9 ? r : eval("(" + r + ")"))						
						}
						catch(e) {
							fn(e);
						}
					});
					
					return false;
				});
			}
		}
	
		return this;
	};
	$.fn.ajax = function(fn) {
		var form = null;
		if(this.length === 1 && (form = this[0]).nodeName.toLowerCase() === "form") {
			if(form.getAttribute("action")) {
				$.ajax({
					type: form.getAttribute("method") || "POST",
					url: form.getAttribute("action"),
					data: $(form).toParam(),
					complete: function(r) {
						fn(r);
					}
				});
				return;
			}
		}
	
		console.log("XHR fail!");
	};

	var req = function() {
		var parameter = function(p) {
			if(p == null) return null
			else if(typeof p === "string") return p
			else if(p.jquery && p.is("form")) return p.serialize()
			else if($.isPlainObject(p)) return JSON.stringify(p)
		},
		suppressError = function(r) {
			alert("처리 중 오류가 발생하였습니다. 관리자에게 문의 바랍니다.")
		}
		
		return {
			"post": function(url, param, fn, async) {
				if(typeof param === "function") {
					async = fn
					fn = param 
				}

				$.ajax({
					"type":		"POST",
					"async":	async === undefined || async,
					"url":		url,
					"data":		parameter(param),
					"xhrFields":	{
						"withCredentials": true
					},
					"beforeSend":	function(xhr) {
						xhr.setRequestHeader("CEI-XmlHttpRequest", "jquery-X-Requested-With")
					},
					"success":	function(r){
						if(typeof fn === "function") {
							return fn(r)
						}
					},
					"error":	function(r) {
						if(suppressError(r) && typeof fn === "function") {
							try { 
								return fn($.parseJSON(r))
							}
							catch(e) {
								return fn(r.responseText)
							}
						}
					}
				})
			}
		}
	}()
}))