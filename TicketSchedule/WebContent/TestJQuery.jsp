<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="./JS/jquery-1.10.1.js">
</script>
<script>
$(document).ready(function(){
  $("p").click(function(){
    $(this).hide();
  });
});
</script>
<title>Insert title here</title>
</head>
<body>
<p>Will disappear 1</p>
<p>Will disappear 2</p>
<p>Will disappear 3</p>
</body>
</html>