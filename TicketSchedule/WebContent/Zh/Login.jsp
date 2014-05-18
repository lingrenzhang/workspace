<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="../CSS/style.css" type="text/css" rel="stylesheet">
<link href="../CSS/custom_login.css" type="text/css" rel="stylesheet">
<script src="../JS/jquery-1.10.1.js"></script>
<script>
function refreshCode_login(){
	document.getElementById("verifyPic_login").src="/TicketSchedule/servlet/CheckCode";
}

$(document).ready(function(){

	//------------------------register listener-------------------------
	$("#register").click(function(){
    	document.getElementById("method").value = "register";
	});
})
</script>

<title>登录</title>
</head>
<body>
<div id="content_wrapper">
	<div id="content_container" class="clearfix">
		<div id="login_panel">
		<form method="post" id="login-Form" class="login-form" action="/TicketSchedule/servlet/Login">
			<dl class="top clearfix">
				<dt><label for="email">用户名</label></dt>
				<dd style="border-color: rgb(173, 182, 201);">
					<span id="errorMessage" class="errors_div" style="display:none;"></span>
					<input type="text" name="email" class="input-text" id="email" tabindex="1" value="" placeholder="用户名" style="color: rgb(136, 136, 136);" >
				</dd>
			</dl>
			<dl class="pwd clearfix">
				<dt><label for="password">密码</label></dt>
				<dd>
					<input type="password" id="password" name="password" placeholder="密码" error="Please enter your password" class="input-text" tabindex="2" autocomplete="off">
					<!--  <label class="pwdtip" id="pwdTip" for="password">Password</label>-->
				</dd>
			</dl>
			<div class="caps-lock-tips" id="capsLockMessage" style="display:none"></div>
			<dl class="savepassword clearfix">
				<dt>
					<label title="Do not click it in pub environment" for="autoLogin" class="labelCheckbox">
					<input type="checkbox" name="autoLogin" id="autoLogin" value="true" tabindex="4">自动登录</input>
					</label>
				</dt>
				<dd>
					<span class="getpassword" id="getpassword"><a href="" stats="home_findpassword">忘记密码？</a></span>
				</dd>
			</dl>
			<dl id="codeimg" class="codeimg hidden">
				<dt></dt>
				<dd><img id="verifyPic_login" src="./servlet/CheckCode">
					<a href="javascript:refreshCode_login();" id="swith">Switch</a>
	
				</dd>
			</dl>
			<dl id="code" class="code hidden">
				<dt><label for="code"></label></dt>
				<dd>
					<input id="icode" type="text" name="icode" class="input-text" tabindex="3" placeholder="Code" autocomplete="off">
					<!--  <label class="codetip" id="codeTip" for="icode">Code</label> -->
				</dd>
			</dl>
			<dl class="bottom">
			</dl>
			<input type="hidden" name="language" value="Zh"/>
			<input type="hidden" name="origURL" value=""/>
			<input type="hidden" name="key_id" value="1"/>
			<input type="hidden" name="captcha_type" id="captcha_type" value="web_login"/>
			<input type="hidden" name="method" id="method" value = ""/>
			<input type="submit" id="login" class="input-submit login-btn" stats="loginPage_login_button" value="登录" tabindex="5"/>
			<input type="button" id="register" class="input-submit regist-btn" stats="loginPage_regist_button" value="注册" tabindex="6"
				 onclick="window.location = '/TicketSchedule/Zh/register.jsp'"/>
		</form>
			
		</div>
	</div>
</div>
<input type="text" class="hidden" value="1"></input>
</body>
</html>