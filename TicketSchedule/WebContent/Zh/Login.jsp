<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%!// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),  
	// 字符串在编译时会被转码一次,所以是 "\\b"  
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)  
	String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
			+ "|windows (phone|ce)|blackberry"
			+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
			+ "|laystation portable)|nokia|fennec|htc[-_]"
			+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
			+ "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

	public boolean checkMobile(String userAgent) {
		if (null == userAgent) {
			userAgent = "";
		}
		// 匹配  
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return true;
		} else {
			return false;
		}
	}%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	//  
	String userAgent = request.getHeader("USER-AGENT").toLowerCase();

	if (null == userAgent) {
		userAgent = "";
	}
	if (checkMobile(userAgent)) {
		response.sendRedirect(basePath + "m/login.html");  
	} else {
	}
	//
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="../CSS/style.css" type="text/css" rel="stylesheet">
<link href="../CSS/custom_login.css" type="text/css" rel="stylesheet">
<link rel="shortcut icon" href="/TicketSchedule/favicon.ico"
	type="image/x-icon" />
<script src="../JS/jquery-1.10.1.js"></script>
<script>
	function refreshCode_login() {
		document.getElementById("verifyPic_login").src = "/TicketSchedule/servlet/CheckCode";
	}

	$(document).ready(function() {

		//------------------------register listener-------------------------
		$("#register").click(function() {
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
				<form method="post" id="login-Form" class="login-form"
					action="/TicketSchedule/servlet/Login">
					<dl class="top clearfix">
						<dt>
							<label for="email">用户名</label>
						</dt>
						<dd style="border-color: rgb(173, 182, 201);">
							<span id="errorMessage" class="errors_div" style="display: none;"></span>
							<input type="text" name="email" class="input-text" id="email"
								tabindex="1" value="" placeholder="用户名"
								style="color: rgb(136, 136, 136);">
						</dd>
					</dl>
					<dl class="pwd clearfix">
						<dt>
							<label for="password">密码</label>
						</dt>
						<dd>
							<input type="password" id="password" name="password"
								placeholder="密码" error="Please enter your password"
								class="input-text" tabindex="2" autocomplete="off">
							<!--  <label class="pwdtip" id="pwdTip" for="password">Password</label>-->
						</dd>
					</dl>
					<div class="caps-lock-tips" id="capsLockMessage"
						style="display: none"></div>
					<dl class="savepassword clearfix">
						<dt>
							<label title="Do not click it in pub environment" for="autoLogin"
								class="labelCheckbox"> <input type="checkbox"
								name="autoLogin" id="autoLogin" value="true" tabindex="4">自动登录</input>
							</label>
						</dt>
						<dd>
							<span class="getpassword" id="getpassword"><a href=""
								stats="home_findpassword">忘记密码？</a></span>
						</dd>
					</dl>
					<dl id="codeimg" class="codeimg hidden">
						<dt></dt>
						<dd>
							<img id="verifyPic_login" src="./servlet/CheckCode"> <a
								href="javascript:refreshCode_login();" id="swith">Switch</a>

						</dd>
					</dl>
					<dl id="code" class="code hidden">
						<dt>
							<label for="code"></label>
						</dt>
						<dd>
							<input id="icode" type="text" name="icode" class="input-text"
								tabindex="3" placeholder="Code" autocomplete="off">
							<!--  <label class="codetip" id="codeTip" for="icode">Code</label> -->
						</dd>
					</dl>
					<dl class="bottom">
					</dl>
					<input type="hidden" name="language" value="Zh" /> <input
						type="hidden" name="origURL" value="" /> <input type="hidden"
						name="key_id" value="1" /> <input type="hidden"
						name="captcha_type" id="captcha_type" value="web_login" /> <input
						type="hidden" name="method" id="method" value="" /> <input
						type="submit" id="login" class="input-submit login-btn"
						stats="loginPage_login_button" value="登录" tabindex="5" /> <input
						type="button" id="register" class="input-submit regist-btn"
						stats="loginPage_regist_button" value="注册" tabindex="6"
						onclick="window.location = '/TicketSchedule/Zh/register.jsp'" />
				</form>

			</div>
		</div>
	</div>
	<div>微信扫一扫，登录手机版<br><img src="../m/img/mQR.png"/></div>
	
	<input type="text" class="hidden" value="1"></input>
		<div id="baidu_tongji" style="display: none">
		<script type="text/javascript">
			var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://"
					: " http://");
			document
					.write(unescape("%3Cscript src='"
							+ _bdhmProtocol
							+ "hm.baidu.com/h.js%3F04d65d39238bfa4301b173d21ddcfeb7' type='text/javascript'%3E%3C/script%3E"));
		</script>
	</div>
</body>
</html>