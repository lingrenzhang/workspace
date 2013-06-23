<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="/TicketSchedule/servlet/Welcome" method="post">
    <div class="box">
		<div class="text_input">
		<label class="pin start" for="search_s"></label>
		<input id="splash_s" class="input_text ac_input clickaway" type="text" value="From" autocomplete="off" name="s" alt="search_start" placeholder="From" style="color: rgb(153, 153, 153);">
		</div>
		<div class="text_input">
		<label class="pin end" for="search_e"></label>
		<input id="splash_e" class="input_text ac_input clickaway" type="text" value="To" autocomplete="off" name="e" alt="search_end" placeholder="To">
		</div>
		<button class="clickaway_confirm" type="submit">Search</button>
	</div>
</form>
</body>
</html>