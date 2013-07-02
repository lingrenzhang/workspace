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
<title>Insert title here</title>

<script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=geometry">
</script>
<script type="text/javascript">
    function initialize() {

    	var address="Beach Blvd & Artesia Blvd";
    	geocoder.geocode( { 'address': address}, function(results, status) {
    	    if (status == google.maps.GeocoderStatus.OK) {
    	    	document.getElementById("latitude").value=results[0].geometry.location.jb;
    			document.getElementById("longitude").value=results[0].geometry.location.kb;
    	    } else {
    	      alert('Geocode was not successful for the following reason: ' + status);
    	    }
    	  });
      }
    
    function codeAddress() {
       	var address="Beach Blvd & Artesia Blvd";
    	  geocoder.geocode( { 'address': address}, function(results, status) {
      	    if (status == google.maps.GeocoderStatus.OK) {
      	    	document.getElementById("latitude").value=results[0].geometry.location.jb;
      			document.getElementById("longitude").value=results[0].geometry.location.kb;
      	    } else {
      	      alert('Geocode was not successful for the following reason: ' + status);
      	    }
      	  });
    }
    
</script>

</head>
<body>
<table>
	<tr>
			<td>Latitude</td>
			<td><input id="latitude" name="latitude" value="0.00"></input></td>
			<td>Longitude</td>
			<td><input id="longitude" name="longitude" value="0.00"></input></td>
	</tr>
	<tr>
		<td><input type="button" value="Geocode" onclick="codeAddress()"></td>
	</tr>
</table>
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