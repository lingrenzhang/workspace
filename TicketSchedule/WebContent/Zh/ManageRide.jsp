<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page import="com.hitchride.standardClass.User" %>
<% 
	String queryString = request.getQueryString();
	int rid = 0;
	if (queryString!=null)
	{
	 rid = Integer.parseInt(queryString.split("=")[1]);
	}
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
%>
    
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/manageride.css" type="text/css" rel="stylesheet">

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<!--
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyBtajlUONtd9R9vdowDwwrc-ul6NarmtiE&sensor=false&libraries=places">
</script>
-->

<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=Mto5Y3Pq2fgwkY2Kt9n60bWl"></script>
<script>
var map;
var origLat,origLng,destLat,destLng;
var origAddr,destAddr;
function asPassenger()
{
	document.getElementById("asPassenger").setAttribute("class", "active");
	document.getElementById("asDriver").setAttribute("class", "");
	document.getElementById("userType").setAttribute("value", "passenger");
	$(".cost-visibility").children("label").text("How much are you willing to contribute?");
	$(".cost-visibility").fadeIn(1000);
	$(".seats-visibility").fadeOut(1000);
}

function asDriver()
{
	document.getElementById("asPassenger").setAttribute("class", "");
	document.getElementById("asDriver").setAttribute("class", "active");
	document.getElementById("userType").setAttribute("value", "driver");
	$(".cost-visibility").children("label").text("How much do you want each passenger to contribute?");
	$(".cost-visibility").fadeIn(1000);
	$(".seats-visibility").fadeIn(1000);
}

function asTransient()
{
	document.getElementById("asTransient").setAttribute("class", "active");
	document.getElementById("asCommute").setAttribute("class", "");
	document.getElementById("commuteType").setAttribute("value", "Transient");
	$("#there_repeating").fadeOut(1000,function(){
		$("#multipostwrapper").fadeIn(1000);
		});
}

function asCommute()
{
	if (document.getElementById("IsLogin").value == "true")
	{
		document.getElementById("asCommute").setAttribute("class", "active");
		document.getElementById("asTransient").setAttribute("class", "");
		document.getElementById("commuteType").setAttribute("value", "Commute");
		$("#multipostwrapper").fadeOut(1000,function(){
				$("#there_repeating").fadeIn(1000);
		});
		
		var dayofweek=0;
		for(var i=1;i<8;i++)
		{
			var cflag = document.getElementById("there_"+i).checked;
			if (cflag==true)
			{
				dayofweek = dayofweek*10+i;
			}
		}
		document.getElementById("dayofweek").value=dayofweek;
	}
	else
	{
		window.location.href="/TicketSchedule/Login.jsp";
	}
}

$(document).ready(function(){
	//------------------------register listener-------------------------
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

	$(".increment").click(function(){
		var value=parseInt(this.parentNode.previousElementSibling.value)+1;
		this.parentNode.previousElementSibling.value=value;
	});

	$(".decrement").click(function(){
		var value=parseInt(this.parentNode.previousElementSibling.value)-1;
		if (value>=0)
		{
			this.parentNode.previousElementSibling.value=value;
		}
	});
	
	
	$(".trip_close").click(function(){
		$(this.parentNode).fadeOut("slow").remove();
		var numtrip=parseInt($("#num_trips").val())-1;
		$("#num_trips").val(numtrip);
		if (numtrip>1)
		{
			$("#multi_trips").val("1");
		}
		else
		{
			$("#multi_trips").val("0");
			$(".trip_num").css("visibility","hidden");
			$(".trip_close").css("visibility","hidden");
		}
		
		var a=$(".singletripwrapper");
		if (numtrip>1)
		{
			
			for (var i=1;i<=numtrip;i++)
			{
				$(a[i-1]).children(".trip_num").text("Trip "+i);
				$(a[i-1]).find(".there").attr("id","there_trip"+i);
				$(a[i-1]).find(".there").attr("name","there_trip"+i);
				$(a[i-1]).find(".depart-date").attr("for","depart-date-trip"+i);
				//To add
			}
			
		}
	});

	$("#add_multi").click(function(){
		var numtrip=parseInt($("#num_trips").val())+1;
		$("#num_trips").val(numtrip);
		if (numtrip>1)
		{
			$("#multi_trips").val("1");
		}
		else
		{
			$("#multi_trips").val("0");
		}
		
		$(".singletripwrapper:last").clone(true).insertAfter(".singletripwrapper:last");
		$(".trip_num").css("visibility","visible");
		$(".trip_close").css("visibility","visible");
		var a=$(".singletripwrapper");
		if (numtrip>1)
		{
			for (var i=1;i<=numtrip;i++)
			{
				$(a[i-1]).children(".trip_num").text("Trip "+i);
				$(a[i-1]).find(".there").attr("id","there_trip"+i);
				$(a[i-1]).find(".there").attr("name","there_trip"+i);
				$(a[i-1]).find(".depart-date").attr("for","depart-date-trip"+i);
			}
			
		}
	});
	
	$("#there_time_0").change(function(){
		document.getElementById("there_time_1").value = this.value;
		document.getElementById("there_time_2").value = this.value;
		document.getElementById("there_time_3").value = this.value;
		document.getElementById("there_time_4").value = this.value;
		document.getElementById("there_time_5").value = this.value;
		document.getElementById("there_time_6").value = this.value;
		document.getElementById("there_time_7").value = this.value;
		$("p.time-alias1").text(this.value);
	});

	$("#back_time_0").change(function(){
		document.getElementById("back_time_1").value = this.value;
		document.getElementById("back_time_2").value = this.value;
		document.getElementById("back_time_3").value = this.value;
		document.getElementById("back_time_4").value = this.value;
		document.getElementById("back_time_5").value = this.value;
		document.getElementById("back_time_6").value = this.value;
		document.getElementById("back_time_7").value = this.value;
		
		$("p.time-alias2").text(this.value);
	});


	$(".pillbuttons").children("div").click(function(){
		switch ($(this).text())
		{
			case "10min":
				this.className="first chosen";
				this.parentNode.children[1].className="";
				this.parentNode.children[2].className="last";
				this.parentNode.children[3].value="10";
				break;
			case "20min":
				this.parentNode.children[0].className="first";
				this.className="chosen";
				this.parentNode.children[2].className="last";
				this.parentNode.children[3].value="20";
				break;
			case "30min":
				this.parentNode.children[0].className="first";
				this.parentNode.children[1].className="";
				this.className="last chosen";
				this.parentNode.children[3].value="30";
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
					document.getElementById("there_time_1").value=$("ul#col2").children("li:eq(1)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(1)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(1)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_1").value=$("ul#col3").children("li:eq(1)").children("p")[0].innerHTML;
					break;
				case "2":
					$("ul#col2").children("li:eq(2)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(2)").children("select")[0].className="slim hidden";
					document.getElementById("there_time_2").value=$("ul#col2").children("li:eq(2)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(2)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(2)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_2").value=$("ul#col3").children("li:eq(2)").children("p")[0].innerHTML;
					break;
				case "3":
					$("ul#col2").children("li:eq(3)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(3)").children("select")[0].className="slim hidden";
					document.getElementById("there_time_3").value=$("ul#col2").children("li:eq(3)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(3)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(3)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_3").value=$("ul#col3").children("li:eq(3)").children("p")[0].innerHTML;
					break;
				case "4":
					$("ul#col2").children("li:eq(4)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(4)").children("select")[0].className="slim hidden";
					document.getElementById("there_time_4").value=$("ul#col2").children("li:eq(4)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(4)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(4)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_4").value=$("ul#col3").children("li:eq(4)").children("p")[0].innerHTML;
					break;
				case "5":
					$("ul#col2").children("li:eq(5)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(5)").children("select")[0].className="slim hidden";
					document.getElementById("there_time_5").value=$("ul#col2").children("li:eq(5)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(5)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(5)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_5").value=$("ul#col3").children("li:eq(5)").children("p")[0].innerHTML;
					break;
				case "6":
					$("ul#col2").children("li:eq(6)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(6)").children("select")[0].className="slim hidden";
					document.getElementById("there_time_6").value=$("ul#col2").children("li:eq(6)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(6)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(6)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_6").value=$("ul#col3").children("li:eq(6)").children("p")[0].innerHTML;
					break;
				case "7":
					$("ul#col2").children("li:eq(7)").children("p")[0].className="time-alias1";
					$("ul#col2").children("li:eq(7)").children("select")[0].className="slim hidden";
					document.getElementById("there_time_7").value=$("ul#col2").children("li:eq(7)").children("p")[0].innerHTML;
					$("ul#col3").children("li:eq(7)").children("p")[0].className="time-alias2";
					$("ul#col3").children("li:eq(7)").children("select")[0].className="slim hidden";
					document.getElementById("back_time_7").value=$("ul#col3").children("li:eq(7)").children("p")[0].innerHTML;
					break;
			}
		}
	});
	
	$(".checkbox").change(function(){
		var dayofweek=0;
		for(var i=1;i<8;i++)
		{
			var cflag = document.getElementById("there_"+i).checked;
			if (cflag==true)
			{
				dayofweek = dayofweek*10+i;
			}
		}
		document.getElementById("dayofweek").value=dayofweek;
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
	//------------------------register listener end-----------------------------
	
	initizlieMap();
	//-----------------------Initialize value
	var rid = <%=rid%>;
	if (rid!=0)
	{
		loadValue(rid);
	}
});
</script>

<script>
	function initizlieMap()
	{
		
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
		
		
		var geol;		
		//var nowLat=31.271998;
		//var nowLng=121.542146;
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
		

		map = new BMap.Map("post-map-canvas");
		var point = new BMap.Point(nowLng,nowLat);
		map.addControl(new BMap.NavigationControl());    
		map.addControl(new BMap.ScaleControl());
		map.centerAndZoom(point,15);

		var searchBoxO = new BMap.Autocomplete(
				{"input" : "s",
				 "location" : map});
		var searchBoxD = new BMap.Autocomplete(
				{"input" : "e",
				 "location" : map});
		
		var omarker;
		var dmarker;
		var basicbounds = new BMap.Bounds();
		
		
		
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
			    document.getElementById("dtime").value =response.rows[0].elements[0].duration.value;
			   }
			}
	};
</script>
<script>
function refitb(bounds)
{
	 var range = Math.max(bounds.toSpan().lat,bounds.toSpan().lng);
	 var zoomNum = Math.floor(9-Math.log(range)/Math.log(2));
	 map.setCenter(bounds.getCenter());
	 map.setZoom(zoomNum);
}
</script>
<script>
	function loadValue(rid)
	{
		var rideinfo = JSON.parse(getJson("/TicketSchedule/servlet/ManageRide?rid="+rid));
		document.getElementById("rid").value=rid;
		document.getElementById("s").setAttribute("value", rideinfo.origLoc._formatedAddr);
		document.getElementById("e").setAttribute("value", rideinfo.destLoc._formatedAddr);
		document.getElementById("origLat").value=rideinfo.origLoc._lat;
		document.getElementById("origLng").value=rideinfo.origLoc._lon;
		document.getElementById("destLat").value=rideinfo.destLoc._lat;
		document.getElementById("destLng").value=rideinfo.destLoc._lon;
		document.getElementById("distance").value=rideinfo.dist;
		document.getElementById("dtime").value=rideinfo.dura;
		if (rideinfo.schedule._isCommute==true)
		{
			asCommute();
			for (var i=0;i<7;i++)
			{
				var k=(i==0)?7:i;
				if (rideinfo.schedule._dayOfWeek[i])
				{
					document.getElementById("there_"+k).checked = true;
					$("#there_"+k).change();
					document.getElementById("there_time_"+k).value=rideinfo.schedule.cftime[i];
					document.getElementById("there_time_"+k).nextElementSibling.innerHTML=rideinfo.schedule.cftime[i];
					document.getElementById("back_time_"+k).value=rideinfo.schedule.cbtime[i];
					document.getElementById("back_time_"+k).nextElementSibling.innerHTML=rideinfo.schedule.cbtime[i];
					var dayofweek = document.getElementById("dayofweek").value;
					dayofweek=10*dayofweek+k;
					document.getElementById("dayofweek").value=dayofweek;
				}
				else
				{
					document.getElementById("there_"+k).checked = false;
					$("#there_"+k).change();
				}
			}
		}
		else
		{
			asTransient();
			document.getElementById("depart-date").value = rideinfo.schedule.tripDate;
			//document.getElementById("there_time").value = rideinfo.schedule.tripDate;
		}
		if (rideinfo.userType==true)
		{
			asPassenger();
		}
		else
		{
			asDriver();
		}
	}
</script>
<title>管理行程</title>
</head>
<body>
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
			      <li class="active"><a href="/TicketSchedule/Zh/ManageRide.jsp">管理行程</a></li>
			      <li><a href="/TicketSchedule/Zh/SearchRide.jsp">上下班拼车</a></li>
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
	<div id="content_container" class="clearfix">
		<div id="wide_column_left" class="newAddRideStyle">
			<form action="/TicketSchedule/servlet/ManageRide" method="Post" id="add_ride" class="standard requires_login_results" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
				<div class="panel" id="GeoInfo">
					<div class="panel-heading">地址信息</div>
					<div class="panel-body">
						<dl>
							<dt>出发地</dt>
							<dd>
	                        	<input type="text" class="ad_input" placeholder="张江高科" name="s" id="s" maxlength="400"  autocomplete="off" >
	                        </dd>
	                        <dt>目的地</dt>
	                        <dd>
								<input type="text" class="ad_input" placeholder="人民广场" name="e" id="e" maxlength="400" autocomplete="off">
							</dd>
						</dl>
					</div>
				</div>
				
				<div class="panel" id="Schedule" >
					<div class="panel-heading">日程</div>
					<div class="panel-body">
					<div class="tabbable tabs-top">
					    <ul class="nav nav-tabs">
					        <li id="asTransient" class="active"><a href="javascript: asTransient()">临时拼车</a></li>
					        <li id="asCommute"><a href="javascript: asCommute()">上下班拼车</a></li>
					    </ul>
					</div>
					<input id="commuteType" class="commuteType hidden" type="text" value="Transient" name="commuteType"></input>
                    <div class="schedule-info">
                    <dl>                
                       <dd class="triptabs" style="display: block;">
                   		 <input id="onetime-only" type="hidden" name="type" value="one-time">
		                   	<div id="multipostwrapper" style="">
			                        <div id="singletripwrapper" class="singletripwrapper">
			                        	<span class="trip_num">Trip 1</span>
			                        	<span class="float_right trip_close dot">X</span><br>
			                        	<div class="tripbox">
			                               <div id="there_one_time">
			                               		<input type="hidden" id="there_trip" name="there_trip" class="there" value="1">
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
					                                <option value="1:00 AM">1:00 AM</option>
					                                <option value="2:00 AM">2:00 AM</option>
					                                <option value="3:00 AM">3:00 AM</option>
					                                <option value="4:00 AM">4:00 AM</option>
					                                <option value="5:00 AM">5:00 AM</option>
					                                <option value="6:00 AM">6:00 AM</option>
					                                <option value="7:00 AM">7:00 AM</option>
					                                <option value="8:00 AM">8:00 AM</option>
					                                <option value="9:00 AM">9:00 AM</option>
					                                <option value="10:00 AM">10:00 AM</option>
					                                <option value="11:00 AM">11:00 AM</option>
					                                <option value="12:00 PM">noon</option>
					                                <option value="1:00 PM">1:00 PM</option>
					                                <option value="2:00 PM">2:00 PM</option>
					                                <option value="3:00 PM">3:00 PM</option>
					                                <option value="4:00 PM">4:00 PM</option>
					                                <option value="5:00 PM">5:00 PM</option>
					                                <option value="6:00 PM">6:00 PM</option>
					                                <option value="7:00 PM">7:00 PM</option>
					                                <option value="8:00 PM">8:00 PM</option>
					                                <option value="9:00 PM">9:00 PM</option>
					                                <option value="10:00 PM">10:00 PM</option>
					                                <option value="11:00 PM">11:00 PM</option>		                                
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
			                                    	<option value="1:00 AM">1:00 AM</option>
			                                    	<option value="2:00 AM">2:00 AM</option>
			                                    	<option value="3:00 AM">3:00 AM</option>
			                                    	<option value="4:00 AM">4:00 AM</option>
			                                    	<option value="5:00 AM">5:00 AM</option>
			                                    	<option value="6:00 AM">6:00 AM</option>
			                                    	<option value="7:00 AM">7:00 AM</option>
			                                    	<option value="8:00 AM">8:00 AM</option>
			                                    	<option value="9:00 AM">9:00 AM</option>
			                                    	<option value="10:00 AM">10:00 AM</option>
			                                    	<option value="11:00 AM">11:00 AM</option>
			                                    	<option value="12:00 PM">noon</option>
			                                    	<option value="1:00 PM">1:00 PM</option>
			                                    	<option value="2:00 PM">2:00 PM</option>
			                                    	<option value="3:00 PM">3:00 PM</option>
			                                    	<option value="4:00 PM">4:00 PM</option>
				                                    <option value="5:00 PM">5:00 PM</option>
				                                    <option value="6:00 PM">6:00 PM</option>
				                                    <option value="7:00 PM">7:00 PM</option>
				                                    <option value="8:00 PM">8:00 PM</option>
				                                    <option value="9:00 PM">9:00 PM</option>
				                                    <option value="10:00 PM">10:00 PM</option>
				                                    <option value="11:00 PM">11:00 PM</option>
				                                    </select>
					                            </div>
			                            	</div>
			                        	</div>
				                        <button id="add_multi" class="hidden" type="button"><b>+</b> 更多行程</button> 
				                        <span class="max_trips hidden">你最多一次只能添加五个行程</span>
			                        	<input type="hidden" name="multi_trips" id="multi_trips" value="0">
			                        	<input type="hidden" name="num_trips" id="num_trips" value="1">
		                     </div>

							<div id="there_repeating" style="display: none;">
		                            <div id="commute-table">
										<ul id="col1">
		                                    <li class="first"><p>选择日期</p></li>
		                                    <li><input type="checkbox" name="there_1" id="there_1" class="checkbox" checked alt="Enable trip for Monday"><label for="there_1">Monday</label></li>
		                                    <li><input type="checkbox" name="there_2" id="there_2" class="checkbox" checked alt="Enable trip for Tuesday"><label for="there_2">Tuesday</label></li>                                                     
		                          			<li><input type="checkbox" name="there_3" id="there_3" class="checkbox" checked alt="Enable trip for Wednesday"><label for="there_3">Wednesday</label></li>                                                 
		  									<li><input type="checkbox" name="there_4" id="there_4" class="checkbox" checked alt="Enable trip for Thursday"><label for="there_4">Thursday</label></li>                                                   
		 									<li><input type="checkbox" name="there_5" id="there_5" class="checkbox" checked alt="Enable trip for Friday"><label for="there_5">Friday</label></li>                                                       
		                                    <li><input type="checkbox" name="there_6" id="there_6" class="checkbox" alt="Enable trip for Saturday"><label for="there_6">Saturday</label></li>                                                                     
		                                    <li><input type="checkbox" name="there_7" id="there_7" class="checkbox" alt="Enable trip for Sunday"><label for="there_7">Sunday</label></li>                                                                         
		                                </ul>
		                                <ul id="col2">
		                                    <li class="first">
		                                        <h3 class="th_time">Depart</h3>
		                                        <select name="there_time_0" id="there_time_0" class="slim">
		                                        <option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                    </li> 
		
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_1" id="there_time_1" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1">7:00 AM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_2" id="there_time_2" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1">7:00 AM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_3" id="there_time_3" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1">7:00 AM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_4" id="there_time_4" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1">7:00 AM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_5" id="there_time_5" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1">7:00 AM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_6" id="there_time_6" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1" style="color: rgb(187, 187, 187);">7:00 AM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="there_time_7" id="there_time_7" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM" selected="selected">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias1" style="color: rgb(187, 187, 187);">7:00 AM</p>
		                                    </li>
		                                </ul>
		                                <ul id="col3">
		                                    <li class="first">
		                                        <h3 class="th_time">Return</h3>
		                                        <select name="back_time_0" id="back_time_0" class="slim">
		                                        <option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_1" id="back_time_1" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2">5:00 PM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_2" id="back_time_2" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2">5:00 PM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_3" id="back_time_3" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2">5:00 PM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_4" id="back_time_4" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2">5:00 PM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_5" id="back_time_5" class="slim hidden"><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2">5:00 PM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_6" id="back_time_6" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2" style="color: rgb(187, 187, 187);">5:00 PM</p>
		                                    </li>
		                                                                                                                                                                                                                                                                                                                                                                        <li>
		                                        <select name="back_time_7" id="back_time_7" class="slim hidden" disabled=""><option value="No Trip">No Trip</option><option value="12:00 AM">12:00 AM</option><option value="12:15 AM">12:15 AM</option><option value="12:30 AM">12:30 AM</option><option value="12:45 AM">12:45 AM</option><option value="1:00 AM">1:00 AM</option><option value="1:15 AM">1:15 AM</option><option value="1:30 AM">1:30 AM</option><option value="1:45 AM">1:45 AM</option><option value="2:00 AM">2:00 AM</option><option value="2:15 AM">2:15 AM</option><option value="2:30 AM">2:30 AM</option><option value="2:45 AM">2:45 AM</option><option value="3:00 AM">3:00 AM</option><option value="3:15 AM">3:15 AM</option><option value="3:30 AM">3:30 AM</option><option value="3:45 AM">3:45 AM</option><option value="4:00 AM">4:00 AM</option><option value="4:15 AM">4:15 AM</option><option value="4:30 AM">4:30 AM</option><option value="4:45 AM">4:45 AM</option><option value="5:00 AM">5:00 AM</option><option value="5:15 AM">5:15 AM</option><option value="5:30 AM">5:30 AM</option><option value="5:45 AM">5:45 AM</option><option value="6:00 AM">6:00 AM</option><option value="6:15 AM">6:15 AM</option><option value="6:30 AM">6:30 AM</option><option value="6:45 AM">6:45 AM</option><option value="7:00 AM">7:00 AM</option><option value="7:15 AM">7:15 AM</option><option value="7:30 AM">7:30 AM</option><option value="7:45 AM">7:45 AM</option><option value="8:00 AM">8:00 AM</option><option value="8:15 AM">8:15 AM</option><option value="8:30 AM">8:30 AM</option><option value="8:45 AM">8:45 AM</option><option value="9:00 AM">9:00 AM</option><option value="9:15 AM">9:15 AM</option><option value="9:30 AM">9:30 AM</option><option value="9:45 AM">9:45 AM</option><option value="10:00 AM">10:00 AM</option><option value="10:15 AM">10:15 AM</option><option value="10:30 AM">10:30 AM</option><option value="10:45 AM">10:45 AM</option><option value="11:00 AM">11:00 AM</option><option value="11:15 AM">11:15 AM</option><option value="11:30 AM">11:30 AM</option><option value="11:45 AM">11:45 AM</option><option value="12:00 PM">12:00 PM</option><option value="12:15 PM">12:15 PM</option><option value="12:30 PM">12:30 PM</option><option value="12:45 PM">12:45 PM</option><option value="1:00 PM">1:00 PM</option><option value="1:15 PM">1:15 PM</option><option value="1:30 PM">1:30 PM</option><option value="1:45 PM">1:45 PM</option><option value="2:00 PM">2:00 PM</option><option value="2:15 PM">2:15 PM</option><option value="2:30 PM">2:30 PM</option><option value="2:45 PM">2:45 PM</option><option value="3:00 PM">3:00 PM</option><option value="3:15 PM">3:15 PM</option><option value="3:30 PM">3:30 PM</option><option value="3:45 PM">3:45 PM</option><option value="4:00 PM">4:00 PM</option><option value="4:15 PM">4:15 PM</option><option value="4:30 PM">4:30 PM</option><option value="4:45 PM">4:45 PM</option><option value="5:00 PM" selected="selected">5:00 PM</option><option value="5:15 PM">5:15 PM</option><option value="5:30 PM">5:30 PM</option><option value="5:45 PM">5:45 PM</option><option value="6:00 PM">6:00 PM</option><option value="6:15 PM">6:15 PM</option><option value="6:30 PM">6:30 PM</option><option value="6:45 PM">6:45 PM</option><option value="7:00 PM">7:00 PM</option><option value="7:15 PM">7:15 PM</option><option value="7:30 PM">7:30 PM</option><option value="7:45 PM">7:45 PM</option><option value="8:00 PM">8:00 PM</option><option value="8:15 PM">8:15 PM</option><option value="8:30 PM">8:30 PM</option><option value="8:45 PM">8:45 PM</option><option value="9:00 PM">9:00 PM</option><option value="9:15 PM">9:15 PM</option><option value="9:30 PM">9:30 PM</option><option value="9:45 PM">9:45 PM</option><option value="10:00 PM">10:00 PM</option><option value="10:15 PM">10:15 PM</option><option value="10:30 PM">10:30 PM</option><option value="10:45 PM">10:45 PM</option><option value="11:00 PM">11:00 PM</option><option value="11:15 PM">11:15 PM</option><option value="11:30 PM">11:30 PM</option><option value="11:45 PM">11:45 PM</option></select>
		                                        <p class="time-alias2" style="color: rgb(187, 187, 187);">5:00 PM</p>
		                                    </li>
		                                </ul>
		                                <ul id="col4">
		                                    <li class="first">
		                                        <div id="flex">
		                                            <div id="flexheader">
		                                                <span>Firm</span> <span class="last">Flexible</span>
		                                            </div>
		                                            <div class="pillbuttons">
			                                            <div class="first">10min</div><div class="chosen">20min</div><div class="last">30min</div>
			                                            <input type="hidden" id="flex_global" name="flex_global" value="20">
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
					</dl>
					</div>
				</div>
			</div>
 	            <div class="panel" id="UserType">
	                <div class="panel-heading">交易信息</div>
					<div class="panel-body">
		            	<div class="tabbable tabs-top">
		  					<ul class="nav nav-tabs">
		  						<li class="active" id="asDriver"><a href="javascript: asDriver()">有车</a></li>
		  						<li id="asPassenger"><a href="javascript: asPassenger()">乘客</a></li>
							</ul>
						</div>
	               		<input class="usertype hidden" type="text" name="userType" id="userType" value="driver">
	                	<div class="bargin-info">
		                    <div class="cost-visibility" style="display:"><label for="cost">希望每个乘客付多少钱？</label></div>
			                <div class="cost-visibility" style="display:">
			                     <span id="dollarsign">$</span> <input type="text" name="cost" id="cost" class="slim align_right" maxlength="6" value="0">
			                     <div class="input-arrows">
			                         <a href="javascript:void(0)" class="increment"><img src="/TicketSchedule/Picture/increment.png"></a>
			                         <a href="javascript:void(0)" class="decrement"><img src="/TicketSchedule/Picture/decrement.png"></a>
			                     </div>
			                     <span id="price-per">单程</span>
			                </div>
				            <div class="suggest">
				                <p>根据距离，建议 拼车费为 <span id="suggested_price">10</span>元<span id="suggested_price_text"></span></p>
			                </div>
			                 
			                 <div class="seats-visibility" style="display: "><label for="seats">空座数量</label></div>
			                 <div class="seats-visibility" style="display: ">
				                 <input type="text" name="seats" id="seats" class="slim align_right" maxlength="1" value="2">
				                 <div class="input-arrows">
				                       <a href="javascript:void(0)" class="increment"><img src="/TicketSchedule/Picture/increment.png"></a>
				                       <a href="javascript:void(0)" class="decrement"><img src="/TicketSchedule/Picture/decrement.png"></a>
				                 </div>
			            	</div>
				            <div class="note_wrapper">
				            	<label for="notes">备注</label>
	     	                	<textarea id="notes" cols="30" rows="3" name="notes" class="clickaway badWord" placeholder="Any other details..." style="color: rgb(153, 153, 153);">Any other details...</textarea>
	     	                </div>
			              </div>
					</div>	
				</div> 
			<span id="form_submit"><button type="submit" class="btn btn-primary">Create or Update Ride</button></span>          
            <input type="text" class="hidden" id="origLat" name="origLat" value=""></input>
            <input type="text" class="hidden" id="origLng" name="origLng" value=""></input>
            <input type="text" class="hidden" id="destLat" name="destLat" value=""></input>
            <input type="text" class="hidden" id="destLng" name="destLng" value=""></input>
            <input type="text" class="hidden" id="distance" name="distance" value=""></input>
            <input type="text" class="hidden" id="dtime" name="dtime" value=""></input>
            <input type="text" class="hidden" id="isPost" name="isPost" value="true"></input>
            <input type="text" class="hidden" id="dayofweek" name="dayofweek" value="0"></input>
            <input type="text" class="hidden" id="rid" name="rid" value="0"></input>

			</form>
		</div>
		<div id="post-map-canvas">
		</div>
	</div>
</div>
<input type="text" class="hidden" id="IsLogin" value='<%=(IsLogin == null) ? "false" : IsLogin%>'></input>
</body>
</html>