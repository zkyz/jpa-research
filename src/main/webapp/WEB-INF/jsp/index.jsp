<!DOCTYPE html><%@
page	contentType="text/html; charset=utf-8" %>
<html lang="ko" class="A"
		data-corp="N00"
		data-id="admin">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="theme-color" content="#4499dd">
	<title>대외전산망 업무지원 시스템 (손해보험협회)</title>
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<link rel="stylesheet" href="/css/themify-icons.css">
	<link rel="stylesheet" href="/css/summernote.css">
	<link rel="stylesheet" href="/css/daterangepicker.css">
	<link rel="stylesheet" href="/css/base.css">
	<link rel="stylesheet" href="/css/cei/list.css">
	<link rel="stylesheet" href="/css/articles.css">
	<link rel="stylesheet" href="/css/reply.css">
</head>
<body>
	<div id="tul" data-ng-app="replyApp">
		<div class="aside">
			<h1 class="title"><small>손해보험협회</small>대외전산망 업무지원 시스템</h1>
			<div class="aside-menu">
				<ul id="charim">
					<li><a><span>관리</span></a>
						<ul class="fixed-menu">
							<li class="grade-B"><a href="#/supervision/user"><i class="ti-user"></i><span>사용자</span></a></li>
							<li><a id="call-help"><i class="ti-help-alt"></i><span>도움말</span></a></li>
							<li><a href="/login"><i class="ti-close"></i><span>로그아웃</span></a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
		<div id="ter" ui-view></div>
	</div>

	<div id="helper" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
					<h4 class="modal-title">도움말</h4>
				</div>
				<div class="modal-body">
					<textarea id="helper-content"></textarea>
					
					<div class="btn-area grade-A">
						<button onclick="saveHelp()" class="btn btn-primary">저장</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Based required libraries -->
	<script src="/js/jquery.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/angular.min.js"></script>
	<script src="/js/angular-sanitize.min.js"></script>
	<!-- <script src="/js/angular-animate.min.js"></script> -->
	<script src="/js/angular-ui-router.min.js"></script>
	<script src="/js/summernote.js"></script>
	<script src="/js/moment.min.js"></script>
	<script src="/js/daterangepicker.js"></script>
	<script src="/js/lang/summernote-ko-KR.min.js"></script>
	<script src="/js/enscroll-0.6.2.min.js"></script>
	<script src="/js/select2.min.js"></script>
	<!-- Custom libraries by (C)Cheoeumin -->
	<script src="/js/app/List.js"></script>
	<script src="/js/cei.doum.jquery.js"></script>
	<script src="/js/cei.ter.js"></script>

	<!-- Application -->
	<script defer="defer" src="/js/app/core.js"></script>
	<script defer="defer" src="/js/app/controller/ReplyController.js"></script>
	<script defer="defer" src="/js/app/controller/BoardController.js"></script>
	<script defer="defer" src="/js/app/controller/UserController.js"></script>
	<script defer="defer" src="/js/app/controller/StatisticsController.js"></script>
	<script defer="defer" src="/js/app/controller/CommentsController.js"></script>
</body>
</html>