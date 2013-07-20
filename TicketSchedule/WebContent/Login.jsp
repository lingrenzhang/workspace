<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="./CSS/style.css" type="text/css" rel="stylesheet">
<link href="./CSS/custom_login.css" type="text/css" rel="stylesheet">
<script src="./JS/jquery-1.10.1.js"></script>

<title>Insert title here</title>
</head>
<body>
<div id="content_wrapper">
	<div id="content_container" class="clearfix">
		<div id="login_panel">
		<form method="post" id="login-Form" class="login-form" action="./Login.jsp">
			<dl class="top clearfix">
				<dt><label for="email">username</label></dt>
				<dd style="border-color: rgb(173, 182, 201);">
					<span id="errorMessage" class="errors_div" style="display:none;"></span>
					<input type="text" name="email" class="input-text" id="email" tabindex="1" value="" placeholder="Username" style="color: rgb(136, 136, 136);" >
				</dd>
			</dl>
			<dl class="pwd clearfix">
				<dt><label for="password">password</label></dt>
				<dd>
					<input type="password" id="password" name="password" placeholder="password" error="Please enter your password" class="input-text" tabindex="2" autocomplete="off">
					<!--  <label class="pwdtip" id="pwdTip" for="password">Password</label>-->
				</dd>
			</dl>
			<div class="caps-lock-tips" id="capsLockMessage" style="display:none"></div>
			<dl class="savepassword clearfix">
				<dt>
				<label title="Do not click it in pub environment" for="autoLogin" class="labelCheckbox">
					<input type="checkbox" name="autoLogin" id="autoLogin" value="true" tabindex="4">AutoLogin</input>
				</label>
				</dt>
				<dd>
				<span class="getpassword" id="getpassword"><a href="" stats="home_findpassword">Forget PWD?</a></span>
				</dd>
				</dl>
			<dl id="codeimg" class="codeimg clearfix">
		<dt></dt>
		<dd><img id="verifyPic_login" src="./servlet/CheckCode">
			<a href="javascript:refreshCode_login();" id="swith">Switch</a>
	
		</dd>
		</dl>
		<dl id="code" class="code clearfix">
		<dt><label for="code"></label></dt>
		<dd>
		<input id="icode" type="text" name="icode" class="input-text" tabindex="3" placeholder="Code" autocomplete="off">
		<!--  <label class="codetip" id="codeTip" for="icode">Code</label> -->
		</dd>
		</dl>
		<dl class="bottom">
			<input type="hidden" name="origURL" value="">
			<input type="hidden" name="key_id" value="1">
			<input type="hidden" name="captcha_type" id="captcha_type" value="web_login">
			<input type="submit" id="login" class="input-submit login-btn" stats="loginPage_login_button" value="Login" tabindex="5">
		</dl>
		</form>
			
		</div>
	</div>
</div>

</body>
</html>