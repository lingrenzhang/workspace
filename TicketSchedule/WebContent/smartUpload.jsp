<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspsmart.upload.SmartUpload"%>
<%
    SmartUpload su = new SmartUpload();
    su.initialize(pageContext);
    su.setMaxFileSize(10000000);
    su.setTotalMaxFileSize(20000000);
    su.setAllowedFilesList("doc,txt,jpg,rar,mid,waw,mp3,gif,bmp");
    boolean sign = true;
    try {
    su.setDeniedFilesList("exe,bat,jsp,htm,html");
    su.upload();
    su.save("c:\\");
    } catch (Exception e) {
    e.printStackTrace();
    sign = false;
    }
    if(sign==true)
    {
    out.println("<script>parent.callback('upload file success')</script>");
    }else
    {
    out.println("<script>parent.callback('upload file error')</script>");
    }
%>

   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>



</body>
</html>