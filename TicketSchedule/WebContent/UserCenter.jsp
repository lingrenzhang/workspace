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
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>User Center</title>
	<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
	<link href="/TicketSchedule/CSS/usercenter.css" type="text/css" rel="stylesheet">
	
	<!-- Bootstrap -->
	<!--  
	<link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
    -->
    <link rel="stylesheet" href="./bootstrap/css/bootstrap.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
        <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="./bootstrap/js/bootstrap.js"></script>
 <script>
 	function getTopics()
 	{

 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li class=\"active\"><a href=\"javascript:getTopics()\">RideTopics</a></li>"
	  		+	 "<li><a href=\"javascript:getMessages()\">Messages</a></li>"
	  		+	 "<li><a href=\"javascript:getHistory()\">History</a></li>"
			+	 "<li><a href=\"javascript:getProfile()\">Profile</a></li>"
			+"</ul>";
			
		var content1 = "";
		content1=content1 
		 + "<div class=\"panel-heading\">Topic you own</div>"
		 + "<div class=\"panel-body\">"
		 +   "<div class=\"ride_wrapper\">"
		 +   	"<div class = \"geo\">"
		 +   		"From place:abcd<br>"
		 +   		"To place: efgh"
		 +   	"</div>"
		 +   	"<div class = \"schedule\">"
		 +   	"	Date: Mon, Thu<br>"
		 +   	"	Time: 10:00-11:00 18:00-19:00"
		 +   	"</div>"
		 +   	"<div class = \"people\">"
		 +			
		 +   	"</div>"
		 +   "</div>"
		 + "</div>";
	 	document.getElementById("panel-1").innerHTML = content1;
		
	 	var content2="";
	 	content2 = content2 +
		  "<div class=\"panel-heading\">Topic you participate"
		  +"</div>"
		  +"<div class=\"panel-body\">"
		  +"</div>";
		document.getElementById("panel-2").innerHTML = content2;
		
		var content3="";
		content3=content3 +
		  "<div class=\"panel-heading\">Your free rides"
		  +"</div>"
		  +"<div class=\"panel-body\">"
		  +"</div>";
		document.getElementById("panel-3").innerHTML = content3;

 	}
 	function getMessages()
 	{

 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li><a href=\"javascript:getTopics()\">RideTopics</a></li>"
	  		+	 "<li class=\"active\"><a href=\"javascript:getMessages()\">Messages</a></li>"
	  		+	 "<li><a href=\"javascript:getHistory()\">History</a></li>"
			+	 "<li><a href=\"javascript:getProfile()\">Profile</a></li>"
			+"</ul>";
 		var string = "";
 		string = "<div class=\"panel-heading\">Your message</div>"
			 + "<div class=\"panel-body\">";
	    string = string + "123";
	    string = string+"</div></div>";
		document.getElementById("panel-1").innerHTML = string;
		document.getElementById("panel-2").innerHTML = "";
		document.getElementById("panel-3").innerHTML = "";
		
 	}
 	function getHistory()
 	{

 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li><a href=\"javascript:getTopics()\">RideTopics</a></li>"
	  		+	 "<li><a href=\"javascript:getMessages()\">Messages</a></li>"
	  		+	 "<li class=\"active\"><a href=\"javascript:getHistory()\">History</a></li>"
			+	 "<li><a href=\"javascript:getProfile()\">Profile</a></li>"
			+"</ul>";
 		alert(3);
 	}
 	function getProfile()
 	{
 		document.getElementById("user_nav").innerHTML = 	
 			"<ul class=\"nav nav-pills nav-justified nav-stacked\" id=\"user_nav\">"
  		 	+	 "<li><a href=\"javascript:getTopics()\">RideTopics</a></li>"
	  		+	 "<li><a href=\"javascript:getMessages()\">Messages</a></li>"
	  		+	 "<li><a href=\"javascript:getHistory()\">History</a></li>"
			+	 "<li class=\"active\"><a href=\"javascript:getProfile()\">Profile</a></li>"
			+"</ul>";
 		alert(4);
 	}
 </script>   
    
    
</head>
<body>
	
<div id="UserCenter">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
		<div class="header_nav_wrapper">
			<nav class="navbar navbar-default" role="navigation">
			  <div class="navbar navbar-default">
			    <ul class="nav navbar-nav">
			      <li class="active"><a href="#">UserCenter</a></li>
			      <li><a href="#">PostRide</a></li>
			      <li><a href="#">RideCenter</a></li>
			    </ul>
			  </div>
			</nav>
		</div>
		<div class="header_user_wrapper">
			
		</div>
	</div>
	<div id="content_wrapper">
		<div class="user_wrapper">
		   <div class="user_info" id="from">
			<div class="userpic">
				<div class="username"><%=user.get_name() %></div>
				<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
			</div>
				<div class = "user_nav_wrapper">
				  <ul class="nav nav-pills nav-justified nav-stacked" id="user_nav">
 			  		 <li class="active"><a href="javascript:getTopics()">RideTopics</a></li>
 			  		 <li><a href="javascript:getMessages()">Messages</a></li>
 			  		 <li><a href="javascript:getHistory()">History</a></li>
 					 <li><a href="javascript:getProfile()">Profile</a></li>
				 </ul>
				</div>
			</div>
		</div>
		<div class="inter_content_wrapper">
		    <div class="panel panel-default" id="panel-1">
			  <div class="panel-heading">Topic you own</div>
			  <div class="panel-body">
			    <div class="ride_wrapper">
			    	<div class = "geo">
			    		From place:abcd<br>
			    		To place: efgh
			    	</div>
			    	<div class = "schedule">
			    		Date: Mon, Thu<br>
			    		Time: 10:00-11:00 18:00-19:00
			    	</div>
			    	<div class = "people">
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
			    		<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
			    		<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
			    		<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
			    	</div>
			    </div>
			  </div>
			</div>
			
			<div class="panel panel-default" id="panel-2">
			  <div class="panel-heading">Topic you participate
			  </div>
			  <div class="panel-body">

			  </div>
			</div>
			
			<div class="panel panel-default" id="panel-3">
			  <div class="panel-heading">Your free rides
			  </div>
			  <div class="panel-body">
			  </div>
			</div>
		</div>
	</div>
</div>
</body>
</html>