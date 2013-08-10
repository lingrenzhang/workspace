<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String from = (String) request.getSession().getAttribute("userName");
    String to = (String) request.getAttribute("to");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MessageBox</title>

<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/messagebox.css" type="text/css" rel="stylesheet">

</head>
<body>
<div id="MessageBox">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
	</div>
	<div id="content_wrapper">
	<form action="/TicketSchedule/servlet/MessageBox" method="Post" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
		<div class="user_wrapper">
			<div class="user_info" id="from">
				<div class="userpic">
						<div class="username"><%=from %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+from+".jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
				</div>
			</div>
			<div class="user_info" id="to">
				<div class="userpic">
					<div class="username"><%=to %></div>
					<img src=<%="/TicketSchedule/UserProfile/"+to+".jpg" %> alt="Profile Picture"></img>
					<span class="passenger"></span>
				</div>
			</div>
			<div>
				<button type="submit" id="sendbutton">Send</button>
			</div>
		</div>
		<div class="biginput" id="message_wrapper">
			<textarea id="messageText" cols="30" rows="3" name="notes" class="clickaway badWord" placeholder="Your Message..." style="color: rgb(153, 153, 153);"></textarea>
		</div>
		<input class="hidden" name="from" value=<%=from%>></input>
		<input class="hidden" name="to" value=<%=to%>></input>
	</form>
	</div>
	
</div>
</body>
</html>