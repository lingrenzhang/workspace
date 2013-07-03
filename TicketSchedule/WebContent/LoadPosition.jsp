<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.sql.*"%>
<%@page import="java.util.*" %>
<%
    ResultSet results = (ResultSet) request.getAttribute("ResultList");
    //session.setAttribute("ResultList",results);    
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map-canvas { height: 100% }
    </style>
<script src="../JS/jquery-1.10.1.js">
</script>
<script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
<script type="text/javascript">
var searchBoxO;
var searchBoxD;
var orig;
var dest;
function initialize() {
    var mapOptions = {
      center: new google.maps.LatLng(-34.397, 150.644),
      zoom: 8,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);

    orig = document.getElementById('orig');
  	searchBoxO = new google.maps.places.SearchBox(orig);
    dest = document.getElementById('dest');
  	searchBoxD = new google.maps.places.SearchBox(dest);
  	var markers = [];

 	google.maps.event.addListener(searchBoxO, 'places_changed', function() {
  	  var places = searchBoxO.getPlaces();
  	  place = places[0];
	  document.getElementById("origLat").value=place.geometry.location.jb;
	  document.getElementById("origLng").value=place.geometry.location.kb;
  	});

 	google.maps.event.addListener(searchBoxD, 'places_changed', function() {
   	  var places = searchBoxD.getPlaces();
      place = places[0];
	  document.getElementById("destLat").value=place.geometry.location.jb;
	  document.getElementById("destLng").value=place.geometry.location.kb;
   	});
   	
  	google.maps.event.addListener(map, 'bounds_changed', function() {
   	  var bounds = map.getBounds();
      searchBox.setBounds(bounds);
  	});
 }
 google.maps.event.addDomListener(window, 'load', initialize);

 function ImportDb()
 {
	    $("#orig").val("Shanghai").change(function(){google.maps.event.trigger(searchBoxO, 'places_changed');});

	    //google.maps.event.trigger(searchBoxO, 'places_changed');
	    $("#dest").val("Beijing\r").change(function(){google.maps.event.trigger(searchBoxD, 'places_changed');});
	    

	    	    
 }
 
</script>

</head>
<body>
<div id="panel">
    	<input id="orig" width="300px"></input>
    	<input id="dest" width="300px"></input>
</div>

<table>
	<tr>
			<td>OrigLat</td>
			<td><input id="origLat" name="origLat" value="0.00"></input></td>
			<td>OrigLng</td>
			<td><input id="origLng" name="origLng" value="0.00"></input></td>
	</tr>
	<tr>
			<td>DestLat</td>
			<td><input id="destLat" name="destLat" value="0.00"></input></td>
			<td>DestLng</td>
			<td><input id="destLng" name="destLng" value="0.00"></input></td>
	</tr>
	<tr>
		<td><input type="button" value="ImportDb" onclick="ImportDb()"></td>
	</tr>
</table>
<div id="map-canvas"></div>
<table>
	<tr>
		<td align="center">OrigCity</td>
		<td align="center">OrigAddress</td>
		<td align="center">DestCity</td>
		<td align="center">DestAddress</td>
		<td align="center">OrigLonLat</td>
		<td align="center">DestLonLat</td>
		<td align="center">TravelTime</td>
	</tr>
  <% int i=1; %>
  <% while(results.next()) {%>
	<tr>
		<td align="center" id="<%=i%>1"><%=results.getString(1) %></td>
	    <td align="center" id="<%=i%>2"><%=results.getString(2) %></td>
	    <td align="center"><%=results.getString(3) %></td>
	    <td align="center"><%=results.getString(4) %></td>
	    <td align="center"></td>
	    <td align="center"></td>
	    <td align="center"></td>
	</tr>
	<% i++; %>
	<%} %>
</table>


</body>
</html>