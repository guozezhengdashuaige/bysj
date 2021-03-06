<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<title>随便美餐-用户</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/styles.css">
<link href="${ctx}/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<script src="${ctx}/js/jquery-2.2.2.min.js"></script>
<script src="${ctx}/bootstrap/js/bootstrap.min.js"  type="text/javascript"></script>
</head>
<body>
<div class="htmleaf-container">
		<c:if test="${!empty err}">
			<div style="width:30%;margin: 0 auto;margin-top:160px;position:relative;z-index:999;" class="alert alert-danger">
						<a class="close" data-dismiss="alert" href="#" aria-hidden="true">
							&times; </a><p style = "text-align:center"> ${err}</p>
			</div>
		</c:if>
	<div class="wrapper">
		<div class="container">
			<h1>随便美餐</h1>
			
			<form id="loginForm" action="${ctx}/user/user_doLogin.action" method="post">
				<input type="text" name="loginName" placeholder="请输入用户名">
				<input type="password"  name="password" placeholder="请输入密码">
				<input type="submit" id="login-button" value="登录">
				<a  href="${ctx}/user/user_register.action">注册用户</a>
			</form>
		</div>
		<ul class="bg-bubbles">
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
		</ul>
	</div>
</div>
<script>
function validate(){
	  document.getElementById('loginForm').submit();
	}
$('#login-button').click(function (event) {
	event.preventDefault();
	$('form').fadeOut(500);
	$('.wrapper').addClass('form-success');
	window.load=validate();
});
</script>
<c:if test="${!empty err}">
	<div style="text-align:center;margin:50px 0; font:normal 14px/24px 'MicroSoft YaHei';color:#000000">
	
	</div>
</c:if>

</body>
</html>