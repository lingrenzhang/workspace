<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hitchride.standardClass.User" %>
<%@ page import="com.hitchride.standardClass.Topic" %>
<%@ page import="com.hitchride.standardClass.RideInfo" %>
<%
	List<Topic> results = (List<Topic>) request.getAttribute("results");
	if (results==null)
	{
		
		List<Topic> nulltopic= new ArrayList<Topic>();
		results= nulltopic;
	}
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
		 user.set_name("guest");
		 user.set_emailAddress("guest");
		 user.set_avatarID("default.jpg");
		 user.set_userLevel(0);
	}
	RideInfo actRide = (RideInfo) request.getSession().getAttribute("actRide");
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/searchride.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/calandar.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
<script>
$(document).ready(function(){
	initCalandar();
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();

	//Display Related
	var torigLat, torigLng, tdestLat, tdestLng;
	var tomarker,tdmarker;

	var imaget = {
	          url: '/TicketSchedule/Picture/pin_end.png',
	          size: new google.maps.Size(71, 71),
	          origin: new google.maps.Point(0, 0),
	          anchor: new google.maps.Point(17, 34),
	          scaledSize: new google.maps.Size(25, 25)
	};
	
	$(".entry").hover(function(){
		torigLat = $(this)[0].getAttribute("origLat");
		torigLng = $(this)[0].getAttribute("origLng");
		tdestLat = $(this)[0].getAttribute("destLat");
		tdestLng = $(this)[0].getAttribute("destLng");
		var toLatlng = new google.maps.LatLng(torigLat,torigLng);
		var tdLatlng = new google.maps.LatLng(tdestLat,tdestLng);

		tomarker = new google.maps.Marker({
		            map: map,
		            icon: imaget,
		            position: toLatlng
		 });
		tdmarker = new google.maps.Marker({
            map: map,
            icon: imaget,
            position: tdLatlng
 		});
	 	var bounds = new google.maps.LatLngBounds();
	    bounds.extend(toLatlng);
		bounds.extend(tdLatlng);
		bounds.union(basicbounds);
		map.fitBounds(bounds);
	},
	function(){
		tomarker.setMap(null);
		tdmarker.setMap(null);
		map.fitBounds(basicbounds);
	});
	
	
	
	//Search box realted
	var searchBoxO;
	var searchBoxD;
	var orig;
	var dest;
	var omarkers = [];
	var dmarkers = [];
	var mapOptions = {
		  center: new google.maps.LatLng(37.397, -122.144),
		  zoom: 8,
		  mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("post-map-canvas"),mapOptions);

    orig = document.getElementById('search_s');
	searchBoxO = new google.maps.places.SearchBox(orig);
    dest = document.getElementById('search_e');
	searchBoxD = new google.maps.places.SearchBox(dest);

	google.maps.event.addListener(searchBoxO, 'places_changed', function() {
	  	  var places = searchBoxO.getPlaces();

	  	  for (var i = 0, marker; marker = omarkers[i]; i++) {
	        marker.setMap(null);
	      }

	      omarkers = [];
	      
	      for (var i = 0, place; place = places[i]; i++) {
	        var image = {
	          url: place.icon,
	          size: new google.maps.Size(71, 71),
	          origin: new google.maps.Point(0, 0),
	          anchor: new google.maps.Point(17, 34),
	          scaledSize: new google.maps.Size(25, 25)
	        };

	        var marker = new google.maps.Marker({
	            map: map,
	            icon: image,
	            title: place.name,
	            position: place.geometry.location
	          });
	        omarkers.push(marker);

	        bounds.extend(place.geometry.location);
	      }
	      map.fitBounds(bounds);

		      
	  	  place = places[0];
		  document.getElementById("origLat").value=place.geometry.location.lat();
		  document.getElementById("origLng").value=place.geometry.location.lng();
		  oLat=place.geometry.location.lat();
		  oLng=place.geometry.location.lng();
		  dLat=document.getElementById("destLat").value;
		  dLng=document.getElementById("destLng").value;
		  if (dLat !="" && dLng!="")
		  {
			  calculateDistances();
		  }
	});

	google.maps.event.addListener(searchBoxD, 'places_changed', function() {
	   	  var places = searchBoxD.getPlaces();
	   	  for (var i = 0, marker; marker = dmarkers[i]; i++) {
	        marker.setMap(null);
	      }

	      dmarkers = [];
	      for (var i = 0, place; place = places[i]; i++) {
	        var image = {
	          url: place.icon,
	          size: new google.maps.Size(71, 71),
	          origin: new google.maps.Point(0, 0),
	          anchor: new google.maps.Point(17, 34),
	          scaledSize: new google.maps.Size(25, 25)
	        };

	        var marker = new google.maps.Marker({
	            map: map,
	            icon: image,
	            title: place.name,
	            position: place.geometry.location
	          });
	        dmarkers.push(marker);

	        bounds.extend(place.geometry.location);
	      }
	      map.fitBounds(bounds);
	      place = places[0];
		  document.getElementById("destLat").value=place.geometry.location.lat();
		  document.getElementById("destLng").value=place.geometry.location.lng();
		  dLat=place.geometry.location.lat();
		  dLng=place.geometry.location.lng();
		  oLat=document.getElementById("origLat").value;
		  oLng=document.getElementById("origLng").value;
		  
		  if (oLat !="" && oLng!="")
		  {
			  calculateDistances();
		  }
	});

    
    var origLat="<%=actRide==null?"":actRide.origLoc.get_lat()%>";
	var origLng="<%=actRide==null?"":actRide.origLoc.get_lon()%>";
	var destLat="<%=actRide==null?"":actRide.destLoc.get_lat()%>";
	var destLng="<%=actRide==null?"":actRide.origLoc.get_lon()%>";
	var basicbounds = new google.maps.LatLngBounds();
	
	if (origLat!="" && origLng!="" && origLat!="" &&origLng!="")
	{
		var omarker,dmarker;
		var oLatlng = new google.maps.LatLng(origLat,origLng);
		var dLatlng = new google.maps.LatLng(destLat,destLng);
		
		var imageu = {
			    url: '/TicketSchedule/Picture/pin_start.png',
		        size: new google.maps.Size(71, 71),
		        origin: new google.maps.Point(0, 0),
		        anchor: new google.maps.Point(17, 34),
		        scaledSize: new google.maps.Size(25, 25)
			  };
		
		omarker = new google.maps.Marker({
		    map: map,
		    icon: imageu,
		    position: oLatlng
		});
		dmarker = new google.maps.Marker({
			map: map,
			icon: imageu,
			position: dLatlng
		});
		basicbounds.extend(oLatlng);
		basicbounds.extend(dLatlng);
		map.fitBounds(basicbounds);
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
			  <li><a href="/TicketSchedule/ManageRide.jsp">PostRide</a></li>
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
							autocomplete="off" value=<%=(request.getAttribute("orig") ==null) ? "" : request.getAttribute("orig")%>>
						</input>
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="input_text" type="text" 
						placeholder="Going to..." name="e" alt="search_end" 
						autocomplete="off" value=<%= (request.getAttribute("dest")==null) ? "" : request.getAttribute("dest") %>>
					</div>
					<div class="geo_internal" style="display:none">
						<input id="origLat" name="oLat" value=""></input>
						<input id="origLng" name="oLng" value=""></input>
						<input id="destLat" name="dLat" value=""></input>
						<input id="destLng" name="dLnt" value=""></input>
					</div>
					<%if (commute == true) { %>
					<div class="text_input datetime">
						<label class="datetime_icon" for="search_date"></label>
						<input id="search_date" class="slim datepicker hasDatepicker" type="text" value="exp" name="date">
					</div>
					<%}%>
					
					<button class="btn btn-primary" type="submit">Search</button>
					<%if (commute == false) { %>
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
					</div>
					<% } %>
				</form>
			</div>
			<div id="results">
				<div class="ride_list">
					<h3 class="headline first">Departing<em>Today</em>
						<span> - Friday, July 5th</span>
					</h3>
					<% Iterator<Topic> itr = results.iterator(); %>
					<% while (itr.hasNext()){ %>
					<% 	Topic topicInfo = itr.next(); %>
    						<%=topicInfo.getHTML()%>
					<% } %>
					

					<div id="action">
						<div class="item postride">
							<h2>
								<a href="">Didn't find what you were looking for?						
								</a>
							</h2>
							<p>Post a ride as a driver or passenger and get notified when new matches are found!							
							</p>
							<a class="button post" href="../Postride.jsp">Post a ride</a>
						</div>
					</div>
				</div>
				<div class="page_list">
					<span class="showing">Showing 1 - 2 of
						<strong> 20 results</strong>
					</span>
					<div class="pagination">
						<span class="first_page">
							<span> Prev </span>
						</span>
						<span class="current_page">
							<span>1</span>
						</span>
						<a href="id2">
							<span>2</span>
						</a>
						<a href="id2" class="next_page"><span> Next</span></a>
					</div>
				</div>
			</div>
			
			<div id="info">
				<div class="floatable">
					<div class="floatwrap" id="floatwrap">
				 		<div id="post-map-canvas">
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
<div id="footer">
</div>
<div id="map-canvas">
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
 			<tbody>
 			</tbody>
 		</thead>
 	</table>

 </div>



</body>
</html>