<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
	CommuteRide actRide = (CommuteRide) request.getSession().getAttribute("actRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/searchcommutetopic.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/JS/calandar.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
<script>
$(document).ready(function(){
	initCalandar();
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	document.getElementById("headline").innerHTML="Departing<em>Today</em><span> - "+ new Date().toDateString() +"</span>";

	//Display Related
	var torigLat, torigLng, tdestLat, tdestLng;
	var tomarker,tdmarker;
	
	var images = {
		    url: '/TicketSchedule/Picture/pin_start.png',
	        size: new google.maps.Size(71, 71),
	        origin: new google.maps.Point(0, 0),
	        anchor: new google.maps.Point(17, 34),
	        scaledSize: new google.maps.Size(25, 25)
		  };
	var imagee = {
	          url: '/TicketSchedule/Picture/pin_end.png',
	          size: new google.maps.Size(71, 71),
	          origin: new google.maps.Point(0, 0),
	          anchor: new google.maps.Point(17, 34),
	          scaledSize: new google.maps.Size(25, 25)
	};
	
	
	
	
	//Search box realted
	var searchBoxO;
	var searchBoxD;
	var orig;
	var dest;
	var omarker;
	var dmarker;
	
	var mapOptions = {
			  center: new google.maps.LatLng(37.397, -122.144),
			  zoom: 8,
			  mapTypeId: google.maps.MapTypeId.ROADMAP
		};
	var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);

	
	var origLat="<%=actRide==null?"":actRide.origLoc.get_lat()%>";
	var origLng="<%=actRide==null?"":actRide.origLoc.get_lon()%>";
	var destLat="<%=actRide==null?"":actRide.destLoc.get_lat()%>";
	var destLng="<%=actRide==null?"":actRide.destLoc.get_lon()%>";
	var basicbounds = new google.maps.LatLngBounds();
	
	if (origLat!="" && origLng!="" && origLat!="" &&origLng!="")
	{
		var oLatlng = new google.maps.LatLng(origLat,origLng);
		var dLatlng = new google.maps.LatLng(destLat,destLng);
		
		omarker = new google.maps.Marker({
		    map: map,
		    icon: images,
		    position: oLatlng
		});
		dmarker = new google.maps.Marker({
			map: map,
			icon: imagee,
			position: dLatlng
		});
		
		document.getElementById("origLat").value=origLat;
		document.getElementById("origLng").value=origLng;
		document.getElementById("destLat").value=destLat;
		document.getElementById("destLng").value=destLng;
		
		basicbounds.extend(oLatlng);
		basicbounds.extend(dLatlng);
		map.fitBounds(basicbounds);
	}
	
	

    orig = document.getElementById('search_s');
	searchBoxO = new google.maps.places.SearchBox(orig);
    dest = document.getElementById('search_e');
	searchBoxD = new google.maps.places.SearchBox(dest);

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
	
	function refit()
	  {
		  var oLatlng = new google.maps.LatLng(origLat,origLng);
		  var dLatlng = new google.maps.LatLng(destLat,destLng);
		  basicbounds= new google.maps.LatLngBounds();
		  basicbounds.extend(oLatlng);
		  basicbounds.extend(dLatlng);
		  map.fitBounds(basicbounds);
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
  

	var results = JSON.parse(getJson("/TicketSchedule/SearchCommuteTopics"));
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
		topicstring = topicstring + "<a href='/TicketSchedule/CommuteTopicCenter?topicId="+topicInfo._topicId +"&type=commute&language=En'>";
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
		topicstring = topicstring + "<img src= \""+topicInfo.owner.get_head_portrait_path() +"\" alt=\"Profile Picture\"></img>";
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
			  <li><a href="/TicketSchedule/UserCenter.jsp">UserCenter</a></li>
			  <li><a href="/TicketSchedule/ManageRide.jsp">ManageRide</a></li>
		      <li class="active"><a href="#">SearchRide</a></li>
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
							autocomplete="off" value=<%=(actRide ==null) ? "" : actRide.origLoc._addr%>>
						</input>
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="input_text" type="text" 
						placeholder="Going to..." name="e" alt="search_end" 
						autocomplete="off" value=<%=(actRide ==null) ? "" : actRide.destLoc._addr%>>
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
								<a href="">Didn't find what you were looking for?						
								</a>
							</h2>
							<p>Create a topic about your travel plan so other people could find you!							
							</p>
							<form method="post" action="/TicketSchedule/CommuteTopicCenter">	
								<button id="createTopic" type="submit" class="button post">Create a Topic</button>
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
 			<span class="ui-datepicker-month" id="picker-Month">July</span>
 			&nbsp;
 			<span class="ui-datepicker-year" id="picker-Year">2013</span>
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