app.controller("ReplyController", function($scope, $http, $stateParams, $timeout, $interval) {
	$scope.inquiry = {
		group: "REPLY",
		categoryId: $stateParams.cid || "ALL",
		searchBy: "ALL",
		orderBy: "CATE_ORD",
		orderMethod: "ASC",
		keyword: "",
		page: 0
	}
	
	$scope.iam = {
		id: $("html").data("id")
	}
	
	// 목록 불러오기
	$scope.list = [];
	$scope.load = function(id, continuely) {
		if(!continuely)
			$(".waiting").show();

		id = id || $scope.inquiry.categoryId;
		charim.chosen(id);

		$http.post("/reply/" + id, $scope.inquiry).success(function(data) {
			if(id.toUpperCase() === "ALL" && !$scope.inquiry.keyword) {
				
				if($scope.inquiry.page === 0) {
					$scope.list = [];
				}

				$scope.list = $scope.list.concat(data);
				

				if(data.length >= 20) {
					$scope.inquiry.page = $scope.inquiry.page + 1;
					$scope.load(id, true);
				}
				else {
					$scope.inquiry.page = 0;
				}
			}
			else {
				$scope.list = data;
			}
			
			$("#rowCount").html($scope.list.length);
			
			$timeout(function(){
				$(".waiting").fadeOut();
			}, 500)
		})
	}
	
	$scope.load();

	// 글쓰기 카테고리 가져오기
	$http.get("/category/children?id=-")
		.success(function(data){
			$scope.pcodes = data
		})

	// 목록에서 선택된 글 불러오기
	$scope.selectArticle = function($event, article) {
		$(".article-items .selected").removeClass("selected")
		
		if($event) {
			$($event.target.parentNode).addClass("selected")
		}
		
		var src = article.html;
		if (src)
			src = src.replace(/(.+?)([\\|/]reply.+)/, "$2").replace(/\\/, "/");
		
		$("#for-content").attr("src", src)
			.on("load", function(){
				$(".article-empty").hide();
			});

		$scope.article = article;
		charim.chosen(article.categoryId);
	}
	
	// 파일다운로드 검증
	$scope.validFilePath = function($event) {
		if (/file\/\/.+/.test($event.currentTarget.href)) {
			alert("열람 대상을 선택한 후, 파일도 다운로드가 가능합니다.");
			$event.preventDefault();
		}
	}

	// 댓글보기
	$scope.goReply = function() {
		var win = $("#for-content")[0].contentWindow;
		
		$(win.document.body).add(win.document.documentElement).animate({
			scrollTop: win.document.body.scrollHeight
		}, 1000);
	}
	
	$scope.viewHistory = function() {
		$(window).trigger("resize");
	}
	
	$scope.remove = function() {
		if(!$scope.article) {
			alert("글 목록에서 삭제하고픈 회신문을 선택하시고 난 후 가능합니다.");
			return false;
		}
		else if(!confirm("삭제하시겠습니까?")) {
			return false;
		}

		$http.post("/reply/remove", $scope.article)
			.success(function(){
				$scope.article = {};
				$scope.load();
			})
	}

	// 글쓰기
	$scope.writeReply = function(mode) {
		if(mode === "edit") {
			$(".for-etc").show();
			
			if(!$scope.article) {
				alert("글 목록에서 수정하고픈 회신문을 선택하시고 난 후 가능합니다.");
				return false;
			}

			$("#title").val($scope.article.title);
			$("[name=no]").val($scope.article.no);

			$scope.selectedPCode = $scope.article.category[0].pid;
		}
		else {
			$(".for-etc").hide();
			$("input,textarea", "#write-reply").val("");
		}

		$("#write-reply").modal({
			backdrop: false
		});
	}

	// 글쓰기의 상위 분류 코드가 변경될때 처리
	$scope.$watch("selectedPCode", function() {
		if ($scope.selectedPCode) {
			$http.get("/category/children?id=" + $scope.selectedPCode)
				.success(function(data) {
					$scope.codes = data;

					if($scope.article && $scope.article.category) {
						$scope.categoryId = $scope.article.category[0].id;
					}
				})
		}
	})

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
		
		if($scope.inquiry.searchBy != $(this).data("searchBy")) {
			$scope.inquiry.searchBy = $(this).data("searchBy");
	
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

	// 글목록에 이쁜이 스크롤
	$timeout(function(){
		$("#article-list").enscroll({})
	}, 1000)

	$("form[name=save]").submit(function() {
		if(!$("#title").val()) {
			alert("제목을 입력해 주세요.")
			$("#title").focus();
			return false;
		}
		else if(!$("[name=categoryId]").val()) {
			alert("분류를 선택해주세요.")
			return false;
		}
		else if($("[name=no]").val() && !$("#etc").val()) {
			alert("변경사항 메모를 입력해주세요.");
			$("#etc").focus();
			return false;
		}
		else {
			$("#write-group").val("REPLY");
		}
	});
	
	$("iframe[name=for-save").on("load", function(){
		if(this.contentWindow.location.pathname === "/blank") {
			return 
		}

		alert("저장하였습니다.");
		$("#write-reply").modal("hide");
		$scope.load();
	})
	$("#write-reply").on("show.bs.modal", function() {
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
	});

	
	// 열람 순위
	$http.get("/popular")
		.success(function(data){
			$scope.popular = data

			$interval(function(){
				var def = {
					height: 60,
					tall: 300,
					top: 0
				}, 
				list = $(".rank-list")
					.mouseenter(function(){
						$(this.parentNode).addClass("open")
					})
					.mouseleave(function(){
						def.top = 0
						list.css("top", def.top)
						$(this.parentNode).removeClass("open")
					})

				return function() {
					if(!list.parent().is(".open")) {
						def.top += def.height;

						if(def.top >= def.tall)
							def.top = 0;

						list.css("top", def.top * -1)
					}
				}
			}(), 3000)
		})
})