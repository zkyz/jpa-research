app.controller("UserController", function($scope, $http) {
	charim.chosen("user");
	
	$scope.comm = (function() {
		$http.get("/supervision/user/corps")
		.success(function(data){
			$scope.corps = data;
		});

		return {
			add: function() {
				$scope.list.clear();
				$("[name=id]").focus();
			},
			save: function() {
				console.log(this);
			}
		}
	})()
	
	$scope.inquiry = {
		keyword: "",
		searchBy: "ALL",
		orderBy: "ID",
		orderMethod: "DESC"
	}
	
	$scope.list = new List("user")
		.setUrl("/supervision/user")
		.setEditor(".panel")
		.setParamValue($scope.inquiry)
		.onload(function(data){
			$("#rowCount").text(data.length);
		})
		.load();

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
			$scope.list.load();
		}
	})
	.keyup(function(e){
		if((e.keyCode||e.which) === 13) {
			if($(this).data("before") != this.value){
				$(this).data("before", this.value);
				$scope.list.load();
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
					$scope.list.load()
				}, 200);
			}
		}
	})

	// 정렬기준 명시
	$("a[data-order-by]").click(function(){
		$("#order-by").text($(this).text())
		$scope.inquiry.orderBy = $(this).data("order-by");
		$scope.list.load();
	})
	// 정렬방식 명시
	$("a[data-order-method]").click(function(){
		$(".ti-check", this.parentNode.parentNode).removeClass("ti-check").addClass("empty")
		$(".empty", this).removeClass("empty").addClass("ti-check")
		$scope.inquiry.orderMethod = $(this).data("order-method");
		$scope.list.load();
	})

	$("form.panel").submit(function(){
		if($("html").is(".B")) {
			$("select[name=group]").val($("html").data("corp"));
		}

		if(!$("[name=id]", this).val()) {
			alert("아이디를 입력해 주세요.")
			$("[name=id]", this).focus();
			return false;
		}
		else if(!$("[name=name]", this).val()) {
			alert("이름을 입력해 주세요.")
			$("[name=name]", this).focus();
			return false;
		}
		else if(!$("[name=group]", this).val()) {
			alert("회사를 입력해 주세요.")
			$("[name=group]", this).focus();
			return false;
		}

		$.ajax({
			type: "PUT",
			contentType: "application/json",
			url: "/supervision/user",
			data: function(items) {
				var param = {};
				$(items).each(function(i,item){
					param[item.name] = item.value;
				})
				return JSON.stringify(param)
			}($(this).serializeArray()),
			success: function(r) {
				$scope.list.load();
			}
		})
	})
	
	// 기능 버튼 도움말 활성화 
	$("#fn-buttons>a").tooltip({
		"placement": "bottom"
	})
})