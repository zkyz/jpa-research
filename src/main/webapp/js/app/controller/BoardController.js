app.controller("BoardController", function($scope, $http, $stateParams, $timeout, $interval) {
	charim.chosen($stateParams.cid);

	if($stateParams.cid === "notice") {
		if(!$("html").is(".A")) {
			$("#fn-buttons > a").hide()
		}
	}
	
	$scope.inquiry = {
		group: $stateParams.cid,
		categoryId: "",
		searchBy: "ALL",
		orderBy: "UPDATED",
		orderMethod: "DESC",
		keyword: ""
	}
	
	$scope.iam = {
		id: $("html").data("id")
	}

	// 목록 불러오기
	$scope.load = function(fn) {
		$(".waiting").show();

		$http.post("/board/" + $stateParams.cid, $scope.inquiry).success(function(data) {
			$scope.list = data;
			
			if(typeof fn === "function") {
				fn();
			}
			
			$timeout(function(){
				$(".waiting").fadeOut();
			}, 500)
		})
	}
	
	$scope.load();

	// 목록에서 선택된 글 불러오기
	$scope.selectArticle = function($event, article) {
		$(".article-items .selected").removeClass("selected")

		if($event) {
			$($event.target.parentNode).addClass("selected")
		}

		$scope.article = article;
		$scope.loadReply(article.no);

		$(".article-empty").hide();

		setTimeout(function(){
			$(window).trigger("resize");
		}, 1000)
	}

	// 댓글보기
	$scope.goReply = function() {
		$("#article-content").animate({
			scrollTop: 9999
		}, 1000)
	}
	$scope.loadReply = function(no) {
		$(".waiting-reply").show();

		$http.post("/board/replies?no=" + no).success(function(data) {
			$scope.replies = data;

			$timeout(function(){
				$(".waiting-reply").fadeOut();
			}, 1000)
		})
	}

	// 글쓰기
	$scope.writeBoard = function(mode) {
		if(mode === "edit") {
			if(!$scope.article) {
				alert("글 목록에서 수정하고픈 게시글을 선택하시고 난 후 가능합니다.");
				return false;
			}
			
			$("#title").val($scope.article.title);
			$("[name=no]").val($scope.article.no);
			$scope.contentEditor.summernote("code", decodeURIComponent($scope.article.content));
		}
		else {
			$("input,textarea", "#write-board").val("");
			$scope.contentEditor.summernote("code", "");
		}

		$("#write-board").modal({
			backdrop: false
		});
	}

	// 검색 조건
	$("#search-keyword")
	.focus(function(){
		$(".search-types").stop().slideDown()
	})
	.blur(function(){
		if(!this.value) {
			$(".search-types").delay(200).slideUp().find(".active").removeAttr("active")
		}
		else if($(this).data("before") != this.value){
			$(this).data("before", this.value);
			$scope.load();
		}
	})
	.keyup(function(e){
		if((e.keyCode||e.which) === 13) {
			if($(this).data("before") != this.value){
				$(this).data("before", this.value);
				$scope.load();
			}
		}
		else if((e.keyCode||e.which) === 27) {
			$(this).blur()
			//$(".search-types").slideUp().find(".active").removeAttr("active")
		}
	})

	// 검색조건 명시
	$(".search-types")
	.click(function(){
		$(this).stop();
		$("#search-keyword").focus();
	})
	.on("click", "li", function(){
		$(".active", this.parentNode).removeClass("active")
		this.className = "active";
		
		if($scope.inquiry.searchBy != $(this).data("search-by")) {
			$scope.inquiry.searchBy = $(this).data("search-by");
	
			if($scope.inquiry.keyword) {
				setTimeout(function(){
					$scope.load()
				}, 200);
			}
		}
	})

	// 정렬기준 명시
	$("a[data-order-by]").click(function(){
		$("#order-by").text($(this).text())
		$scope.inquiry.orderBy = $(this).data("order-by");
		$scope.load();
	})
	// 정렬방식 명시
	$("a[data-order-method]").click(function(){
		$(".ti-check", this.parentNode.parentNode).removeClass("ti-check").addClass("empty")
		$(".empty", this).removeClass("empty").addClass("ti-check")
		$scope.inquiry.orderMethod = $(this).data("order-method");
		$scope.load();
	})

	// 기능 버튼 도움말 활성화 
	$("#fn-buttons>a").tooltip({
		"placement": "bottom"
	})
	
	// 글쓰기 에디터
	$scope.contentEditor = $("#content-editor");
	$scope.contentEditor.summernote({
		height: "300px",
		lang: "ko-KR",
		toolbar : [
			['style',		[ 'bold', 'italic', 'underline', 'clear' ] ],
			['font',		[ 'strikethrough', 'superscript', 'subscript' ] ],
			['fontsize',	[ 'fontsize' ] ],
			['color',		[ 'color' ] ],
			['para',		[ 'ul', 'ol', 'paragraph' ] ],
			['height', 	[ 'height' ] ] 
		]
	});

	$scope.saveReply = function() {
		$(".waiting-reply").show();

		$http.post("/board/comment/save", {
			no: $scope.article.no,
			content: $scope.reply.content
		})
		.success(function(data){
			$scope.loadReply($scope.article.no);
		})
	}

	$scope.removeReply = function(comment) {
		if(confirm("삭제할까요?")) {
			$(".waiting-reply").show();
	
			$http.post("/board/comment/remove", comment)
			.success(function(data){
				$scope.loadReply(comment.no);
			})
		}
	}

	$scope.remove = function() {
		if(!confirm("삭제하시겠습니까?")) {
			return false;
		}

		$http.post("/board/remove", $scope.article)
			.success(function(){
				$scope.article = null;
				$scope.load();
			})
			.error(function(){
				alert("오류가 발생하였습니다. 잠시 후 다시 시도해 주시기 바랍니다.");
			})
	}

	$("form[name=save]").submit(function() {
		if(!$("#title").val()) {
			alert("제목을 입력해 주세요.")
			$("#title").focus();
			return false;
		}
		else if(!$scope.contentEditor.summernote("code").replace(/<\/?[^>]+(>|$)/g, "")) {
			alert("내용을 입력해주세요.");
			$scope.contentEditor.summernote("focus");
			return false;
		}
		else {
			$("#write-group").val($scope.inquiry.group);
			$("#write-content").val(encodeURIComponent($scope.contentEditor.summernote("code")));
		}
	});

	$("#write-board").on("show.bs.modal", function() {
		$(".file-area > input[type=file]").val("").off().change(function() {
			if (!this.value) {
				$(this.parentNode).removeClass("chosen")
					.find("span").remove();
			}
			else {
				var fileName = this.value;
				
				if (fileName.lastIndexOf("\\") > -1) {
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
				}
				else if (fileName.lastIndexOf("/") > -1) {
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				}
	
				$(this.parentNode).find("span").remove();
				$(this.parentNode).addClass("chosen").append("<span>" + fileName + "</span>");
			}
		});
		
		
	})
	.on("shown.bs.modal", function(){
		$(".modal-backdrop").remove()
	});

	$("iframe[name=for-save").on("load", function(){
		var no = parseInt($(this.contentWindow.document).text());

		$scope.load(function(){
			if(no) {
				for(var i = 0; i < $scope.list.length; i++) {
					if($scope.list[i].no === no) {
						$scope.article = $scope.list[i];
						
						$timeout(function(i){
							return function() {
								$("#list_item_" + i).addClass("selected");
							}
						}(i));

						break;
					}
				}
			}

			$("#write-board").modal("hide");
		});
	})

	// 글목록에 이쁜이 스크롤
	$timeout(function(){
		$("#article-list").enscroll()
		$("#article-content").css({
			overflow: "auto"
		});
	}, 1000)
})