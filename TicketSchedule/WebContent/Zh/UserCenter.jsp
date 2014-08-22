<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="com.hitchride.User"%>
<%@ page import="com.hitchride.Message"%>
<%@ page import="java.util.Iterator"%>
<%
    User user = (User) request.getSession().getAttribute("user");
    if (user==null)
    {
    	 user = new User(); //Prevent null pointer error.
		 user.set_authLevel(1);
		 user.set_name("na");
		 user.set_emailAddress("na");
		 user.set_avatarID("default.jpg");
		 user.set_userLevel(0);
    }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>用户中心</title>
	<!-- Bootstrap -->
	<!--  
	<link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
    -->
    <link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
    <link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
    <link href="/TicketSchedule/CSS/usercenter.css" type="text/css" rel="stylesheet">

	<link rel="shortcut icon" href="/TicketSchedule/favicon.ico" type="image/x-icon" /> 

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
        <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
    <script src="/TicketSchedule/JS/site.js"></script>
    
<script language="JavaScript"> //disable browser backward
	javascript:window.history.forward(1);
</script>
 <script>
    if ("<%=user.get_name()%>"=="na")
    {
    	window.location.href="/TicketSchedule/Zh/Login.jsp";
   	}
    
 	var newmessage = <%=user.numofnewMessage%>;
 	var content = '<%=request.getParameter("content")%>';
 	var msg = '<%=request.getParameter("msg")%>';
 	var uid = '<%=user.get_uid()%>';
 	function getTopics()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class='nav nav-pills nav-justified nav-stacked' id='user_nav'>"
  		 	+	 "<li class='active'><a href='javascript:getTopics()'>我的行程</a></li>"
	  		+	 "<li><a href='javascript:getMessages()'>"
	  		+    	"<span class='badge pull-right'>"+newmessage
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li><a href='javascript:getHistory()'>历史记录</a></li>"
			+	 "<li><a href='javascript:getProfile()'>个人信息</a></li>"
			+	 "<li><a href='/TicketSchedule/servlet/Logout?uid='"+uid+"'>退出系统</a></li>"
			+"</ul>";
			
		var content = getJson("/TicketSchedule/servlet/UserCenter?content=topics&language=Zh");
		document.getElementById("innerContent").innerHTML = content;
 	}
 	
 	
 	function getMessages()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class='nav nav-pills nav-justified nav-stacked' id='user_nav'>"
  		 	+	 "<li><a href='javascript:getTopics()'>我的行程</a></li>"
	  		+	 "<li class='active'><a href='javascript:getMessages()'>"
	  		+    	"<span class='badge pull-right active'>"+"0"
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li><a href='javascript:getHistory()'>历史记录</a></li>"
			+	 "<li><a href='javascript:getProfile()'>个人信息</a></li>"
			+	 "<li><a href='/TicketSchedule/servlet/Logout?uid='"+uid+"'>退出系统</a></li>"
			+"</ul>";
 		var content = getJson("/TicketSchedule/servlet/UserCenter?content=messages&language=Zh");		
	    
		document.getElementById("innerContent").innerHTML = content;
		newmessage = 0;
		
 	}
 	function getHistory()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class='nav nav-pills nav-justified nav-stacked' id='user_nav'>"
  		 	+	 "<li><a href='javascript:getTopics()'>我的行程</a></li>"
	  		+	 "<li><a href='javascript:getMessages()'>"
	  		+    	"<span class='badge pull-right'>"+newmessage
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li class='active'><a href='javascript:getHistory()'>历史记录</a></li>"
			+	 "<li><a href='javascript:getProfile()'>个人信息</a></li>"
			+	 "<li><a href='/TicketSchedule/servlet/Logout?uid='"+uid+"'>退出系统</a></li>"
			+"</ul>";
 		alert("功能开发中...");
 	}
 	function getProfile()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class='nav nav-pills nav-justified nav-stacked' id='user_nav'>"
  		 	+	 "<li><a href='javascript:getTopics()'>我的行程</a></li>"
	  		+	 "<li><a href='javascript:getMessages()'>"
	  		+    	"<span class='badge pull-right'>"+newmessage
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li><a href='javascript:getHistory()'>历史记录</a></li>"
			+	 "<li class='active'><a href='javascript:getProfile()'>个人信息</a></li>"
			+	 "<li><a href='/TicketSchedule/servlet/Logout?uid='"+uid+"'>退出系统</a></li>"
			+"</ul>";
 		var content = getJson("/TicketSchedule/servlet/UserCenter?content=profile&language=Zh");		
		document.getElementById("innerContent").innerHTML = content;
 	}
 	
 	function init(){
	 	if (content!="" || content!=null)
	 	{
	 		if (content=='profile')
	 		{
	 			getProfile();
	 			if (msg!="" || msg!=null)
	 			{
	 				if (msg=='wrongPWD')
	 				{
	 					alert("密码错误！");
	 				}
	 			}
	 			return;
	 		}
	 		getTopics();
	 	}
 	};
</script>   
<script>
function validateNewpwd()
{
	var oldpwd= document.getElementById("oldpwd").value;
	var newpwd= document.getElementById("newpwd").value;
	var newpwd2= document.getElementById("newpwd2").value;
	if (newpwd!=newpwd2)
	{
		alert("两次输入新密码不匹配");
		return false;
	}
	else if (newpwd!="")
	{
		var regu="^\\w{6,12}$";
		var re=new RegExp(regu);
		if(!re.test(newpwd)){
			alert("密码为6-12位数字字母");
			return false;
		}
		if(oldpwd=="")
		{
			alert("请输入旧密码");
			return false;
		}
	}
	return true;
}

</script>
    
</head>
<body onload="init()">
	
<div id="UserCenter">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
		<div class="header_nav_wrapper">
			<div class="navbar navbar-default" role="navigation">
			  <div class="navbar navbar-default">
			    <ul class="nav navbar-nav">
			      <li class="active"><a href="#">用户中心</a></li>
			      <li><a href="/TicketSchedule/Zh/ManageRide.jsp">管理行程</a></li> 
			  	  <li><a href="/TicketSchedule/Zh/SearchRide.jsp">上下班拼车</a></li>
			      <!--<li><a href="javascript:inbuilding()">管理行程</a></li> 
			      <li><a href="javascript:inbuilding()">上下班拼车</a></li>-->
			      <li><a href="/TicketSchedule/Zh/SearchTransientRide.jsp">临时拼车</a></li>
			    </ul>
			  </div>
			</div>
		</div>
		<div class="header_user_wrapper">
			
		</div>
	</div>
	<div id="content_wrapper">
		<div class="user_wrapper">
		   <div class="user_info" id="from">
			<div class="userpic">
				<div class="username"><%=(user.get_name()==null)?"guest":user.get_name() %></div>
				<img src=<%= user.get_head_portrait_path() %> alt="Profile Picture"></img>
			</div>
				<div class = "user_nav_wrapper">
				  <ul class="nav nav-pills nav-justified nav-stacked" id="user_nav">
 				 </ul>
				</div>
			</div>
		</div>
		<div class="inner_content_wrapper" id="innerContent">
		</div>
	</div>
</div>
<!--  
	<div id="baidu_tongji" style="display: none">
		<script type="text/javascript">
			var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
			document.write(unescape("%3Cscript src='"+ _bdhmProtocol
				+ "hm.baidu.com/h.js%3F04d65d39238bfa4301b173d21ddcfeb7' type='text/javascript'%3E%3C/script%3E"));
		</script>
	</div>
-->
</body>
</html>