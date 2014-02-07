<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="com.hitchride.standardClass.MessageInfo"%>
<%
	String from = (String) request.getSession().getAttribute("userName");
    String to = (String) request.getAttribute("to");
    MessageInfo messInfo = (MessageInfo) request.getAttribute("messageInfo");
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
		<div class="entry">
						<div class="passenger_box">
								<p>
									<span class="icon"></span><%=to %> is a 
									<strong>passenger</strong>
								</p>
						</div>
						<div class="userpic">
								<div class="username"><%=messInfo.owner %></div>
								<img src=<%="/TicketSchedule/UserProfile/"+messInfo.owner+".jpg" %> alt="Profile Picture"></img>
								<span class="passenger"></span>
						</div>
							<div class="inner_content">
								<h3>
									<span class="inner"><%=messInfo.origCity %>
										<span class="trip_type round_trip"></span>
										<%=messInfo.destCity %>
									</span>
								</h3>
								<h4>
								From: <%=messInfo.origAddr+", "+messInfo.origCity%>
								To: <%=messInfo.destAddr+ ", "+messInfo.destCity%>
								</h4>
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