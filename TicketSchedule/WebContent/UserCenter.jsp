<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="com.hitchride.standardClass.User"%>
<%
    User user = (User) request.getSession().getAttribute("user");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Center</title>

<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">

</head>
<body>
<div id="UserCenter">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
	</div>
	
	<div id="content_wrapper">
		<div class="user_wrapper">
		   <div class="user_info" id="from">
				<div class="userpic">
				
						<div class="username"><%=user.get_name() %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
				</div>
				<div class="user_operation">
					<button type=button onclick="join()">Join</button>
				</div>
				
			</div>
			
		</div>
</div>
</body>
</html>