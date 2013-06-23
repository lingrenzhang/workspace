<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function clickaway(str){
	if (str=="username")
	{
		document.getElementById("name").value="";
	}
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show User</title>
</head>
<body>
<form action="/TicketSchedule/servlet/ShowUser" method="post">
    <div class="box">
		<div class="text_input">
		<input id="name" class="input_text_clickaway" type="text" value="username"  name="name" alt="search_start" style="color: rgb(153, 153, 153);">
		</div>
		<button class="clickaway_confirm" type="submit">ShowByName</button>
	</div>
</form>
</body>
</html>