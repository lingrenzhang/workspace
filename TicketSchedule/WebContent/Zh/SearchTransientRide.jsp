<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hitchride.User" %>
<%@ page import="com.hitchride.CommuteTopic" %>
<%@ page import="com.hitchride.TransientRide" %>
<%
	boolean commute = true;
%>
<% 
	User user= (User) request.getSession().getAttribute("user");
    String IsLogin="true";
	if (user == null)
	{
		 user = new User();
		 user.set_authLevel(1);
		 user.set_name("guest");
		 user.set_emailAddress("guest");
		 user.set_avatarID("default.jpg");
		 user.set_userLevel(0);
		 IsLogin="false";
	}
	TransientRide tranRide = (TransientRide) request.getSession().getAttribute("tranRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/searchtransientride.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<link rel="shortcut icon" href="/TicketSchedule/favicon.ico" type="image/x-icon" /> 

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/jquery.blockUI.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/JS/calandar.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<!--  
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
-->
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=Mto5Y3Pq2fgwkY2Kt9n60bWl"></script>
<script type="text/javascript">
var isLogin=<%=IsLogin%>;
//New Topic Related
var origLat,origLng,destLat,destLng;
var origAddr,destAddr;
var distance,duration;
var date;
var userType=true;


//Display Related
var torigLat, torigLng, tdestLat, tdestLng;
var tomarker,tdmarker;
var map;
var basicbounds;

var images = new BMap.Icon
("/TicketSchedule/Picture/pin_start.png",
new BMap.Size(71, 71),{
anchor: new BMap.Size(8, 16),
});
var imagee = new BMap.Icon
("/TicketSchedule/Picture/pin_end.png",
new BMap.Size(71, 71),{
anchor: new BMap.Size(8, 16),
});

$(document).ready(function(){

	initCalandar("ui-datepicker-div","search_date","map-canvas");
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	date=document.getElementById("search_date").value;
	document.getElementById("headline").innerHTML="今日出发：<span>"+ new Date().toDateString() +"</span>";
	
	//Search box realted
	var searchBoxO;
	var searchBoxD;
	var orig;
	var dest;
	var omarker;
	var dmarker;

	//初始化地图
	var geol;		
	//var nowLat=31.270998;
	//var nowLng=121.543146;
	var nowLat=31.4656;
	var nowLng=121.1460;
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
	
	origLat="<%=tranRide==null?"":tranRide.origLoc.get_lat()%>";
	origLng="<%=tranRide==null?"":tranRide.origLoc.get_lon()%>";
	origAddr= "<%=(tranRide ==null) ? "" : tranRide.origLoc.get_formatedAddr()%>";
	destLat="<%=tranRide==null?"":tranRide.destLoc.get_lat()%>";
	destLng="<%=tranRide==null?"":tranRide.destLoc.get_lon()%>";
	destAddr= "<%=(tranRide ==null) ? "" : tranRide.destLoc.get_formatedAddr()%>";
	document.getElementById("search_s").setAttribute("value",origAddr);
	document.getElementById("search_e").setAttribute("value",destAddr);
	
	if (origAddr!="")
	{
		document.getElementById("search_s").setAttribute("placeholder",origAddr);
	}
	if (destAddr!="")
	{
		document.getElementById("search_e").setAttribute("placeholder",destAddr);
	}
	
	basicbounds = new BMap.Bounds();
	
	if (origLat!="" && origLng!="" && destLat!="" &&destLng!="")
	{
		var oLatlng = new BMap.Point(origLng,origLat);
		var dLatlng = new BMap.Point(destLng,destLat);
		
		omarker = new BMap.Marker(oLatlng);
		map.addOverlay(omarker);
		   
		dmarker = new BMap.Marker(dLatlng);
		map.addOverlay(dmarker);
		
		document.getElementById("origLat").value=origLat;
		document.getElementById("origLng").value=origLng;
		document.getElementById("destLat").value=destLat;
		document.getElementById("destLng").value=destLng;
		
		basicbounds.extend(oLatlng);
		basicbounds.extend(dLatlng);
		refitb(basicbounds);
	}
	
	searchBoxO = new BMap.Autocomplete(
			{"input" : "search_s",
			 "location" : map});
	searchBoxD = new BMap.Autocomplete(
			{"input" : "search_e",
			 "location" : map});

	searchBoxO.addEventListener("onconfirm",function(e){
		var _value = e.item.value;
		myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
		function myFun(){
			if (omarker!=null)
			{
				map.removeOverlay(omarker);
			}
			point = new BMap.Point(local.getResults().getPoi(0).point.lng,local.getResults().getPoi(0).point.lat);
			omarker = new BMap.Marker(point,{icon: images});
			map.centerAndZoom(point,18);
			map.addOverlay(omarker);
			document.getElementById("origLat").value=point.lat;
			document.getElementById("origLng").value=point.lng;
			origLat=point.lat;
			origLng=point.lng;
			destLat=document.getElementById("destLat").value;
			destLng=document.getElementById("destLng").value;
			if (destLat !="" && destLng!="")
			{
				var oLatlng = new BMap.Point(origLng,origLat);
				var dLatlng = new BMap.Point(destLng,destLat);
				basicbounds= new BMap.Bounds(oLatlng,dLatlng);
			  	refitb(basicbounds);
			    calculateDistances();
			}
		}
		var local = new BMap.LocalSearch(map,{onSearchComplete : myFun});
		local.search(myValue);
	});
	
	searchBoxD.addEventListener("onconfirm",function(e){
		var _value = e.item.value;
		myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
		function myFun(){
			if (dmarker!=null)
			{
				map.removeOverlay(dmarker);
			}
			point = new BMap.Point(local.getResults().getPoi(0).point.lng,local.getResults().getPoi(0).point.lat);
			dmarker = new BMap.Marker(point,{icon: imagee});
			map.centerAndZoom(point,18);
			map.addOverlay(dmarker);
			document.getElementById("destLat").value=point.lat;
			document.getElementById("destLng").value=point.lng;
			destLat=point.lat;
			destLng=point.lng;
			origLat=document.getElementById("origLat").value;
			origLng=document.getElementById("origLng").value;
			if (origLat !="" && origLng!="")
			{
				var oLatlng = new BMap.Point(origLng,origLat);
				var dLatlng = new BMap.Point(destLng,destLat);
				basicbounds= new BMap.Bounds(oLatlng,dLatlng);
			 	refitb(basicbounds);
			    calculateDistances();
			}
		}
		var local = new BMap.LocalSearch(map,{onSearchComplete : myFun});
		local.search(myValue);
	});
	
	//Rough distance here
	function calculateDistances() {

		var dx, dy, dew;

		var DEF_PI = 3.14159265359; // PI
		var DEF_2PI= 6.28318530712; // 2*PI
		var DEF_PI180= 0.01745329252; // PI/180.0
		var DEF_R =6370693.5; // radius of earth

		ew1 = origLng* DEF_PI180;
		ns1 = origLat * DEF_PI180;
		ew2 = destLng * DEF_PI180;
		ns2 = destLat * DEF_PI180;

		dew = ew1 - ew2;

		if (dew > DEF_PI)
		{
			dew = DEF_2PI - dew;
		}
		else
		{
			if (dew < -DEF_PI)
			{
				dew = DEF_2PI + dew;
			}
		}
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
			
		distance = Math.floor(Math.sqrt(dx * dx + dy * dy)*1.1/1000);
		duration = distance/30; //Will use better algorithm if necessary
		
		document.getElementById("distance").setAttribute("value",distance);
		document.getElementById("duration").setAttribute("value",duration);
		   
		var price = distance;
		document.getElementById("price").setAttribute("value",price);
	}
	
	
	search();
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

function search()
{
	origAddr =document.getElementById("search_s").value;
	origLat = document.getElementById("origLat").value;
	origLng = document.getElementById("origLng").value;
	destAddr = document.getElementById("search_e").value;
	destLat = document.getElementById("destLat").value;
	destLng = document.getElementById("destLng").value;
	distance = document.getElementById("distance").value*1000;
	duration = document.getElementById("duration").value;
	date = document.getElementById("search_date").value;
	var queryURL = "/TicketSchedule/servlet/SearchTransientTopic";
	queryURL = queryURL+"?s="+origAddr+"&origLat="+origLat+"&origLng="+origLng;
	queryURL = queryURL+"&e="+destAddr+"&destLat="+destLat+"&destLng="+destLng;
	queryURL = queryURL+"&distance="+distance+"&duration"+duration;
	queryURL = queryURL+"&date="+date;
	document.getElementById("headline").innerHTML="<span>出发日："+date+"</span>";
    //var results = JSON.parse(getJson(queryURL));
    $.blockUI({ message: '<h1><img src="../Picture/busy1.gif" /></h1>' });
    $.get(queryURL,function(data,status){
    	$.unblockUI();
    	document.getElementById("searchResultMessage").innerHTML="<h2>检索结束</h2>";
    	results=JSON.parse(data);
    	listResults(results);
    	
    	$(".entry").hover(function(){
    		torigLat = $(this)[0].getAttribute("origLat");
    		torigLng = $(this)[0].getAttribute("origLng");
    		tdestLat = $(this)[0].getAttribute("destLat");
    		tdestLng = $(this)[0].getAttribute("destLng");
    		var rank = $(this)[0].getAttribute("rank");
    		var toLatlng = new BMap.Point(torigLng,torigLat);
    		var tdLatlng = new BMap.Point(tdestLng,tdestLat);

    		if (tomarker!=null)
    		{
    			map.removeOverlay(tomarker);
    		}
    		if (tdmarker!=null)
    		{
    			map.removeOverlay(tdmarker);
    		}
    		
    		tomarker = new BMap.Marker(toLatlng,{icon: images});
    		tdmarker = new BMap.Marker(tdLatlng,{icon: imagee});
    		
    	 	var bounds = new BMap.Bounds(basicbounds.getSouthWest(),basicbounds.getNorthEast());
    	    bounds.extend(toLatlng);
    		bounds.extend(tdLatlng);
    		refitb(bounds);
    		map.addOverlay(tomarker);
    		map.addOverlay(tdmarker);
    	});

    });
    
   document.getElementById("searchResultMessage").innerHTML="<h2>检索中</h2>";
}

function refitb(bounds)
{
	 var range = Math.max(bounds.toSpan().lat,bounds.toSpan().lng);
	 var zoomNum = Math.floor(9-Math.log(range)/Math.log(2));
	 map.setCenter(bounds.getCenter());
	 map.setZoom(zoomNum);
}
</script>

<script>
	function listResults(results){
		var num = results.length;
		if (num==0)
		{
			document.getElementById("searchResultMessage").innerHTML ="<h2><a href=\"javascript:onPublishValidate()\">本日尚未有临时拼车信息发布</a></h2>";
		}
		else
		{
			document.getElementById("searchResultMessage").innerHTML ="<h2><a href=\"javascript:onPublishValidate()\">没有找到你要的临时拼车信息?	</a></h2>";
		}
		
		var resultString="";
		
		for (var i=0;i<num;i++)
		{
			resultString =  resultString + getTransientRide(results[i],i);
		}
		document.getElementById("ride_content").innerHTML = resultString;
	};
	
	function getTransientRide(trInfo,rank)
	{
		var topicstring="";
		topicstring = topicstring + "<a href=\"./TransientTopic.jsp?trId="+trInfo.transientRideId +"\">";
		topicstring = topicstring + "<div class=\"entry\" origLat="+trInfo.origLoc_lat+" ";
		topicstring = topicstring + "origLng=" +  trInfo.origLoc_lon+" ";
		topicstring = topicstring + "destLat=" +  trInfo.destLoc_lat+" ";
		topicstring = topicstring + "destLng=" +  trInfo.destLoc_lon+" ";
		topicstring = topicstring + "rank=" +  rank +">";
		
		if (trInfo.userType)
		{
			topicstring = topicstring + "<div class=\"passenger_box\"><p>";
			topicstring = topicstring +"<span><img src='/TicketSchedule/Picture/nocar.jpg'/><br><span>";
			topicstring = topicstring + "<strong>不提供车<br>找"+trInfo.totalSeats+"人拼车</strong></p></div>";
		}
		else{
			topicstring = topicstring + "<div class=\"price_box\"><div class=\"seats\">";
			topicstring = topicstring +"<img src='/TicketSchedule/Picture/seats.jpg'/><span class='count'>"+trInfo.totalSeats+"</span></div>";
			topicstring = topicstring +"<p>每座<b>"+trInfo.price + "</b>元</p></div>";
		}
		
		topicstring = topicstring + "<div class=\"userpic\">";
		topicstring = topicstring + "<div class=\"username\">"+trInfo.owner_givenname+"</div>";
		topicstring = topicstring + "<img src= \"/TicketSchedule/pics/"+trInfo.owner_avatarID+"\" alt=\"Profile Picture\"></img>";
		topicstring = topicstring + "<span class=\"passenger\"></span></div>";
		topicstring = topicstring + "<div class=\"inner_content\"><h5>";
		//topicstring = topicstring + "<span class=\"inner\">"+"出发地："+trInfo.origLoc._addr+"<br>";
		//topicstring = topicstring + "目的地："+trInfo.destLoc._addr+"</span></h4>";
		topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_start.png\"/>"+"  出发地："+trInfo.origLoc_addr+"<br>";
		topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_end.png\"/>"+"  目的地："+trInfo.destLoc_addr+"<br>";
		topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/clock_small.jpg\"/>"+" 出发时间："+trInfo.rideTime+"<br>";
		topicstring = topicstring + "</h5></div></div></a>";
		return topicstring;
	};
</script>


<script type="text/javascript">
function publishRide()
{
	if (!isLogin)
	{
		window.location.href="/TicketSchedule/Zh/Login.jsp";
	}
	if (onPublishValidate())
	{
		var queryURL = "/TicketSchedule/servlet/PublishTransientTopic";
		if (document.getElementById("search_s").value!="") //Weird bug when integrating baidu api with the input box.
		{
			queryURL = queryURL+"?s="+document.getElementById("search_s").value; 
		}
		else
		{
			queryURL = queryURL+"?s="+origAddr;
		}
		queryURL = queryURL+"&origLat="+origLat;
		queryURL = queryURL+"&origLng="+origLng;
		
		if (document.getElementById("search_e").value!="") //Weird bug when integrating baidu api with the  box.
		{
		queryURL = queryURL+"&e="+document.getElementById("search_e").value; 
		}
		else
		{
			queryURL = queryURL+"&e="+destAddr;
		}

		queryURL = queryURL+"&destLat="+destLat;
		queryURL = queryURL+"&destLng="+destLng;
		queryURL = queryURL+"&distance="+distance;
		queryURL = queryURL+"&duration="+duration;
		date = document.getElementById("search_date").value;
		queryURL = queryURL+"&date="+date;
		var time_hour;
		if (document.getElementById("ride_time_ap").value=="AM")
		{
			time_hour=  document.getElementById("ride_time_hour").value;
		}
		else
		{
			time_hour=  Number(document.getElementById("ride_time_hour").value)+Number(12);
			if (time_hour>=24)
				time_hour =time_hour-24;
		}
		var time_minute = document.getElementById("ride_time_minute").value;
		
		queryURL = queryURL+"&time_hour="+time_hour;
		queryURL = queryURL+"&time_minute="+time_minute;
		queryURL = queryURL+"&userType="+userType;
		queryURL = queryURL+"&price="+document.getElementById("price").value;
		queryURL = queryURL+"&seats="+document.getElementById("seats").value;
		
		var tranid = getJson(queryURL);
		window.location.href = "/TicketSchedule/Zh/TransientTopic.jsp?trId="+tranid+"&onCreate=true";
	}
	else
	{
		return false;
	}
}

function onPublishValidate()
{
	if (document.getElementById("origLat").value=="" ||document.getElementById("destLat").value=="")
	{
		alert("先在搜索栏输入您的出发和目的地，并保证其位置显示在了地图上。然后在右下角输入具体信息。");
		document.getElementById("topAnchor").click();
		document.getElementById("additional-info").setAttribute("class", "panel");
		return false;
	}
	if (document.getElementById("additional-info").getAttribute("class")=="panel hidden")
	{
		document.getElementById("additional-info").setAttribute("class","panel");
		setDefaultTime();
		return false;
	}
	//More input validation here
	return true;
}

function setDefaultTime()
{
	var ctime = new Date();
	var hour = ctime.getHours();
	var minutes = ctime.getMinutes();
	var mu = Math.floor(minutes/10)+4;
	if (mu>=6)
	{ 
	  mu = mu-6;
	  hour = hour+1;
	}
	document.getElementById("ride_time_minute").selectedIndex = mu;
	if (hour>=12)
	{
		hour=hour - 12;
		if (hour!=12)
		{
			document.getElementById("ride_time_ap").value="PM";
			document.getElementById("ride_time_hour").value=hour;
		}
		else
		{
			//Next day
			document.getElementById("ride_time_ap").value="AM";
			document.getElementById("ride_time_hour").value= 0 ;
		}
	}
	else
	{
		document.getElementById("ride_time_hour").value=hour;
	}
	
	
	
}


function asDriver()
{
	document.getElementById("asDriver").setAttribute("class","active");
	document.getElementById("asPassenger").setAttribute("class","");
	document.getElementById("price-content").style.display="inline";
	userType = false;
}

function asPassenger()
{
	document.getElementById("asDriver").setAttribute("class","");
	document.getElementById("asPassenger").setAttribute("class","active");
	document.getElementById("price-content").style.display="none";
	userType = true;
}

</script>

<title>临时拼车</title>
</head>
<body id="search_index">
<div id="header_wrap">
	<div id="logo_wrap">
		<div id="logo">
		</div>
	</div>
	<div class="header_nav_wrapper">
		<div class="navbar navbar-default" role="navigation">
		<div class="navbar navbar-default">
			<ul class="nav navbar-nav">
			  <li><a href="/TicketSchedule/Zh/UserCenter.jsp">用户中心</a></li>
			  <li><a href="/TicketSchedule/Zh/ManageRide.jsp">管理行程</a></li> 
			  <li><a href="/TicketSchedule/Zh/SearchRide.jsp">上下班拼车</a></li>
			  <!--<li><a href="javascript:inbuilding()">管理行程</a></li> 
			  <li><a href="javascript:inbuilding()">上下班拼车</a></li>-->
		      <li class="active"><a href="#">临时拼车</a></li>
		    </ul>
		 </div>
		</div>
	</div>
	<div id="user_info_wrap">
		<%=user.getUserWrapper()%>
	</div>
</div>
<div id="content_wrapper">
	<div id="content_container"> 
		<div id="content">
			<div id="head">
				<div class="search">
					<div class="text_input">
						<label class="pin start" for="search_s"></label>
						<input id="search_s" class="input_text" type="text" placeholder="Starting from..." name="s" alt="search_start" value="" />
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="input_text" type="text" placeholder="Going to..." name="e" alt="search_end" value="" />
					</div>
					<div class="geo_internal" style="display:none">
						<input id="origLat" name="origLat" value=""></input>
						<input id="origLng" name="origLng" value=""></input>
						<input id="destLat" name="destLat" value=""></input>
						<input id="destLng" name="destLng" value=""></input>
						<input id="duration" name="duration" value="<%=tranRide==null?"":tranRide.dura%>"></input>
					</div>
					<div class="text_input datetime">
						<label class="datetime_icon"></label>
						<input id="search_date" class="slim datepicker hasDatepicker" type="text" value="exp" name="date" readonly="readonly" style="cursor:pointer">
					</div>
					<button class="btn btn-primary" type="submit" onclick="search()">查找</button>
				</div>
			</div>
			<div id="results">
				<div class="ride_list">
					<h3 id="headline" class="headline first"></h3>
					<div id="ride_content">
					</div>
						<div id="action">
						<div class="item postride">
							<div id="searchResultMessage"></div>
							<p>点击链接并在右下角补充更多信息后发布，其他人可以找到并加入你!							
							</p>
							<button id="createTopic" type="submit" class="button post" onclick="publishRide()">发布临时拼车</button>
						</div>
					</div>
				</div>
			</div>
			
			<div id="info">
				<div class="floatable">
					<div class="floatwrap" id="floatwrap">
				 		<div id="map-canvas">
						</div>
						<div class="panel hidden" id="additional-info">
							<div class="panel-heading">在此输入补充信息</div>
								<div class="panel-body">
									<div class="tabbable tabs-top">
					  					<ul class="nav nav-tabs">
					  						<li class="" id="asDriver"><a href="javascript: asDriver()"><img src= "/TicketSchedule/Picture/car.jpg"></img>有车</a></li>
					  						<li class="active" id="asPassenger"><a href="javascript: asPassenger()"><img src= "/TicketSchedule/Picture/nocar.jpg"></img>无车</a></li>
										</ul>
									</div>
									<div id="bargin-content">
										<div id="seats-content">
											<img src= "/TicketSchedule/Picture/seats.jpg" title="提供座位数"></img>
											<input type="text" id="seats" value="3"/>
										</div>
										<div id="price-content" style="display:none">
			 								<img src= "/TicketSchedule/Picture/yuansign.jpg" title="对搭车者要价"></img>
											<input type="text" id="price" value="15"/>
										</div>
										<div id="distance-content" >
			 								<img src= "/TicketSchedule/Picture/dissign.png" title="大约距离"></img>
											<input type="text" id="distance" value="<%=tranRide==null?"":tranRide.dist%>" readonly="readonly" />
										</div>
									</div>
									<div id="schedule-info">
										<img src= "/TicketSchedule/Picture/clock.jpg"/>
										<select name="ride_time_ap" id="ride_time_ap">
											<option value="AM">上午</option>
											<option value="PM">下午</option>
										</select>
										<select name="ride_time_hour" id="ride_time_hour" class="slim">
							                  <option value="0">0</option>	
							                  <option value="1">1</option>
							                  <option value="2">2</option>
							                  <option value="3">4</option>
							                  <option value="4">4</option>
							                  <option value="5">5</option>
							                  <option value="6">6</option>
							                  <option value="7">7</option>
							                  <option value="8">8</option>
							                  <option value="9">9</option>
							                  <option value="10">10</option>
							                  <option value="11">11</option>
		    				            </select>点
										<select name="ride_time_minute" id="ride_time_minute" class="slim">
							                  <option value="00">00</option>	
							                  <option value="10">10</option>
							                  <option value="20">20</option>
							                  <option value="30">30</option>
							                  <option value="40">40</option>
							                  <option value="50">50</option>
							        	</select>分
									</div>
									<div>
										<button id="publishTopic" type="submit" class="button post" onclick="publishRide()">发布</button>
									</div>
								</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
<div id="footer">
</div>


<div id="ui-datepicker-div" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"
 style="position: absolute; z-index:1;display:none;">
 	
 </div>
<a class="hidden" id ="topAnchor" href="#"></a> 
<!--  
<div id="baidu_tongji" style="display: none">
	<script type="text/javascript">
			var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
			document.write(unescape("%3Cscript src='"	+ _bdhmProtocol
			+ "hm.baidu.com/h.js%3F04d65d39238bfa4301b173d21ddcfeb7' type='text/javascript'%3E%3C/script%3E"));
	</script>
</div>
-->
</body>
</html>