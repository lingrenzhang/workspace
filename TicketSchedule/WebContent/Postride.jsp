<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="./CSS/style.css" type="text/css" rel="stylesheet">
<script src="./JS/jquery-1.10.1.js"></script>
<script>
$(document).ready(function(){
	$(".datetime_icon").click(function(){
    var search= $("#search_date").offset();
    var datepicker=$("#ui-datepicker-div");
    datepicker.css({"left" : search.left,"top" : search.top+35});
	datepicker.fadeToggle();
	});

	$("#commute_day").click(function(){
		$(this).className=$(this).className + " active";
	});

	$("#back").change(function(){
		
	});
	
	$("#there_time_0").change(function(){
		$("p.time-alias1").text($(this).children('option:selected').val());
	});

	$("#back_time_0").change(function(){
		$("p.time-alias2").text($(this).children('option:selected').val());
	});


	$(".pillbuttons").children("div").click(function(){
		switch ($(this).text())
		{
			case "0min":
				this.className="first chosen";
				this.parentNode.children[1].className="";
				this.parentNode.children[2].className="last";
				this.parentNode.children[3].value="0min";
				break;
			case "10-15min":
				this.parentNode.children[0].className="first";
				this.className="chosen";
				this.parentNode.children[2].className="last";
				this.parentNode.children[3].value="10-15min";
				break;
			case "30min+":
				this.parentNode.children[0].className="first";
				this.parentNode.children[1].className="";
				this.className="last chosen";
				this.parentNode.children[3].value="30min";
				break;
		}
			
	});
	
	$("a.edit").click(function(){
		if ($(this).text()=="edit")
		{
			$(this).text("revert");
			$(this).css("opacity", "0.6");
			Id=$(this).attr("id");
			num=Id.charAt(11);
			switch (num)
			{
				case "1":
					$("ul#col2").children("li:eq(1)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(1)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(1)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(1)").children("select")[0].className="slim";
					break;
				case "2":
					$("ul#col2").children("li:eq(2)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(2)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(2)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(2)").children("select")[0].className="slim";
					break;
				case "3":
					$("ul#col2").children("li:eq(3)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(3)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(3)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(3)").children("select")[0].className="slim";
					break;
				case "4":
					$("ul#col2").children("li:eq(4)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(4)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(4)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(4)").children("select")[0].className="slim";
					break;
				case "5":
					$("ul#col2").children("li:eq(5)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(5)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(5)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(5)").children("select")[0].className="slim";
					break;
				case "6":
					$("ul#col2").children("li:eq(6)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(6)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(6)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(6)").children("select")[0].className="slim";
					break;
				case "7":
					$("ul#col2").children("li:eq(7)").children("p")[0].className="time-alias1 hidden";
					$("ul#col2").children("li:eq(7)").children("select")[0].className="slim";
					$("ul#col3").children("li:eq(7)").children("p")[0].className="time-alias2 hidden";
					$("ul#col3").children("li:eq(7)").children("select")[0].className="slim";
					break;
			}
		}
		else
		{
			$(this).text("edit");
			$(this).css("opacity", "1");
			Id=$(this).attr("id");
			num=Id.charAt(11);
			switch (num)
			{
				case "1":
					$("ul#col2").children("li:eq(1)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(1)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(1)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(1)").children("select")[0].className="slim hidden";
					break;
				case "2":
					$("ul#col2").children("li:eq(2)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(2)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(2)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(2)").children("select")[0].className="slim hidden";
					break;
				case "3":
					$("ul#col2").children("li:eq(3)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(3)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(3)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(3)").children("select")[0].className="slim hidden";
					break;
				case "4":
					$("ul#col2").children("li:eq(4)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(4)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(4)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(4)").children("select")[0].className="slim hidden";
					break;
				case "5":
					$("ul#col2").children("li:eq(5)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(5)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(5)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(5)").children("select")[0].className="slim hidden";
					break;
				case "6":
					$("ul#col2").children("li:eq(6)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(6)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(6)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(6)").children("select")[0].className="slim hidden";
					break;
				case "7":
					$("ul#col2").children("li:eq(7)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(7)").children("select")[0].className="slim hidden";
					$("ul#col3").children("li:eq(7)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(7)").children("select")[0].className="slim hidden";
					break;
			}
		}
	});
	
	$(".checkbox").change(function(){
		if ($(this).prop('checked')==true)
		{
			Id=$(this).attr("id");
			num=Id.charAt(6);
			switch (num)
			{
				case "1":
					$("ul#col2").children("li:eq(1)").children("p").css("color","black");
					$("ul#col2").children("li:eq(1)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(1)").children("p").css("color","black");
					$("ul#col3").children("li:eq(1)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(1)").children("a").css("visibility","visible");
					break;
				case "2":
					$("ul#col2").children("li:eq(2)").children("p").css("color","black");
					$("ul#col2").children("li:eq(2)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(2)").children("p").css("color","black");
					$("ul#col3").children("li:eq(2)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(2)").children("a").css("visibility","visible");
					break;
				case "3":
					$("ul#col2").children("li:eq(3)").children("p").css("color","black");
					$("ul#col2").children("li:eq(3)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(3)").children("p").css("color","black");
					$("ul#col3").children("li:eq(3)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(3)").children("a").css("visibility","visible");
					break;
				case "4":
					$("ul#col2").children("li:eq(4)").children("p").css("color","black");
					$("ul#col2").children("li:eq(4)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(4)").children("p").css("color","black");
					$("ul#col3").children("li:eq(4)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(4)").children("a").css("visibility","visible");
					break;
				case "5":
					$("ul#col2").children("li:eq(5)").children("p").css("color","black");
					$("ul#col2").children("li:eq(5)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(5)").children("p").css("color","black");
					$("ul#col3").children("li:eq(5)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(5)").children("a").css("visibility","visible");
					break;
				case "6":
					$("ul#col2").children("li:eq(6)").children("p").css("color","black");
					$("ul#col2").children("li:eq(6)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(6)").children("p").css("color","black");
					$("ul#col3").children("li:eq(6)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(6)").children("a").css("visibility","visible");
					break;
				case "7":
					$("ul#col2").children("li:eq(7)").children("p").css("color","black");
					$("ul#col2").children("li:eq(7)").children("select").removeAttr("disabled");
					$("ul#col3").children("li:eq(7)").children("p").css("color","black");
					$("ul#col3").children("li:eq(7)").children("select").removeAttr("disabled");
					$("ul#col4").children("li:eq(7)").children("a").css("visibility","visible");
					break;
					
			}
		}
		else
		{
			Id=$(this).attr("id");
			num=Id.charAt(6);
			switch (num)
			{
				case "1":
					$("ul#col2").children("li:eq(1)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(1)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(1)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(1)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(1)").children("a").css("visibility","hidden");
					break;
				case "2":
					$("ul#col2").children("li:eq(2)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(2)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(2)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(2)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(2)").children("a").css("visibility","hidden");
					break;
				case "3":
					$("ul#col2").children("li:eq(3)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(3)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(3)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(3)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(3)").children("a").css("visibility","hidden");
					break;
				case "4":
					$("ul#col2").children("li:eq(4)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(4)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(4)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(4)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(4)").children("a").css("visibility","hidden");
					break;
				case "5":
					$("ul#col2").children("li:eq(5)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(5)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(5)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(5)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(5)").children("a").css("visibility","hidden");
					break;
				case "6":
					$("ul#col2").children("li:eq(6)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(6)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(6)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(6)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(6)").children("a").css("visibility","hidden");
					break;
				case "7":
					$("ul#col2").children("li:eq(7)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col2").children("li:eq(7)").children("select").attr("disabled","disabled");
					$("ul#col3").children("li:eq(7)").children("p").css("color","rgb(187, 187, 187)");
					$("ul#col3").children("li:eq(7)").children("select").attr("disabled","disabled");
					$("ul#col4").children("li:eq(7)").children("a").css("visibility","hidden");
					break;
			}
		}
	});
});

</script>
<title>Insert title here</title>
</head>
<body>
<div id="content_wrapper">
	<div id="content_container" class="clearfix">
		<div id="wide_column_left" class="newAddRideStyle">
			<form action="/servlet/Postride" method="post" id="add_ride" class="standard requires_login_results">
				<fieldset id="step_1">
					<dl>
						<dt>
							<label>I Am</label>
						</dt>
						<dd class="selection" id="who">
							<div class="bigradio" id="driverselect">
                           		<input type="radio" name="who" id="offerid" value="offer" class="radio"><label for="offerid">willing to drive</label>
                        	</div>
							<div class="bigradio" id="passengerselect">
                	           	<input type="radio" name="who" id="needid" value="need" class="radio">
                 		       	<label for="needid">passenger only</label>
                 		  	</div>
                        </dd>
                        
                        <dt><label for="s">Starting From</label></dt>
                        <dd>
                        	<input type="text" class="clickaway ac_input" placeholder="e.g. University Road, Santa Barbara, CA" name="s" id="s" maxlength="400" value="e.g. University Road, Santa Barbara, CA" autocomplete="off">
                        </dd>
                        
                        <dt><label for="e">Going To</label></dt>
                        <dd>
                       		<input class="clickaway ac_input" type="text" placeholder="e.g. San Diego, CA" name="e" id="e" maxlength="400" value="e.g. San Diego, CA" autocomplete="off">
                        	<p>Only the closest crossstreets are shown in your listing (e.g. 1st &amp; Main)</p>
                	    </dd>
						<dd class="changestep">
							<span class="errormsg"></span> 
							<button id="addride-whereto-button" type="button" class="confirm clickaway_confirm next">
							<span>Next Step</span> 
							<img src="http://www.zimride.com/assets/forward-arrow-white.png"></button>
						</dd>
					
					</dl>
				</fieldset>
				
				<fieldset id="step_2" >
                <dl>
                    <input id="onetime-only" type="hidden" name="type" value="one-time"></input>
                    <dd class="triptabs" style="display: block;">
                   	<div id="multipostwrapper">
	                        <div id="singletripwrapper">
	                        	<span class="trip_num">Trip 0</span>
	                        	<span class="float_right trip_close dot">�</span><br>
	                        	<div class="tripbox">
	                               <div id="there_one_time">
	                            	    <label for="depart-date" class="depart-date">Depart</label>
		                                <input type="text" name="date" id="depart-date" class="slim datepicker depart-date hasDatepicker" maxlength="10" value="07/14/2013">
		                                <img class="ui-datepicker-trigger" src="/TicketSchedule/Picture/icon_calendar.png" alt="..." title="...">
		   								<select name="there_time" id="there_time" class="slim there_time">
			                                <option value="anytime" selected="selected">anytime</option>
			                                <option value="early">early (12a-8a)</option>
			                                <option value="morning">morning (8a-12p)</option>
			                                <option value="afternoon">afternoon (12p-5p)</option>
			                                <option value="evening">evening (5p-9p)</option>
			                                <option value="night">night (9p-12a)</option>
			                                <option value="1:00am">1:00am</option>
			                                <option value="2:00am">2:00am</option>
			                                <option value="3:00am">3:00am</option>
			                                <option value="4:00am">4:00am</option>
			                                <option value="5:00am">5:00am</option>
			                                <option value="6:00am">6:00am</option>
			                                <option value="7:00am">7:00am</option>
			                                <option value="8:00am">8:00am</option>
			                                <option value="9:00am">9:00am</option>
			                                <option value="10:00am">10:00am</option>
			                                <option value="11:00am">11:00am</option>
			                                <option value="12:00pm">noon</option>
			                                <option value="1:00pm">1:00pm</option>
			                                <option value="2:00pm">2:00pm</option>
			                                <option value="3:00pm">3:00pm</option>
			                                <option value="4:00pm">4:00pm</option>
			                                <option value="5:00pm">5:00pm</option>
			                                <option value="6:00pm">6:00pm</option>
			                                <option value="7:00pm">7:00pm</option>
			                                <option value="8:00pm">8:00pm</option>
			                                <option value="9:00pm">9:00pm</option>
			                                <option value="10:00pm">10:00pm</option>
			                                <option value="11:00pm">11:00pm</option>		                                
		                                </select>
		                            </div>
		                            <p><input type="checkbox" name="back" id="back" value="1" class="checkbox back" checked="checked"></p>
		                            <label for="return-date" class="return-date" style="opacity: 1;">Return</label>
		                            <div id="back_one_time">
		                            	<input type="text" name="return_date" id="return-date" class="slim datepicker return-date hasDatepicker" maxlength="10" value="07/15/2013">
		                                <img class="ui-datepicker-trigger" src="/TicketSchedule/Picture/icon_calendar.png" alt="..." title="...">
		                                <select name="back_time" id="back_time" class="slim back_time">
	                                    	<option value="anytime" selected="selected">anytime</option>
	                                    	<option value="early">early (12a-8a)</option>
	                                    	<option value="morning">morning (8a-12p)</option>
	                                    	<option value="afternoon">afternoon (12p-5p)</option>
	                                    	<option value="evening">evening (5p-9p)</option>
	                                    	<option value="night">night (9p-12a)</option>
	                                    	<option value="1:00am">1:00am</option>
	                                    	<option value="2:00am">2:00am</option>
	                                    	<option value="3:00am">3:00am</option>
	                                    	<option value="4:00am">4:00am</option>
	                                    	<option value="5:00am">5:00am</option>
	                                    	<option value="6:00am">6:00am</option>
	                                    	<option value="7:00am">7:00am</option>
	                                    	<option value="8:00am">8:00am</option>
	                                    	<option value="9:00am">9:00am</option>
	                                    	<option value="10:00am">10:00am</option>
	                                    	<option value="11:00am">11:00am</option>
	                                    	<option value="12:00pm">noon</option>
	                                    	<option value="1:00pm">1:00pm</option>
	                                    	<option value="2:00pm">2:00pm</option>
	                                    	<option value="3:00pm">3:00pm</option>
	                                    	<option value="4:00pm">4:00pm</option>
		                                    <option value="5:00pm">5:00pm</option>
		                                    <option value="6:00pm">6:00pm</option>
		                                    <option value="7:00pm">7:00pm</option>
		                                    <option value="8:00pm">8:00pm</option>
		                                    <option value="9:00pm">9:00pm</option>
		                                    <option value="10:00pm">10:00pm</option>
		                                    <option value="11:00pm">11:00pm</option>		                                </select>
			                            </div>
	                            	</div>
	                        	</div>
		                        <button id="add_multi" type="button"><b>+</b> Add more dates</button> 
		                        <span class="max_trips hidden">Oops! You can only add a maximum of five rides at a time.</span>
	                        	<input type="hidden" name="multi_trips" id="multi_trips" value="0">
	                        	<input type="hidden" name="num_trips" id="num_trips" value="1">
                     </div>

					<div id="there_repeating" style="display: block;">
                            <div id="commute-table">
								<ul id="col1">
                                    <li class="first"><p>Choose your times and flexibility:</p></li>
                                    <li><input type="checkbox" name="there_1" id="there_1" class="checkbox" checked="checked" alt="Enable trip for Monday"><label for="there_1">Monday</label></li>
                                    <li><input type="checkbox" name="there_2" id="there_2" class="checkbox" checked="checked" alt="Enable trip for Tuesday"><label for="there_2">Tuesday</label></li>                                                     
                          			<li><input type="checkbox" name="there_3" id="there_3" class="checkbox" checked="checked" alt="Enable trip for Wednesday"><label for="there_3">Wednesday</label></li>                                                 
  									<li><input type="checkbox" name="there_4" id="there_4" class="checkbox" checked="checked" alt="Enable trip for Thursday"><label for="there_4">Thursday</label></li>                                                   
 									<li><input type="checkbox" name="there_5" id="there_5" class="checkbox" checked="checked" alt="Enable trip for Friday"><label for="there_5">Friday</label></li>                                                       
                                    <li><input type="checkbox" name="there_6" id="there_6" class="checkbox" alt="Enable trip for Saturday"><label for="there_6">Saturday</label></li>                                                                     
                                    <li><input type="checkbox" name="there_7" id="there_7" class="checkbox" alt="Enable trip for Sunday"><label for="there_7">Sunday</label></li>                                                                         
                                </ul>
                                <ul id="col2">
                                    <li class="first">
                                        <h3 class="th_time">Depart</h3>
                                        <select name="there_time_0" id="there_time_0" class="slim">
                                        <option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                    </li> 

                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_1" id="there_time_1" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1">7:00am</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_2" id="there_time_2" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1">7:00am</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_3" id="there_time_3" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1">7:00am</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_4" id="there_time_4" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1">7:00am</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_5" id="there_time_5" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1">7:00am</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_6" id="there_time_6" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1" style="color: rgb(187, 187, 187);">7:00am</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="there_time_7" id="there_time_7" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am" selected="selected">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias1" style="color: rgb(187, 187, 187);">7:00am</p>
                                    </li>
                                                                    </ul>
                                <ul id="col3">
                                    <li class="first">
                                        <h3 class="th_time">Return</h3>
                                        <select name="back_time_0" id="back_time_0" class="slim">
                                        <option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_1" id="back_time_1" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2">5:00pm</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_2" id="back_time_2" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2">5:00pm</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_3" id="back_time_3" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2">5:00pm</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_4" id="back_time_4" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2">5:00pm</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_5" id="back_time_5" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2">5:00pm</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_6" id="back_time_6" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2" style="color: rgb(187, 187, 187);">5:00pm</p>
                                    </li>
                                                                                                                                                                                                                                                                                                                                                                        <li>
                                        <select name="back_time_7" id="back_time_7" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00am">12:00am</option><option value="12:15am">12:15am</option><option value="12:30am">12:30am</option><option value="12:45am">12:45am</option><option value="1:00am">1:00am</option><option value="1:15am">1:15am</option><option value="1:30am">1:30am</option><option value="1:45am">1:45am</option><option value="2:00am">2:00am</option><option value="2:15am">2:15am</option><option value="2:30am">2:30am</option><option value="2:45am">2:45am</option><option value="3:00am">3:00am</option><option value="3:15am">3:15am</option><option value="3:30am">3:30am</option><option value="3:45am">3:45am</option><option value="4:00am">4:00am</option><option value="4:15am">4:15am</option><option value="4:30am">4:30am</option><option value="4:45am">4:45am</option><option value="5:00am">5:00am</option><option value="5:15am">5:15am</option><option value="5:30am">5:30am</option><option value="5:45am">5:45am</option><option value="6:00am">6:00am</option><option value="6:15am">6:15am</option><option value="6:30am">6:30am</option><option value="6:45am">6:45am</option><option value="7:00am">7:00am</option><option value="7:15am">7:15am</option><option value="7:30am">7:30am</option><option value="7:45am">7:45am</option><option value="8:00am">8:00am</option><option value="8:15am">8:15am</option><option value="8:30am">8:30am</option><option value="8:45am">8:45am</option><option value="9:00am">9:00am</option><option value="9:15am">9:15am</option><option value="9:30am">9:30am</option><option value="9:45am">9:45am</option><option value="10:00am">10:00am</option><option value="10:15am">10:15am</option><option value="10:30am">10:30am</option><option value="10:45am">10:45am</option><option value="11:00am">11:00am</option><option value="11:15am">11:15am</option><option value="11:30am">11:30am</option><option value="11:45am">11:45am</option><option value="12:00pm">12:00pm</option><option value="12:15pm">12:15pm</option><option value="12:30pm">12:30pm</option><option value="12:45pm">12:45pm</option><option value="1:00pm">1:00pm</option><option value="1:15pm">1:15pm</option><option value="1:30pm">1:30pm</option><option value="1:45pm">1:45pm</option><option value="2:00pm">2:00pm</option><option value="2:15pm">2:15pm</option><option value="2:30pm">2:30pm</option><option value="2:45pm">2:45pm</option><option value="3:00pm">3:00pm</option><option value="3:15pm">3:15pm</option><option value="3:30pm">3:30pm</option><option value="3:45pm">3:45pm</option><option value="4:00pm">4:00pm</option><option value="4:15pm">4:15pm</option><option value="4:30pm">4:30pm</option><option value="4:45pm">4:45pm</option><option value="5:00pm" selected="selected">5:00pm</option><option value="5:15pm">5:15pm</option><option value="5:30pm">5:30pm</option><option value="5:45pm">5:45pm</option><option value="6:00pm">6:00pm</option><option value="6:15pm">6:15pm</option><option value="6:30pm">6:30pm</option><option value="6:45pm">6:45pm</option><option value="7:00pm">7:00pm</option><option value="7:15pm">7:15pm</option><option value="7:30pm">7:30pm</option><option value="7:45pm">7:45pm</option><option value="8:00pm">8:00pm</option><option value="8:15pm">8:15pm</option><option value="8:30pm">8:30pm</option><option value="8:45pm">8:45pm</option><option value="9:00pm">9:00pm</option><option value="9:15pm">9:15pm</option><option value="9:30pm">9:30pm</option><option value="9:45pm">9:45pm</option><option value="10:00pm">10:00pm</option><option value="10:15pm">10:15pm</option><option value="10:30pm">10:30pm</option><option value="10:45pm">10:45pm</option><option value="11:00pm">11:00pm</option><option value="11:15pm">11:15pm</option><option value="11:30pm">11:30pm</option><option value="11:45pm">11:45pm</option></select>
                                        <p class="time-alias2" style="color: rgb(187, 187, 187);">5:00pm</p>
                                    </li>
                                </ul>
                                <ul id="col4">
                                    <li class="first">
                                        <div id="flex">
                                            <div id="flexheader">
                                                <span>Firm</span> <span class="last">Flexible</span>
                                            </div>
                                            <div class="pillbuttons">
	                                            <div class="first">0min</div><div class="chosen">10-15min</div><div class="last">30min+</div>
	                                            <input type="hidden" id="flex_global" name="flex_global" value="10-15min">
	                                        </div>
                                        </div>
                                    </li>
                                    <li><a class="edit" href="javascript:void(0)" id="edit_times_1" style="visibility: visible;">edit</a></li>
                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_2" style="visibility: visible;">edit</a></li>
                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_3" style="visibility: visible;">edit</a></li>
                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_4" style="visibility: visible;">edit</a></li>
                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_5" style="visibility: visible;">edit</a></li>
                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_6" style="visibility: hidden;">edit</a></li>
                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_7" style="visibility: hidden;">edit</a></li>
                                 </ul>
                            </div>
                        </div>

                    </dd>
                    <dd class="changestep">
                    	<span class="errormsg" style="opacity: 0.5;">Hold on! Are you a driver or a passenger?</span> 
                    	<a href="javascript:void(0)" class="back">
                    	<img alt="Back Arrow" src="http://www.zimride.com/assets/icon_back.png"> back</a> 
                    	<button id="addride-whattime-button" type="button" class="confirm float_right next">
                    	<span>Next Step</span> 
                    	<img src="http://www.zimride.com/assets/forward-arrow-white.png"></button>
                    </dd>
               </dl>
            </fieldset>
            
            <fieldset id="step_3" class="" style="">
                <dl>
                    <dt><label for="cost">How much do you want each passenger to contribute?</label></dt>
                    <dd>
                        <span id="dollarsign">$</span> <input type="text" name="cost" id="cost" class="slim align_right" maxlength="6" value="0">
                        <div class="input-arrows">
                            <a href="javascript:void(0)" class="increment"><img src="/TicketSchedule/Picture/increment.png"></a>
                            <a href="javascript:void(0)" class="decrement"><img src="/TicketSchedule/Picture/decrement.png"></a>
                        </div>
                        <span id="price-per">each way</span>
                        <p>suggested price of $<span id="suggested_price">75.00</span> <span id="suggested_price_text">based on the Zimride average for this trip</span></p>
                    </dd>
                    <dt class="seats-visibility"><label for="seats">Number of available seats</label></dt>
                    <dd class="seats-visibility">
                        <input type="text" name="seats" id="seats" class="slim align_right" maxlength="1" value="2">
                        <div class="input-arrows">
                            <a href="javascript:void(0)" class="increment"><img src="/TicketSchedule/Picture/increment.png"></a>
                            <a href="javascript:void(0)" class="decrement"><img src="/TicketSchedule/Picture/decrement.png"></a>
                        </div>
                    </dd>
                    <dt><label for="notes">Notes</label></dt>
                    <dd><textarea id="notes" cols="30" rows="3" name="notes" class="clickaway badWord" placeholder="Any other details..." style="color: rgb(153, 153, 153);">Any other details...</textarea></dd>
                    <dd class="changestep"><span class="errormsg" style="opacity: 0;"></span> 
                            <a href="javascript:void(0)" class="back"><img alt="back_icon" src="http://www.zimride.com/assets/icon_back.png"> back</a>
                    <span id="form_submit"><button type="submit" id="form-addride-button" class="clickaway_confirm confirm  requires_login">Post Ride</button></span><br>
                   </dd>
                </dl>
            </fieldset>
			</form>
		</div>
	</div>
</div>
</body>
</html>