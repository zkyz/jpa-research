var app = angular.module("replyApp",
		[
//			"ngAnimate",
			"ngSanitize",
			"ui.router"
	 	]
	)
	.config(function($stateProvider, $urlRouterProvider) {
//		$urlRouterProvider.otherwise("/")

		$stateProvider
		.state("supervision-user", {
			url: "/supervision/user",
			controller : "UserController",
			templateUrl : "view/user.html"
		})
		.state("supervision-statistics", {
			url: "/supervision/statistics",
			controller : "StatisticsController",
			templateUrl : "view/statistics.html"
		})
		.state("supervision-comments", {
			url: "/supervision/comments",
			controller : "CommentsController",
			templateUrl : "view/comments.html"
		})
	})
;

var charim = {
	chosen: function(id) {
		$("#charim .active").removeClass("active");
		$("#charim .charim-open").fadeOut().removeClass("charim-open");

		var cel = $("#charim").find("a[href$=" + id + "]");
		
		if(cel.size() > 0) {
			cel.find("+ul").addClass("charim-open").stop().fadeIn();
			$("#search-keyword").attr("placeholder", "\"" + cel.text() + "\" 검색");
		
			while (!(cel = cel.parent()).is(".fixed-menu")) {
				if (cel.is("li")) {
					cel.addClass("active")
				}
				else if (cel.is("ul")) {
					cel.addClass("charim-open").stop().fadeIn();
				}
			}
		}
	}
}
	
$(document).ready(function(){
	$("#charim ul li").has("ul").addClass("group")
	
	$("#charim>li>ul li")
	.find("a").click(function(){
		if($("+ul.charim-open", this).length > 0) {
			var el = $(this.parentNode)
			while (!el.is("#charim>li")) {
				el.addClass("active")
				el = el.parent().parent()
			}
			$("#charim .charim-open").fadeOut().removeClass("charim-open")
			return
		}

		try {
			charim.chosen(/\w+$/.exec(this.href)[0]);
		}
		catch(e) {}
	})

	// menu scroll
	$(".aside-menu").enscroll({})
	
	if(!location.hash) {
		location.hash = "#/supervision/user";
	}
	
	$("#call-help").click(function(){
		var id = location.hash.substring(1),
		title = " 도움말";

		if(/^\/reply/.test(id)) {
			id = "/reply";
			title = "모범회신문 도움말"
		}
		else {
			title = $("#charim").find(".active").last().text() + " 도움말"
		}

		$("#helper .modal-title").html(title);

		$.ajax({
			url: "/help?id=" + id,
			success: function(r) {
				if(r) $("#helper-content").css("height", r.split("\n").length * 21)

				$("#helper-content").val(r)
				$("#helper").modal("show")
			}
		})
	})
})

// 열람 화면 크기 조정
$(window)
.on("resize", function(){
	try {
		var a = $("#article-content");
		a.height($("#ter").height() - a.offset().top);
	}
	catch(e) {
	}
})
/*
.on("beforeunload", function(){
	$.ajax({
		async: false,
		url: "/logout-async"
	})
})
*/

function saveHelp() {
	var id = location.hash.substring(1);
	
	if(/^\/reply/.test(id)) {
		id = "/reply";
	}

	$.ajax({
		type: "post",
		url: "/help",
		data: "id=" + encodeURIComponent(id) + "&content=" + encodeURIComponent($("#helper-content").val()),
		success: function(r) {
			alert("저장하였습니다.")
		}
	})
}