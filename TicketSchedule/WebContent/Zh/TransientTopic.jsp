<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hitchride.standardClass.User" %>
<%@ page import="com.hitchride.standardClass.Topic" %>
<%@ page import="com.hitchride.standardClass.TransientRide" %>
<% 
	String IsLogin =(String) request.getSession().getAttribute("IsLogin");
	User user= (User) request.getSession().getAttribute("user");
	Boolean isOwnerMode = (Boolean) request.getAttribute("isOwnerMode");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">


<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=Mto5Y3Pq2fgwkY2Kt9n60bWl"></script>

<script type="text/javascript">
</script>
<title>临时拼车</title>
</head>


<div id="header_wrap">
	<div id="logo_wrap">
		<div id="logo">
		</div>
	</div>
	<div class="header_nav_wrapper">
		<div class="navbar navbar-default" role="navigation">
			<div class="navbar navbar-default">
				<ul class="nav navbar-nav">
				  <li><a href="/TicketSchedule/Zh/UserCenter.jsp">用户中心</a></li>
				  <li><a href="/TicketSchedule/Zh/ManageRide.jsp">行程管理</a></li>
				  <li><a href="/TicketSchedule/Zh/SearchRide.jsp">上下班拼车</a></li>
			      <li class="active"><a href="#">临时拼车</a></li>
			    </ul>
			 </div>
		</div>
	</div>
	<div id="user_info_wrap">
			<%=user.getUserWrapper() %>
	</div>
</div>

<div id="content_wrapper">
	<div id="content_container">
		<div id="content">
			<div id="results">
				<div class="ride_list">
					<h3 id="headline" class="headline first"></h3>
					<div id="ride_content">
					</div>
				</div>
			</div>
			
			<div id="info">
				<div class="floatable">
					<div class="floatwrap" id="floatwrap">
				 		<div id="map-canvas">
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
<div id="footer">
</div>

<body>

</body>
</html>