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
	TransientRide tranRide = (TransientRide) request.getSession().getAttribute("tranRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/transienttopic.css" type="text/css" rel="stylesheet">


<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=Mto5Y3Pq2fgwkY2Kt9n60bWl"></script>

<script type="text/javascript">
//New Topic Related
var ttopic;
var tride;

//Display Related
var torigLat, torigLng, tdestLat, tdestLng;
var tomarker,tdmarker;
var map;
var basicbounds;

var images = new BMap.Icon
("/TicketSchedule/Picture/pin_start.png",
new BMap.Size(71, 71),{
anchor: new BMap.Size(8, 16),
//imageOffset: new google.maps.Point(0, 0)
});
var imagee = new BMap.Icon
("/TicketSchedule/Picture/pin_end.png",
new BMap.Size(71, 71),{
anchor: new BMap.Size(8, 16),
//imageOffset: new google.maps.Point(0, 0)
});

$(document).ready(function(){
	
	loadContent();
	
	var omarker;
	var dmarker;

	//初始化地图
	var geol;		
	var nowLat=31.270998;
	var nowLng=121.543146;
	try {
		if (typeof(navigator.geolocation) == 'undefined') {
			geol = google.gears.factory.create('beta.geolocation');
	    } else {
	    	geol = navigator.geolocation;
	    }
	} catch (error) {
			//alert(error.message);
	}
	if (geol) {
		geol.getCurrentPosition(function(position) {
		nowLat = position.coords.latitude;
		nowLng = position.coords.longitude;
		}, function(error) {
			switch(error.code){
			case error.TIMEOUT :
				//alert("连接超时，请重试");
				break;
			case error.PERMISSION_DENIED :
				//alert("您拒绝了使用位置共享服务，查询已取消");
				break;
			case error.POSITION_UNAVAILABLE : 
				//alert("非常抱歉，我们暂时无法通过浏览器获取您的位置信息");
				break;
			}
		}, {timeout:2000});	//设置2秒超时
	}

	map = new BMap.Map("map-canvas");
	var point = new BMap.Point(nowLng,nowLat);
	map.addControl(new BMap.NavigationControl());    
	map.addControl(new BMap.ScaleControl());
	map.centerAndZoom(point,15);
	
	origLat=tride.origLoc._lat;
	origLng=tride.origLoc._lon;
	origAddr= tride.origLoc._formattedAddr;
	destLat=tride.destLoc._lat;
	destLng=tride.destLoc._lon;
	destAddr= tride.destLoc._formattedAddr;
	
	basicbounds = new BMap.Bounds();
	
	if (origLat!="" && origLng!="" && origLat!="" &&origLng!="")
	{
		var oLatlng = new BMap.Point(origLng,origLat);
		var dLatlng = new BMap.Point(destLng,destLat);
		
		omarker = new BMap.Marker(oLatlng,{icon: images});
		map.addOverlay(omarker);
		   
		dmarker = new BMap.Marker(dLatlng,{icon: imagee});
		map.addOverlay(dmarker);
		
		basicbounds.extend(oLatlng);
		basicbounds.extend(dLatlng);
		refitb(basicbounds);
	}
	loadRide();
	
});

		
window.onscroll = function(){
    var t = document.documentElement.scrollTop || document.body.scrollTop; 
    
    if( t >= 180 ) {
    	var div = document.getElementById("floatwrap");
    	div.className="floatwrap fixed";
    } else {
    	var div = document.getElementById("floatwrap");
    	div.className="floatwrap";
    }
};

function refitb(bounds)
{
	 var range = Math.max(bounds.toSpan().lat,bounds.toSpan().lng);
	 var zoomNum = Math.floor(9-Math.log(range)/Math.log(2));
	 map.setCenter(bounds.getCenter());
	 map.setZoom(zoomNum);
}
</script>
<script>
function loadContent()
{
	var trid = getURLPara("trId");
	var queryURL = "/TicketSchedule/servlet/TransientRideCenter?trId="+trid;
	
	var result = JSON.parse(getJson(queryURL));
	tride = result.tride;
	ttopic = result.ttopic;
}

function loadRide()
{
	var topicstring="";
	topicstring = topicstring + "<div class=\"entry\" origLat="+tride.origLoc._lat+" ";
	topicstring = topicstring + "origLng=" +  tride.origLoc._lon+" ";
	topicstring = topicstring + "destLat=" +  tride.destLoc._lat+" ";
	topicstring = topicstring + "destLng=" +  tride.destLoc._lon+" "+">";
	

	
	topicstring = topicstring + "<div class=\"userpic\">";
	topicstring = topicstring + "<div class=\"username\">"+tride.owner._givenname+"</div>";
	topicstring = topicstring + "<img src= \"/TicketSchedule/UserProfile/"+tride.owner._avatarID+"\" alt=\"Profile Picture\"></img>";
	topicstring = topicstring + "<span class=\"passenger\"></span></div>";
	topicstring = topicstring + "<div class=\"inner_content\"><h5>";
	//topicstring = topicstring + "<span class=\"inner\">"+"出发地："+trInfo.origLoc._addr+"<br>";
	//topicstring = topicstring + "目的地："+trInfo.destLoc._addr+"</span></h4>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_start.png\"/>"+"  出发地："+tride.origLoc._addr+"<br>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_end.png\"/>"+"  目的地："+tride.destLoc._addr+"<br>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/clock_small.jpg\"/>"+" 出发时间："+tride.rideTime+"<br>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/mobileicon.jpg\"/>"+" 联系方式："+tride.owner._cellphone+"<br>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_end.png\"/>"+"途经： "+"<br>";
	topicstring = topicstring + "<div id=middlepoint><input id=addmiddle></input></div>";
	topicstring = topicstring + "</h5></div>";
	
	if (tride.userType)
	{
		topicstring = topicstring + "<div class=\"passenger_box\"><p>";
		topicstring = topicstring +"<span><img src='/TicketSchedule/Picture/nocar.jpg'/><br><span>";
		topicstring = topicstring + tride.owner._givenname+"<br> <strong>不提供车</strong></p></div>";
	}
	else{
		topicstring = topicstring + "<div class=\"price_box\"><div class=\"seats\">";
		topicstring = topicstring +"<img src='/TicketSchedule/Picture/seats.jpg'/><span class='count'>"+tride.totalSeats+"</span></div>";
		topicstring = topicstring +"<p><b>"+tride.price + "</b> / 座</p></div>";
	}
	topicstring =topicstring+"</div>";
	
	document.getElementById("topicbody").innerHTML=topicstring;
}
</script>


<title>临时拼车</title>
</head>

<body>
<div id="header_wrap">
	<div id="logo_wrap">
		<div id="logo"></div>
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
			<div id="topicinfo">
				<div id="topichead">行程信息</div>
				<div id="topicbody">
				
				

				</div>
				<div id="participants">
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



</body>
</html>