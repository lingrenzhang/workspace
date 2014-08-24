<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hitchride.User" %>
<%@ page import="com.hitchride.CommuteTopic" %>
<%@ page import="com.hitchride.CommuteRide" %>
<%
	boolean commute = true;
%>
<%
	User user = (User) request.getSession().getAttribute("user");
    String IsLogin ="true";
	
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
	CommuteRide actRide = (CommuteRide) request.getSession().getAttribute("actRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/searchride.css" type="text/css" rel="stylesheet">
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
//Display Related
var torigLat, torigLng, tdestLat, tdestLng;
var tomarker,tdmarker;

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

var map;
var basicbounds = new BMap.Bounds();

$(document).ready(function(){
	initCalandar("ui-datepicker-div","search_date","map-canvas");
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	document.getElementById("headline").innerHTML="今日行程：<span> - "+ new Date().toDateString() +"</span>";
	
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
	
	var origLat="<%=actRide==null?"":actRide.origLoc.get_lat()%>";
	var origLng="<%=actRide==null?"":actRide.origLoc.get_lon()%>";
	var destLat="<%=actRide==null?"":actRide.destLoc.get_lat()%>";
	var destLng="<%=actRide==null?"":actRide.destLoc.get_lon()%>";

	
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
	
	

	//Initial search
	var rid = getURLPara("rid");
	if (rid==null)
	{
		search(-2); //For default search
	}
	else
	{
		search(rid);
	}
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
</script>

<script>
function search(rid)
{
	var queryURL;
	if (rid==-2  ) //-2 for initial
	{
		queryURL = "/TicketSchedule/servlet/SearchTopics";
	}
	else if(rid == -1) // -1 for user specified
	{
		queryURL = "/TicketSchedule/servlet/SearchTopics"+"?"+$('#geoinfo').serialize();
	}
	else
	{
		queryURL = "/TicketSchedule/servlet/SearchTopics?rid="+rid;
	}
	$.blockUI({ message: '<h1><img src="/TicketSchedule/Picture/busy1.gif" /></h1>' });

	$.ajax({
        cache: false,
        type: "Get",
        url:queryURL,
        //data: $('#geoinfo').serialize(),// 你的formid
        async: true,
        timeout:5000,
        error: function() {
        	alert('Server is busy');
    		$.unblockUI();
    		document.getElementById("searchResultMessage").innerHTML="<h2>服务器正忙，请重新检索</h2>";
        },
        success: function(data) {
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
	    		loadSchedule(results[rank]);
	    	});
        }
    });
	document.getElementById("searchResultMessage").innerHTML="<h2>检索中</h2>";
}
	
function listResults(results){
	var num = results.length;
	if (num==0)
	{
		document.getElementById("searchResultMessage").innerHTML ="<h2><a href=''>本日尚无行程发布</a></h2>";
	}
	else
	{
		document.getElementById("searchResultMessage").innerHTML ="<h2><a href=''>没有找到你要的上下班拼车信息?	</a></h2>";
	}
		
	var resultString="";
		
	for (var i=0;i<num;i++)
	{
		resultString =  resultString + getTopic(results[i],i);
	}
	document.getElementById("ride_content").innerHTML = resultString;
};
	
function getTopic(topicInfo,rank)
{
	var topicstring="";
	topicstring = topicstring + "<a href='/TicketSchedule/CommuteTopicCenter?topicId="+topicInfo._topicId +"&type=commute&language=Zh'>";
	topicstring = topicstring + "<div class='entry' origLat="+topicInfo.origLoc_lat+" ";
	topicstring = topicstring + "origLng=" +  topicInfo.origLoc_lon+" ";
	topicstring = topicstring + "destLat=" +  topicInfo.destLoc_lat+" ";
	topicstring = topicstring + "destLng=" +  topicInfo.destLoc_lon+" ";
	topicstring = topicstring + "rank=" +  rank +">";
	
	if (topicInfo.rideInfo_userType)
	{
		topicstring = topicstring + "<div class='passenger_box'><p>";
		topicstring = topicstring +"<span><img src='/TicketSchedule/Picture/nocar.jpg'/><br><span>";
		topicstring = topicstring + "<strong>不提供车<br>找"+topicInfo.rideInfo_totalSeats+"人同行</strong></p></div>";
	}
	else{
		topicstring = topicstring + "<div class=\"price_box\"><div class=\"seats\">";
		topicstring = topicstring +"<img src='/TicketSchedule/Picture/seats.jpg'/><span class='count'>"+topicInfo.rideInfo_totalSeats+"</span></div>";
		topicstring = topicstring +"<p>每座<b>"+topicInfo.rideInfo_price + "</b>元</p></div>";
	}
	
	topicstring = topicstring + "<div class='userpic'>";
	topicstring = topicstring + "<div class='username'>"+topicInfo.owner_givenname+"</div>";
	topicstring = topicstring + "<img src= '/TicketSchedule/pics/"+topicInfo.owner_avatarID+"' alt='Profile Picture'></img>";
	topicstring = topicstring + "<span class='passenger'></span></div>";
	topicstring = topicstring + "<div class='inner_content'><h5>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_start.png\"/>"+"  出发地："+topicInfo.origLoc_addr+"<br>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/pin_end.png\"/>"+"  目的地："+topicInfo.destLoc_addr+"<br>";
	topicstring = topicstring + "<span class=\"inner\"> <img src=\"/TicketSchedule/Picture/clock_small.jpg\"/>"+" 出发时间："+topicInfo.schedule.cftime[0];
	if (topicInfo.schedule._isRoundTrip){
		topicstring = topicstring + "&nbsp&nbsp&nbsp&nbsp返回时间："+topicInfo.schedule.cbtime[0];
	}
	
	
	topicstring = topicstring + "</h5></div></div></a>";
	return topicstring;
};

function loadSchedule(topicInfo)
{
	var schedule = "";
	var ridesche = topicInfo.schedule;
	if (ridesche._isCommute)
	{
		for (var i=0;i<7;i++)
		{
			 if (ridesche._dayOfWeek[i]){
				 switch (i)
				 {
				 	case 1 : schedule = schedule + "Mon: ";
				 		break;
				 	case 2 : schedule = schedule + "Tue: ";
				 		break;
				 	case 3 : schedule = schedule + "Wed: ";
				 		break;
				 	case 4 : schedule = schedule + "Thu: ";
				 		break;
				 	case 5 : schedule = schedule + "Fri: ";
			 			break;
				 	case 6 : schedule = schedule + "Sat: ";
			 			break;
				 	case 0 : schedule = schedule + "Sun: ";
			 			break;
				 }
				schedule = schedule + ridesche.cftime[i] + " " +  ridesche.cbtime[i] + "</br>";
			 }
		}
	}
	else
	{
		schedule = schedule + "Trip date: " + ridesche.tripDate + "</br>";
		schedule = schedule + "Trip time: " + ridesche.tripTime;
	}
	
	document.getElementById("schedule-info").innerHTML = schedule;
	return schedule;
};

function refitb(bounds)
{
	 var range = Math.max(bounds.toSpan().lat,bounds.toSpan().lng);
	 var zoomNum = Math.floor(9-Math.log(range)/Math.log(2));
	 map.setCenter(bounds.getCenter());
	 map.setZoom(zoomNum);
}

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
		
	distance = Math.sqrt(dx * dx + dy * dy)*1.1;
	duration = distance/12;
	
	document.getElementById("distance").setAttribute("value",distance);
	document.getElementById("duration").setAttribute("value",duration);
	    
	var price = Math.floor(distance/1200);
	//document.getElementById("price").setAttribute("value",price);
};
</script>


<title>上下班拼车</title>
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
		      <li class="active"><a href="#">上下班拼车</a></li>
		      <li><a href="/TicketSchedule/Zh/SearchTransientRide.jsp">临时拼车</a></li>
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
				<form class="search" id="geoinfo" action="/TicketSchedule/servlet/Search" method="get" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
					<div class="text_input">
						<label class="pin start" for="search_s"></label>
						<input id="search_s" class="input_text" type="text" 
							placeholder=<%=(actRide ==null) ? "Starting from..." : actRide.origLoc._addr%> name="s" alt="search_start" 
							autocomplete="off" value=<%=(actRide ==null) ? "" : actRide.origLoc._addr%>/>
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="input_text" type="text" 
						placeholder=<%=(actRide ==null) ? "Going to..." : actRide.destLoc._addr%> name="e" alt="search_end" 
						autocomplete="off" value=<%=(actRide ==null) ? "" : actRide.destLoc._addr%>/>
					</div>
					<div class="geo_internal" style="display:none">
						<input id="origLat" name="origLat" value=""></input>
						<input id="origLng" name="origLng" value=""></input>
						<input id="destLat" name="destLat" value=""></input>
						<input id="destLng" name="destLng" value=""></input>
						<input id="distance" name="distance" value="<%=actRide==null?"":actRide.dist%>"></input>
						<input id="duration" name="duration" value="<%=actRide==null?"":actRide.dura%>"></input>
					</div>
					<div class="text_input datetime">
						<label class="datetime_icon"></label>
						<input id="search_date" class="slim datepicker hasDatepicker" type="text" value="exp" name="date" readonly="readonly" style="cursor:pointer">
					</div>
					
					<!--  <button class="btn btn-primary" type="submit">查找</button>-->
				
				<%if (user.get_authLevel()>=32) {%>
				    <div class="sup_method">
				    	<input type="checkbox" name = "innergroup"/>InnerGroup
				    	<input type="checkbox" name = "listAll"/>ListAll
				    	<input type="checkbox" name = "useCommute">useCommute
				    	<%if (commute == true) { %>
						<div class="commute_input">
							<a class="commute_day first" >
								<span>Mon</span>
							</a>
							<a class="commute_day" >
								<span>Tue</span>
							</a>
							<a class="commute_day" >
								<span>Wed</span>
							</a>
							<a class="commute_day" >
								<span>Thu</span>
							</a>
							<a class="commute_day" >
								<span>Fri</span>
							</a>
							<a class="commute_day" >
								<span>Sat</span>
							</a>
							<a class="commute_day last" >
								<span>Sun</span>
							</a>
							<% } %>
						</div>
				    </div>
				<%} %>
				</form>
				<button class="btn btn-primary" type="submit" onclick="search(-1)">查找</button>
			</div>
			<div id="results">
				<div class="ride_list">
					<h3 id="headline" class="headline first"></h3>
					<div id="ride_content">
					</div>
					
					<div id="action">
						<div class="item postride">
							<div id="searchResultMessage"></div>				
							<p>根据你的行程信息创建讨论组，其他人可以检索并加入你!							
							</p>
							<form method="post" action="/TicketSchedule/CommuteTopicCenter">	
								<button id="createTopic" type="submit" class="button post">创建讨论组</button>
							</form>
						</div>
						
					</div>
				</div>
			</div>
			
			<div id="info">
				<div class="floatable">
					<div class="floatwrap" id="floatwrap">
				 		<div id="map-canvas">
						</div>
						<div class="panel" id="additional-info">
							<div id="schedule-info">
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
	
<div id="baidu_tongji" style="display: none">
	<script type="text/javascript">
		var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://"
					: " http://");
		document.write(unescape("%3Cscript src='"
							+ _bdhmProtocol
							+ "hm.baidu.com/h.js%3F04d65d39238bfa4301b173d21ddcfeb7' type='text/javascript'%3E%3C/script%3E"));
	</script>
</div>

</body>
</html>