<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="com.hitchride.standardClass.User"%>
<%@ page import="com.hitchride.standardClass.Message"%>
<%@ page import="java.util.Iterator"%>
<%
    User user = (User) request.getSession().getAttribute("user");
    if (user==null)
    {
    	user= new User();
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
 <script>
 	var newmessage = <%=user.numofnewMessage%>;
 	function loadContent(url)
 	{
 		var xmlhttp;
 	    if (window.XMLHttpRequest)
 	    {// code for IE7+, Firefox, Chrome, Opera, Safari
 	      	xmlhttp=new XMLHttpRequest();
 	    }
 	    else
 	    {// code for IE6, IE5
 	      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
 	    }
 		xmlhttp.open("GET",url,false);
 		xmlhttp.send();
 		return xmlhttp.responseText;
 	}
 	 	
 	
 	function getTopics()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li class=\"active\"><a href=\"javascript:getTopics()\">我的行程</a></li>"
	  		+	 "<li><a href=\"javascript:getMessages()\">"
	  		+    	"<span class=\"badge pull-right\">"+newmessage
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li><a href=\"javascript:getHistory()\">历史记录</a></li>"
			+	 "<li><a href=\"javascript:getProfile()\">个人信息</a></li>"
			+"</ul>";
			
		var content = loadContent("/TicketSchedule/servlet/UserCenter?content=topics&language=Zh");
		document.getElementById("innerContent").innerHTML = content;
 	}
 	
 	
 	function getMessages()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li><a href=\"javascript:getTopics()\">我的行程</a></li>"
	  		+	 "<li class=\"active\"><a href=\"javascript:getMessages()\">"
	  		+    	"<span class=\"badge pull-right active\">"+"0"
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li><a href=\"javascript:getHistory()\">历史记录</a></li>"
			+	 "<li><a href=\"javascript:getProfile()\">个人信息</a></li>"
			+"</ul>";
 		var content = loadContent("/TicketSchedule/servlet/UserCenter?content=messages&language=Zh");		
	    
		document.getElementById("innerContent").innerHTML = content;
		newmessage = 0;
		
 	}
 	function getHistory()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li><a href=\"javascript:getTopics()\">我的行程</a></li>"
	  		+	 "<li><a href=\"javascript:getMessages()\">"
	  		+    	"<span class=\"badge pull-right\">"+newmessage
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li class=\"active\"><a href=\"javascript:getHistory()\">历史记录</a></li>"
			+	 "<li><a href=\"javascript:getProfile()\">个人信息</a></li>"
			+"</ul>";
 		alert(3);
 	}
 	function getProfile()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li><a href=\"javascript:getTopics()\">我的行程</a></li>"
	  		+	 "<li><a href=\"javascript:getMessages()\">"
	  		+    	"<span class=\"badge pull-right\">"+newmessage
	  		+		"</span>"
	  		+    "我的消息</a></li>"
	  		+	 "<li><a href=\"javascript:getHistory()\">历史记录</a></li>"
			+	 "<li class=\"active\"><a href=\"javascript:getProfile()\">个人信息</a></li>"
			+"</ul>";
 		var content = loadContent("/TicketSchedule/servlet/UserCenter?content=profile&language=Zh");		
		document.getElementById("innerContent").innerHTML = content;
 	}
 </script>   
    
    
</head>
<body onload="getTopics()">
	
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
			      <li><a href="/TicketSchedule/Zh/ManageRide.jsp">行程管理</a></li>
			      <li><a href="/TicketSchedule/Zh/SearchRide.jsp">上下班拼车</a></li>
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
				<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
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
</body>
</html>