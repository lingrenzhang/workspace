<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ page import="com.shs.liangdiaosi.Calc.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%
	List<rideInfoParameters> results = (List<rideInfoParameters>) request.getAttribute("results");
	if (results==null)
	{
		List<rideInfoParameters> riders= new ArrayList<rideInfoParameters>();
		rideInfoParameters rideInfo1=new rideInfoParameters();
		rideInfo1.username="Xiyao J";
		rideInfo1.destCity="San Jose";
		rideInfo1.origCity="San Francisco";
		rideInfo1.userType=false;
		riders.add(rideInfo1);
		rideInfoParameters rideInfo2=new rideInfoParameters();
		rideInfo2.username="Lingren Zhang";
		rideInfo2.destCity="San Jose";
		rideInfo2.origCity="San Francisco";
		rideInfo2.userType=true;
		rideInfo2.seatsAvailable=3;
		rideInfo2.price=(double) 30;
		riders.add(rideInfo2);
		results=riders;
	}
	boolean commute = true;
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/style.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
<script>

var currentDate;
var selectDate;
var displayDate;

function selet_Date(value)
{
	var list=$(".ui-state-default");
	for (var i=0;i<list.length;i++)
	{
		if (list[i].innerHTML == value)
		{
			list[i].className = "ui-state-default ui-state-active";
		}
		else
		{
			list[i].className = "ui-state-default";
		}
	}
	
	selectDate.setMonth(displayDate.getMonth());
	selectDate.setFullYear(displayDate.getFullYear());
	selectDate.setDate(value);
	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	
};

function displayCalender(year,month)
{
	displayDate.setFullYear(year);
	displayDate.setMonth(month);
	var mon = displayDate.toDateString().substring(4,7);
	document.getElementById("picker-Month").innerHTML=mon;
	document.getElementById("picker-Year").innerHTML=displayDate.getFullYear();
	displayDate.setDate(1);
	var sWeekday = displayDate.getDay();
	var tablenode=document.getElementById("ui-datepicker-calendar");
	var theadnode= document.createElement("thead");
	var tbodies= tablenode.getElementsByTagName("tbody");
	tablenode.removeChild(tbodies[0]);
	var tbodynode = document.createElement("tbody");
	tablenode.appendChild(tbodynode);
	var temp = new Date();
	temp.setMonth(displayDate.getMonth()+1);
	temp.setDate(0);
	var maxday=temp.getDate();
	tbodynode.insertRow(0);
	for(var i=0;i<sWeekday;i++)
	{
		tbodynode.rows[0].insertCell(i);
		tbodynode.rows[0].cells[i].className=" ui-datepicker-week-end ui-datepicker-other-month ui-datepicker-unselectable ui-state-disabled";
	}
	var countday=1;
	var sdate=selectDate.getDate();
	for(var i=sWeekday;i<7;i++,countday++)
	{
		tbodynode.rows[0].insertCell(i);
		tbodynode.rows[0].cells[i].onclick="";
		var link = document.createElement("a");
		if (countday != sdate)
		{
			link.className="ui-state-default";
		}
		else
		{
			link.className="ui-state-default ui-state-active";
		}
		link.href="javascript:selet_Date("+countday+")";
		link.innerHTML=countday;
		tbodynode.rows[0].cells[i].appendChild(link);
	}

    var week=1;
	while (countday<=maxday)
	{
		tbodynode.insertRow(week);
		for(var i=0;i<7&&countday<=maxday;i++,countday++)
		{
			tbodynode.rows[week].insertCell(i);
			tbodynode.rows[week].cells[i].onclick="";
			var link = document.createElement("a");
			if (countday != sdate)
			{
				link.className="ui-state-default";
			}
			else
			{
				if (selectDate.getFullYear() == displayDate.getFullYear() && selectDate.getMonth()==displayDate.getMonth())
				{
					link.className="ui-state-default ui-state-active";
				}
				else
				{
					link.className="ui-state-default";
				}
			}
			link.innerHTML=countday;
			link.href="javascript:selet_Date("+countday+")";
			
			tbodynode.rows[week].cells[i].appendChild(link);
		}
		week++;
	}
}

window.onclick = function(e){
	var datepicker=$("#ui-datepicker-div");
	var left = parseFloat(datepicker.css("left"));
	var right = left + parseFloat(datepicker.css("width"));
	var top = parseFloat(datepicker.css("top"))-30;
	var down = top+parseFloat(datepicker.css("height"))+30;
	
	if ((e.pageX<left)||(e.pageX>right)
		||(e.pageY<top)||(e.pageY>down))
	{
  	   datepicker.css({"display":"none"});
	}
}

function nextMonth()
{
	displayCalender(displayDate.getFullYear(),displayDate.getMonth()+1);

}

function prevMonth()
{
	displayCalender(displayDate.getFullYear(),displayDate.getMonth()-1);
}

$(document).ready(function(){
	currentDate = new Date();
	selectDate= new Date();
	displayDate= new Date();

	document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();

	$(".datetime_icon").click(function(){
    //Data picker related
    var search= $("#search_date").offset();
    var datepicker=$("#ui-datepicker-div");
    datepicker.css({"left" : search.left,"top" : search.top+35});
    var sdate=document.getElementById("search_date").value;
    var dtArr=sdate.split("/");
    selectDate.setDate(dtArr[1]);
    selectDate.setMonth(dtArr[0]-1);
    selectDate.setFullYear(dtArr[2]);
    
    displayDate.setDate(dtArr[1]);
    displayDate.setMonth(dtArr[0]-1);
    displayDate.setFullYear(dtArr[2]);
 	var mon = displayDate.toDateString().substring(4,7);
	document.getElementById("picker-Month").innerHTML=mon;
	document.getElementById("picker-Year").innerHTML=displayDate.getFullYear();
	displayCalender(displayDate.getFullYear(),displayDate.getMonth());
    
	datepicker.fadeToggle();

	});

	$(".ui-state-default").click(function(){
		
		this.className="ui-state-default ui-state-active";
		selectDate.setMonth(displayDate.getMonth());
		selectDate.setYear(displayDate.getYear());
		selectDate.setDate(this.innerHtml);
		document.getElementById("search_date").value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
		
	});

	$("#commute_day").click(function(){
		$(this).className=$(this).className + " active";
	});

	

	//Search box realted
	var searchBoxO;
	var searchBoxD;
	var orig;
	var dest;
	var mapOptions = {
		  center: new google.maps.LatLng(-34.397, 150.644),
		  zoom: 8,
		  mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);

    orig = document.getElementById('search_s');
	searchBoxO = new google.maps.places.SearchBox(orig);
    dest = document.getElementById('search_e');
	searchBoxD = new google.maps.places.SearchBox(dest);

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
}


</script>
<script>

</script>
<title>Search</title>
</head>


<body id="search_index">
<div id="header_wrap">
	<div id="logo_wrap">
		<div id="logo">
		</div>
	</div>
</div>
<div id="content_wrapper">
	<div id="content_container">

		<div id="content">
			<div id="head">
				<form class="search" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
					<div class="text_input">
						<label class="pin start" for="search_s"></label>
						<input id="search_s" class="clickaway input_text" type="text" 
							placeholder="Starting from..." name="s" alt="search_start" 
							autocomplete="off">
						</input>
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="clickaway input_text" type="text" 
						placeholder="Going to..." name="e" alt="search_end" 
						autocomplete="off">
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
					
					<button class="button confirm clickaway_confirm" type="submit">Search</button>
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
					<% Iterator<rideInfoParameters> itr = results.iterator(); %>
					<% while (itr.hasNext()){ %>
					<% 	rideInfoParameters rideInfo = itr.next(); %>
    				<a href="./MessageBox?id=<%=rideInfo.username %>;">
						<div class="entry">
						<% if (!rideInfo.userType){ %>
							<div class="passenger_box">
								<p>
									<span class="icon"></span><%=rideInfo.username %> is a 
									<strong>passenger</strong>
								</p>
							</div>
						<% }else{ %>
							<div class="price_box">
								<div class="seats">
									<span class="count"><%=rideInfo.seatsAvailable %></span>
									<span class="left">seat left</span>
								</div>
								<p>
									<b>$<%=rideInfo.price %></b>
									 / seat
								</p>
							</div>
						<% } %>
							<div class="userpic">
								<div class="username"><%=rideInfo.username %></div>
								<img src="" alt="Profile Picture"></img>
								<span class="passenger"></span>
							</div>
							<div class="inner_content">
								<h3>
									<span class="inner"><%=rideInfo.origCity %>
										<span class="trip_type round_trip"></span>
										<%=rideInfo.destCity %>
									</span>
								</h3>
								<h4>
								From: <%=rideInfo.origAddr+", "+rideInfo.origCity+", "+rideInfo.origState  %>
								To: <%=rideInfo.destAddr+ ", "+rideInfo.destCity+", "+rideInfo.destState  %>
								</h4>
							</div>
						</div>
					</a>
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
						<div class="filters">
							<h3>Filters<span class="pint"></span></h3>
							<div class="inner">
								<span class="label">Show posts by</span>
								<div class="filterset postsby" data-name="filter_type">
									<a class="filter first active" data-filter="both" href>All</a>
									<a class="filter" data-filter="offer" href>
										<span>Drivers</span>
									</a>
									<a class="filter last passengers" data-filter="need" href>
										<span>Passengers</span>
									</a>
								</div>
								<span class="label">Vehicle type</span>
								<div class="filterset vehicletype" data-name="filter_vehicle">
									<a class="filter first active" data-filter="both" href>All</a>
									<a class="filter car" data-filter="car" href>
										<span>Car</span>
									</a>
									<a class="filter last bus" data=filter="bus" href>
										<span>Bus</span>
									</a>
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