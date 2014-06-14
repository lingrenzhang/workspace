<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hitchride.standardClass.User" %>
<%@ page import="com.hitchride.standardClass.Topic" %>
<%@ page import="com.hitchride.standardClass.RideInfo" %>
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
	RideInfo actRide = (RideInfo) request.getSession().getAttribute("actRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/searchride.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<link rel="shortcut icon" href="/TicketSchedule/favicon.ico" type="image/x-icon" /> 

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
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
$(document).ready(function(){
	initCalandar();
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	document.getElementById("headline").innerHTML="Departing<em>Today</em><span> - "+ new Date().toDateString() +"</span>";

	//Display Related
	var torigLat, torigLng, tdestLat, tdestLng;
	var tomarker,tdmarker;
	
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
	
	//Search box realted
	var searchBoxO;
	var searchBoxD;
	var orig;
	var dest;
	var omarker;
	var dmarker;
	/*
	var mapOptions = {
			  center: new google.maps.LatLng(37.397, -122.144),
			  zoom: 8,
			  mapTypeId: google.maps.MapTypeId.ROADMAP
		};

	var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);
	*/
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


	var map = new BMap.Map("map-canvas");
	var point = new BMap.Point(nowLng,nowLat);
	map.addControl(new BMap.NavigationControl());    
	map.addControl(new BMap.ScaleControl());
	map.centerAndZoom(point,15);
	
	var origLat="<%=actRide==null?"":actRide.origLoc.get_lat()%>";
	var origLng="<%=actRide==null?"":actRide.origLoc.get_lon()%>";
	var destLat="<%=actRide==null?"":actRide.destLoc.get_lat()%>";
	var destLng="<%=actRide==null?"":actRide.destLoc.get_lon()%>";
	var basicbounds = new BMap.Bounds();
	
	if (origLat!="" && origLng!="" && origLat!="" &&origLng!="")
	{
		var oLatlng = new BMap.Point(origLat,origLng);
		var dLatlng = new Bmap.Point(destLat,destLng);
		
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
		map.fitBounds(basicbounds);
	}
	
	

    //orig = document.getElementById('search_s');
	searchBoxO = new BMap.Autocomplete(
			{"input" : "search_s",
			 "location" : map});
    //dest = document.getElementById('search_e');
	searchBoxD = new BMap.Autocomplete(
			{"input" : "search_e",
			 "location" : map});

	/*
	google.maps.event.addListener(searchBoxO, 'places_changed', function() {
	  	  var places = searchBoxO.getPlaces();
	  	  if (omarker!=null)
	  	  {
	      	omarker.setMap(null);
	  	  }
	  	  
	  	  place = places[0];
	      omarker = new google.maps.Marker({
	            map: map,
	            icon: images,
	            title: place.name,
	            position: place.geometry.location
	          });
	  	  
		  document.getElementById("origLat").value=place.geometry.location.lat();
		  document.getElementById("origLng").value=place.geometry.location.lng();
		  origLat=place.geometry.location.lat();
		  origLng=place.geometry.location.lng();
		  destLat=document.getElementById("destLat").value;
		  destLng=document.getElementById("destLng").value;
		  if (destLat !="" && destLng!="")
		  {
			  refit();
			  calculateDistances();
		  }
	});
    */
	searchBoxO.addEventListener("onconfirm",function(e){
		var _value = e.item.value;
		myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
		function myFun(){
			if (omarker!=null)
			{
				map.removeOverlay(omarker);
			}
			point = new BMap.Point(local.getResults().getPoi(0).point.lng,local.getResults().getPoi(0).point.lat);
			omarker = new BMap.Marker(point);
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
			  refit();
			  //calculateDistances();
			}
			
		}
		var local = new BMap.LocalSearch(map,{onSearchComplete : myFun});
		local.search(myValue);
	});

	
	/*
	google.maps.event.addListener(searchBoxD, 'places_changed', function() {
	   	  var places = searchBoxD.getPlaces();
	   	  if (dmarker!=null)
	  	  {
	      	 dmarker.setMap(null);
	  	  }
			
	      place = places[0];
          dmarker = new google.maps.Marker({
	            map: map,
	            icon: imagee,
	            title: place.name,
	            position: place.geometry.location
	          });
	     
		  document.getElementById("destLat").value=place.geometry.location.lat();
		  document.getElementById("destLng").value=place.geometry.location.lng();
		  destLat=place.geometry.location.lat();
		  destLng=place.geometry.location.lng();
		  origLat=document.getElementById("origLat").value;
		  origLng=document.getElementById("origLng").value;
		  
		  if (origLat !="" && origLng!="")
		  {
			  refit();
			  calculateDistances();
		  }
	});
	*/
	searchBoxD.addEventListener("onconfirm",function(e){
		var _value = e.item.value;
		myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
		function myFun(){
			if (dmarker!=null)
			{
				map.removeOverlay(dmarker);
			}
			point = new BMap.Point(local.getResults().getPoi(0).point.lng,local.getResults().getPoi(0).point.lat);
			dmarker = new BMap.Marker(point);
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
			  refit();
			  //calculateDistances();
			}
			
		}
		var local = new BMap.LocalSearch(map,{onSearchComplete : myFun});
		local.search(myValue);
	});
	
	function refit()
	  {
		  var oLatlng = new BMap.Point(origLng,origLat);
		  var dLatlng = new BMap.Point(destLng,destLat);
		  basicbounds= new BMap.Bounds(oLatlng,dLatlng);
		  var range = Math.max(basicbounds.toSpan().lat,basicbounds.toSpan().lng);
		  var zoomNum = Math.floor(9-Math.log(range)/Math.log(2));
		  map.setCenter(basicbounds.getCenter());
		  map.setZoom(zoomNum);
		  
	  }

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
		    document.getElementById("distance").value =response.rows[0].elements[0].distance.value;
		    document.getElementById("duration").value =response.rows[0].elements[0].duration.value;
	   }
	}
	  

	var results = JSON.parse(getJson("/TicketSchedule/servlet/SearchTopics"));
	listResults(results);
	$(".entry").hover(function(){
		torigLat = $(this)[0].getAttribute("origLat");
		torigLng = $(this)[0].getAttribute("origLng");
		tdestLat = $(this)[0].getAttribute("destLat");
		tdestLng = $(this)[0].getAttribute("destLng");
		var rank = $(this)[0].getAttribute("rank");
		var toLatlng = new google.maps.LatLng(torigLat,torigLng);
		var tdLatlng = new google.maps.LatLng(tdestLat,tdestLng);

		tomarker = new google.maps.Marker({
		      map: map,
		      icon: images,
		      position: toLatlng
		 });
		tdmarker = new google.maps.Marker({
            map: map,
            icon: imagee,
            position: tdLatlng
 		});
	 	var bounds = new google.maps.LatLngBounds();
	    bounds.extend(toLatlng);
		bounds.extend(tdLatlng);
		bounds.union(basicbounds);
		map.fitBounds(bounds);
		loadSchedule(results[rank]);
	},
	function(){
		tomarker.setMap(null);
		tdmarker.setMap(null);
		map.fitBounds(basicbounds);
	});
	
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
	function listResults(results){
		var num = results.length;
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
		topicstring = topicstring + "<a href=\"./RideCenter?topicId="+topicInfo._topicId +"&type=commute\">";
		topicstring = topicstring + "<div class=\"entry\" origLat="+topicInfo.ownerRide._rideInfo.origLoc._lat+" ";
		topicstring = topicstring + "origLng=" +  topicInfo.ownerRide._rideInfo.origLoc._lon+" ";
		topicstring = topicstring + "destLat=" +  topicInfo.ownerRide._rideInfo.destLoc._lat+" ";
		topicstring = topicstring + "destLng=" +  topicInfo.ownerRide._rideInfo.destLoc._lon+" ";
		topicstring = topicstring + "rank=" +  rank +">";
		
		if (topicInfo.ownerRide._rideInfo.userType)
		{
			topicstring = topicstring + "<div class=\"passenger_box\"><p>";
			topicstring = topicstring +"<span class=\"icon\"></span>";
			topicstring = topicstring + topicInfo.owner._givenname+" is a <strong>passenger</strong></p></div>";
		}
		else{
			topicstring = topicstring + "<div class=\"price_box\"><div class=\"seats\">";
			topicstring = topicstring +"<span class=\"count\">"+topicInfo.ownerRide._rideInfo.totalSeats+"</span></div>";
			topicstring = topicstring +"<p><b>"+topicInfo.ownerRide._rideInfo.price + "</b> / seat</p></div>";
		}
		
		topicstring = topicstring + "<div class=\"userpic\">";
		topicstring = topicstring + "<div class=\"username\">"+topicInfo.owner._givenname+"</div>";
		topicstring = topicstring + "<img src= \"/TicketSchedule/UserProfile/"+topicInfo.owner._avatarID+"\" alt=\"Profile Picture\"></img>";
		topicstring = topicstring + "<span class=\"passenger\"></span></div>";
		topicstring = topicstring + "<div class=\"inner_content\"><h3>";
		topicstring = topicstring + "<span class=\"inner\">"+topicInfo.ownerRide._rideInfo.origLoc._addr;
		topicstring = topicstring + "<span class=\"trip_type round_trip\"></span>";
		topicstring = topicstring + topicInfo.ownerRide._rideInfo.destLoc._addr+"</span></h3><h4>";
		topicstring = topicstring + "From: "+topicInfo.ownerRide._rideInfo.origLoc._formatedAddr;
		topicstring = topicstring + "To: "+topicInfo.ownerRide._rideInfo.destLoc._formatedAddr;
		topicstring = topicstring + "</h4></div></div></a>";
		return topicstring;
	};
	
	function loadSchedule(topicInfo)
	{
		var schedule = "";
		var ridesche = topicInfo.ownerRide._rideInfo.schedule;
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
</script>


<title>Search Ride</title>
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
				<form class="search" action="/TicketSchedule/servlet/Search" method="get" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
					<div class="text_input">
						<label class="pin start" for="search_s"></label>
						<input id="search_s" class="input_text" type="text" 
							placeholder="Starting from..." name="s" alt="search_start" 
							autocomplete="off" value=<%=(actRide ==null) ? "" : actRide.origLoc._addr%>/>
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="input_text" type="text" 
						placeholder="Going to..." name="e" alt="search_end" 
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
						<label class="datetime_icon" for="search_date"></label>
						<input id="search_date" class="slim datepicker hasDatepicker" type="text" value="exp" name="date">
					</div>
					
					<button class="btn btn-primary" type="submit">Search</button>

				
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
			</div>
			<div id="results">
				<div class="ride_list">
					<h3 id="headline" class="headline first"></h3>
					</h3>
					<div id="ride_content">
					</div>
					
					<div id="action">
						<div class="item postride">
							<h2>
								<a href="">没有找到你要的拼车信息?						
								</a>
							</h2>
							<p>根据你的拼车信息创建讨论组，其他人可以检索并加入你!							
							</p>
							<form method="post" action="/TicketSchedule/servlet/RideCenter">	
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
						<div id="schedule-info">
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
			<span class="ui-icon ui-icon-circle-triangle-w">Prev</span>
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

</body>
</html>