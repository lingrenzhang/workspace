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
	boolean commute = true;
%>
<% 
	String IsLogin =(String) request.getSession().getAttribute("IsLogin");
	User user;
	if (IsLogin!= null)
	{
		user = (User) request.getSession().getAttribute("user");
	}
	else
	{
		 user = new User();
		 user.set_authLevel(1);
		 user.set_name("guest");
		 user.set_emailAddress("guest");
		 user.set_avatarID("default.jpg");
		 user.set_userLevel(0);
	}
	TransientRide tranRide = (TransientRide) request.getSession().getAttribute("tranRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/searchtransientride.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/JS/calandar.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=Mto5Y3Pq2fgwkY2Kt9n60bWl"></script>
<script type="text/javascript">

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
	initCalandar();
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
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
	
	var origLat="<%=tranRide==null?"":tranRide.origLoc.get_lat()%>";
	var origLng="<%=tranRide==null?"":tranRide.origLoc.get_lon()%>";
	var origAddr= "<%=(tranRide ==null) ? "" : tranRide.origLoc._addr%>";
	var destLat="<%=tranRide==null?"":tranRide.destLoc.get_lat()%>";
	var destLng="<%=tranRide==null?"":tranRide.destLoc.get_lon()%>";
	var destAddr= "<%=(tranRide ==null) ? "" : tranRide.destLoc._addr%>";
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
	
	if (origLat!="" && origLng!="" && origLat!="" &&origLng!="")
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
	
   //orig = document.getElementById('search_s');
	searchBoxO = new BMap.Autocomplete(
			{"input" : "search_s",
			 "location" : map});
    //dest = document.getElementById('search_e');
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
	
	

	//Use google api now. Change when required to.
	function calculateDistances() {
		var service = new google.maps.DistanceMatrixService();
		var orig = new google.maps.LatLng(origLat,origLng);
		var dest = new google.maps.LatLng(destLat,destLng);
		service.getDistanceMatrix(
		{
		  origins: [orig],
		  destinations: [dest],
		  travelMode: google.maps.TravelMode.DRIVING,
		  unitSystem: google.maps.UnitSystem.METRIC,
		  avoidHighways: false,
		  avoidTolls: false
		 }, callback);
	}
		
	function callback(response, status) {
	  if (status != google.maps.DistanceMatrixStatus.OK) {
	    alert('Error was: ' + status);
	   } else {
		   var distance = response.rows[0].elements[0].distance.value;
		   var duration = response.rows[0].elements[0].duration.value;
		    document.getElementById("distance").setAttribute("value",distance);
		    document.getElementById("duration").setAttribute("value",duration);
		    document.getElementById("distanceP").setAttribute("value",distance);
		    document.getElementById("durationP").setAttribute("value",distance);
		    
		    var price = Math.floor(distance/1200);
		    document.getElementById("price").setAttribute("value",price);
	   }
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
	var origAddr =document.getElementById("search_s").value;
	var origLat = document.getElementById("origLat").value;
	var origLng = document.getElementById("origLng").value;
	var destAddr = document.getElementById("search_e").value;
	var destLat = document.getElementById("destLat").value;
	var destLng = document.getElementById("destLng").value;
	var distance = document.getElementById("distance").value;
	var duration = document.getElementById("duration").value;
	var date = document.getElementById("search_date").value;
	var queryURL = "/TicketSchedule/servlet/SearchTransientTopic";
	queryURL = queryURL+"?s="+origAddr+"&origLat="+origLat+"&origLng="+origLng;
	queryURL = queryURL+"&e="+destAddr+"&destLat="+destLat+"&destLng="+destLng;
	queryURL = queryURL+"&distance="+distance+"&duration"+duration;
	queryURL = queryURL+"&date="+date;
	document.getElementById("headline").innerHTML="<span>出发日："+date+"</span>";
    var results = JSON.parse(getJson(queryURL));
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
		topicstring = topicstring + "<a href=\"./TransientTopic?trId="+trInfo.transientRideId +"&type=commute\">";
		topicstring = topicstring + "<div class=\"entry\" origLat="+trInfo.origLoc._lat+" ";
		topicstring = topicstring + "origLng=" +  trInfo.origLoc._lon+" ";
		topicstring = topicstring + "destLat=" +  trInfo.destLoc._lat+" ";
		topicstring = topicstring + "destLng=" +  trInfo.destLoc._lon+" ";
		topicstring = topicstring + "rank=" +  rank +">";
		
		if (trInfo.userType)
		{
			topicstring = topicstring + "<div class=\"passenger_box\"><p>";
			topicstring = topicstring +"<span><img src='/TicketSchedule/Picture/nocar.jpg'/><br><span>";
			topicstring = topicstring + trInfo.owner._givenname+"<br> <strong>不提供车</strong></p></div>";
		}
		else{
			topicstring = topicstring + "<div class=\"price_box\"><div class=\"seats\">";
			topicstring = topicstring +"<img src='/TicketSchedule/Picture/seats.jpg'/><span class='count'>"+trInfo.totalSeats+"</span></div>";
			topicstring = topicstring +"<p><b>"+trInfo.price + "</b> / 座</p></div>";
		}
		
		topicstring = topicstring + "<div class=\"userpic\">";
		topicstring = topicstring + "<div class=\"username\">"+trInfo.owner._givenname+"</div>";
		topicstring = topicstring + "<img src= \"/TicketSchedule/UserProfile/"+trInfo.owner._avatarID+"\" alt=\"Profile Picture\"></img>";
		topicstring = topicstring + "<span class=\"passenger\"></span></div>";
		topicstring = topicstring + "<div class=\"inner_content\"><h4>";
		//topicstring = topicstring + "<span class=\"inner\">"+"出发地："+trInfo.origLoc._addr+"<br>";
		//topicstring = topicstring + "目的地："+trInfo.destLoc._addr+"</span></h4>";
		topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_start.png\"/>"+"  出发地："+trInfo.origLoc._addr+"<br>";
		topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_end.png\"/>"+"  目的地："+trInfo.destLoc._addr+"<br>";
		topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/clock_small.jpg\"/>"+" 出发时间："+trInfo.rideTime+"<br>";
		topicstring = topicstring + "</div></div></a>";
		return topicstring;
	};
</script>


<script type="text/javascript">
function publishRide()
	{
		if (document.getElementById("origLat") ||document.getElementById("destLat").value=="")
		{
			alert("先在搜索栏输入您的出发和目的地，并保证其位置显示在了地图上。然后在右下角输入具体信息。");
			document.getElementById("topAnchor").click();

		}
		else
		{
			document.getElementById("search_sP").setAttribute("value","");
			document.getElementById("origLatP").setAttribute("value",origLat);
			document.getElementById("origLngP").setAttribute("value",origLng);
			document.getElementById("search_eP").setAttribute("value","");
			document.getElementById("destLatP").setAttribute("value",destLat);
			document.getElementById("destLngP").setAttribute("value",destLng);
			//Set when initialized
			//document.getElementById("distanceP").setAttribute("value","");
			//document.getElementById("durationP").setAttribute("value","");
		}
	document.getElementById("additional-info").setAttribute("class", "panel");
}

function onSearchValidate()
{
	if (document.getElementById("search_s").value=="" ||document.getElementById("search_e").value=="")
	{
		return false;
	}
	
}

function onPublishValidate()
{
	
}

function initCurrentTime()
{
	var myDate= new Date();
	document.getElementById();
}

function asDriver()
{
	document.getElementById("asDriver").setAttribute("class","active");
	document.getElementById("asPassenger").setAttribute("class","");
	document.getElementById("seats-content").style.display="inline";
}

function asPassenger()
{
	document.getElementById("asDriver").setAttribute("class","");
	document.getElementById("asPassenger").setAttribute("class","active");
	document.getElementById("seats-content").style.display="none";
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
						<input id="distance" name="distance" value="<%=tranRide==null?"":tranRide.dist%>"></input>
						<input id="duration" name="duration" value="<%=tranRide==null?"":tranRide.dura%>"></input>
					</div>
					<div class="text_input datetime">
						<label class="datetime_icon" for="search_date"></label>
						<input id="search_date" class="slim datepicker hasDatepicker" type="text" value="exp" name="date">
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
							<h2>
								<a href="">没有找到你要的临时拼车信息?						
								</a>
							</h2>
							<p>补充更多临时拼车信息并发布，其他人可以找到并加入你!							
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
							<form method="post" action="/TicketSchedule/servlet/TransientRideCenter" onsubmit="return onPublishValidate()">	
								<div class="geo_Pinternal" style="display:none">
									<input id="search_sP" name="sP" value=""/>
									<input id="origLatP" name="origLatP" value=""/>
									<input id="origLngP" name="origLngP" value=""/>
									<input id="search_eP" name="eP" value=""/>
									<input id="destLatP" name="destLatP" value=""/>
									<input id="destLngP" name="destLngP" value=""/>
									<input id="distanceP" name="distanceP" value=""/>
									<input id="durationP" name="durationP" value=""/>
								</div>
								<div class="schedule_Pinternal" style="display:none">
									<input id="dateP" name="date" value=""/>
									<input id="timeP" name="time" value=""/>
								</div>
								<div class="bargin_Pinternal" style="display:none">
									<input id="userTypeP" name="userTypeP" value=""/>
									<input id="totalSeatsP" name="totalSeats" value=""/>
									<input id="payPerSeatP" name="payPerSeat" value=""/>
								</div>
								<button type="submit" id="internalSub"></button>
							</form>
								<div class="panel-heading">在此输入补充信息</div>
								<div class="panel-body">
									<div class="tabbable tabs-top">
					  					<ul class="nav nav-tabs">
					  						<li class="" id="asDriver"><a href="javascript: asDriver()"><img src= "/TicketSchedule/Picture/car.jpg"></img>有车</a></li>
					  						<li class="active" id="asPassenger"><a href="javascript: asPassenger()"><img src= "/TicketSchedule/Picture/nocar.jpg"></img>无车</a></li>
										</ul>
									</div>
									<div id="bargin-content">
										<div id="seats-content" style="display:none">
											<img src= "/TicketSchedule/Picture/seats.jpg"></img>
											<input type="text" id="seats" value="3"/>
										</div>
										<div id="price-content" >
			 								<img src= "/TicketSchedule/Picture/yuansign.jpg"></img>
											<input type="text" id="price" value="15"/>
										</div>
									</div>
									<div id="schedule-info">
										<img src= "/TicketSchedule/Picture/clock.jpg"/>
										<select name="ride_time_ap" id="ride_time_ap">
											<option value="AM">上午</option>
											<option value="PM">下午</option>
										</select>
										<select name="ride_time_hour" id="ride_time_hour" class="slim">
							                  <option value="1">1</option>	
							                  <option value="2">2</option>
							                  <option value="3">3</option>
							                  <option value="4">4</option>
							                  <option value="5">5</option>
							                  <option value="6">6</option>
							                  <option value="7">7</option>
							                  <option value="8">8</option>
							                  <option value="9">9</option>
							                  <option value="10">10</option>
							                  <option value="11">11</option>
							                  <option value="12">12</option>
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
 	<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all">
		<a class="ui-datepicker-prev ui-corner-all" onclick="prevMonth()" title="Prev">
			<span class="ui-icon ui-icon-circle-triangle-e">Prev</span>
		</a>
 		<a class="ui-datepicker-next ui-corner-all" onclick="nextMonth()" title="Next">
 			<span class="ui-icon ui-icon-circle-triangle-w">Next</span>
 		</a>
 		<div class="ui-datepicker-title">
 			<span class="ui-datepicker-month" id="picker-Month"></span>
 			&nbsp;
 			<span class="ui-datepicker-year" id="picker-Year"></span>
 		</div>
 	</div>
 	<table class="ui-datepicker-calendar" id="ui-datepicker-calendar">
 		<thead>
 			<tr>
 				<th class="ui-datepicker-week-end">
 					<span title="Sunday">Su</span>
 				</th>
 				<th>
 					<span title="Monday">Mo</span>
 				</th>
 				<th>
 					<span title="Tuesday">Tu</span>
 				</th>
 				<th>
 					<span title="Wednesday">We</span>
 				</th>
 				<th>
 					<span title="Thursday">Th</span>
 				</th>
 				<th>
 					<span title="Friday">Fr</span>
 				</th>
 				<th class="ui-datepicker-week-end">
 					<span title="Saturday">Sa</span>
 				</th>
 			</tr>
 		</thead>
 		<tbody>
 		</tbody>
 	</table>
 </div>
	<a class="hidden" id ="topAnchor" href="#"></a> 
</body>
</html>