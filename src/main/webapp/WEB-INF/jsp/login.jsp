<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@
page	contentType="text/html; charset=utf-8" %><%@
taglib	prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="theme-color" content="#4499dd">
<title>손해보험협회</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
<script src="/js/jquery.js"></script>
</head>
<body>
<script>
function login(){
	var frm = $("form[name=frm]");
	var id = $('#mb_id').val();
	var password = $('#mb_password').val();
	
	if(id == null || id == ''){
		alert("아이디를 입력해주시기 바랍니다.");
		return;
	}
	
	if(password == null || password == ''){
		alert("비밀번호를 입력해주시기 바랍니다.");
		return;
	}
	
	$.ajax({
		type: "POST",
		url: frm.attr("action"),
		data: frm.serialize(),
		complete: function(r) {
			var result = JSON.parse(r.responseText);

			if(result === "success") location.href = "/";
			else {
				alert("아이디 또는 비밀번호가 잘못되었습니다.");
			}
		}
	});
}

function EnterCheck(){
	if(event.keyCode == '13'){
		login();	
	}
}
</script>
<div id="egg">
	<div id="container" class="app">
	
		<div class="header wrap">
				<p class="title"><a href="#" style="font-size:20px;font-weight:700;line-height:35px;white-space:nowrap;text-indent:170px">대외전산망 업무지원 시스템</a></p>
		</div>
		<div id="body" class="body">

			<div class="wrap">
				<div class="login">
					<div class="login_wrap">  
						<form name="frm" method="post" action="/login">
						<div class="login_form">
							<div class="field">
								<label for="mb_id">아이디</label>
								<input id="mb_id" type="text" class="text" name="id" value=""/>
							</div>
							<div class="field">
								<label for="mb_password">비밀번호</label>
								<input type="password" class="text" name="password" id="mb_password" value="" onkeydown="EnterCheck();"/>
							</div>
							<button type="button" class="submit" onclick="login();">로그인</button>
							<div class="field">
								<!-- <p class="mb_id_save">
									<input id="mb_id_save" type="checkbox" name="mb_id_save" value="" /> <label for="mb_id_save">아이디 저장</label>
								</p> --> 
							</div>
							<div class="link_ip"><!-- 접속IP는 " 192.168.207.126 " 입니다. --></div>
						</div>
						
						</form>
						<div class="link">
							<!--
							<span class="btn-arrow hover"><a href="/hcms/member/"><em>회원가입</em></a></span>
							<span class="btn-arrow hover"><a href="/hcms/passSearch/"><em>비번찾기</em></a></span>
							-->
						</div>
						<div class="link">
						<font color="gray">* 이 시스템은 window7 이상 및 인터넷브라우져 Chrome, IE9 버전 이상에 최적화 되어 있습니다.</font>
						<!-- 
						<p>Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36</p>
						-->
						</div>
					</div>
				</div>
			</div>
			
		</div>
		
		<div class="footer">
			<div class="wrap">
				<address>서울특별시 종로구 종로5길 68, 2, 6, 7, 8층(수송동, 코리안리빌딩) 대표전화 : 02-3702-8500 FAX : 02-3702-8690 사업자번호 : 102-82-04254</address>
				<p class="copyright">(C) GENERAL INSURANCE ASSOCIATION OF KOREA. AII RIGHTS RESERVED.</p>
			</div>
		</div><!-- //footer -->
	</div><!-- //container -->
</div><!-- //egg -->
</body>
</html>