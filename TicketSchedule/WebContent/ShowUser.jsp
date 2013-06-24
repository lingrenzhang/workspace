<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<script>
function clickaway(str){
	if (str=="username")
	{
		var obj=document.getElementById("name");
		obj.value="";
		obj.style.color="#000000";
	}
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show User</title>
</head>

<%@page import="java.sql.*"%>
<%@page import="java.util.*" %>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires",0);
    ResultSet rs = (ResultSet)request.getAttribute("results");
%>

<body>
<form action="/TicketSchedule/servlet/ShowUser" method="post">
    <div class="box">
		<div class="text_input">
		<input id="name" class="input_text_clickaway" type="text" value="username"  name="name" alt="search_start" style="color: rgb(153, 153, 153)" onclick="clickaway(this.value)">
		</div>
		<button class="clickaway_confirm" type="submit">ShowByName</button>
	</div>
</form>
<% if (rs!=null) { %>
	<table>
		<tr>
			<td align="center">UserName</td>
			<td align="center">Password</td>
			<td align="center">EmailAddress</td>		
		</tr>

 	 <%while (rs.next()) {%>
		<tr>
			<td align="center"><%=rs.getString(1) %></td>
		    <td align="center"><%=rs.getString(2) %></td>
		    <td align="center"><%=rs.getString(3) %></td>
		</tr>
 	 <%} %>
</table>
<%} %>


</body>
</html>