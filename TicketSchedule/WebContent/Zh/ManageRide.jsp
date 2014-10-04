<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page import="com.hitchride.User" %>
<% 
	String queryString = request.getQueryString();
	int rid = 0;
	if (queryString!=null)
	{
	 rid = Integer.parseInt(queryString.split("=")[1]);
	}
	User user= (User) request.getSession().getAttribute("user");
	String IsLogin = "true";
	if (user== null)
	{
		 user = new User();
		 user.set_authLevel(1);
		 user.set_name("guest");
		 user.set_emailAddress("guest");
		 user.set_avatarID("default.jpg");
		 user.set_userLevel(0);
		 IsLogin="false";
	}
%>
    
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/TicketSchedule/bootstrap/css/bootstrap.css">
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/manageride.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/custom_jqueryui.css" type="text/css" rel="stylesheet">

<link rel="shortcut icon" href="/TicketSchedule/favicon.ico" type="image/x-icon" /> 

<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/site.js"></script>
<script src="/TicketSchedule/JS/calandar.js"></script>
<script src="/TicketSchedule/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=Mto5Y3Pq2fgwkY2Kt9n60bWl"></script>
<script>
var map;
var origLat,origLng,destLat,destLng;
var origAddr,destAddr;

function asDriver()
{
	document.getElementById("asDriver").setAttribute("class","active");
	document.getElementById("asPassenger").setAttribute("class","");
	document.getElementById("price-content").style.display="inline";
	userType = false;
}

function asPassenger()
{
	document.getElementById("asDriver").setAttribute("class","");
	document.getElementById("asPassenger").setAttribute("class","active");
	document.getElementById("price-content").style.display="none";
	userType = true;
}

function asTransient()
{
	document.getElementById("asTransient").setAttribute("class", "active");
	document.getElementById("asCommute").setAttribute("class", "");
	document.getElementById("commuteType").setAttribute("value", "transient");
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
		document.getElementById("commuteType").setAttribute("value", "commute");
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
	
	
	var selectorholder1 = document.getElementById("ddate");
	var pickerholder1 = document.getElementById("ui-datepicker-div");

	var calandar = new Calandar(selectorholder1,pickerholder1,"depart-date","map-canvas");
	date= calandar.getDate();
	
	var timer_holder = document.getElementById("time-info");
	timePicker = new TimePicker(timer_holder,"depart_time","/TicketSchedule/Picture/clock.jpg");
	timePicker.setDefaultTime();
	
	var selectorholder2 = document.getElementById("bdate");
	var pickerholder2 = document.getElementById("ui-datepicker-div");
	
	var calandar2 = new Calandar(selectorholder2,pickerholder2,"back-date","map-canvas");
	date= calandar2.getDate();
	
	var timer_holder2 = document.getElementById("time-info2");
	timePicker2 = new TimePicker(timer_holder2,"back_time","/TicketSchedule/Picture/clock.jpg");
	timePicker2.setDefaultTime();
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
				$(a[i-1]).find(".there").attr("id","depart_time"+i);
				$(a[i-1]).find(".there").attr("name","depart_time"+i);
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
				$(a[i-1]).find(".there").attr("id","depart_time"+i);
				$(a[i-1]).find(".there").attr("name","depart_time"+i);
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
		if ($(this).text()=="编辑")
		{
			$(this).text("取消");
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
			$(this).text("编辑");
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
				
			distance = Math.floor(Math.sqrt(dx * dx + dy * dy)*1.1/1000);
			duration = distance/30; //Will use better algorithm if necessary
			
			document.getElementById("distance").setAttribute("value",distance);
			document.getElementById("duration").setAttribute("value",duration);
			   
			var price = distance;
			document.getElementById("price").setAttribute("value",price);
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
		var rideinfo = JSON.parse(getJson("/TicketSchedule/PublishRide?rid="+rid));
		document.getElementById("rid").value=rid;
		document.getElementById("s").value= rideinfo.origLoc_addr;//Seems have compatible issue with baidu api.
		document.getElementById("defaultS").value= rideinfo.origLoc_addr;
		document.getElementById("s").setAttribute("placeholder", rideinfo.origLoc_addr);
		document.getElementById("e").value= rideinfo.destLoc_addr; //Seems have compatible issue with baidu api.
		document.getElementById("defaultE").value= rideinfo.destLoc_addr;
		document.getElementById("e").setAttribute("placeholder", rideinfo.destLoc_addr);
		document.getElementById("origLat").value=rideinfo.origLoc_lat;
		document.getElementById("origLng").value=rideinfo.origLoc_lon;
		document.getElementById("destLat").value=rideinfo.destLoc_lat;
		document.getElementById("destLng").value=rideinfo.destLoc_lon;
		document.getElementById("price").value =rideinfo.price;
		document.getElementById("seats").value = rideinfo.totalSeats;
		document.getElementById("distance").value=Math.floor(rideinfo.dist/1000);
		document.getElementById("duration").value=rideinfo.dura;
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

<script>
function subform()
{
    //Input check
	return true;
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
			      <li><a href="/TicketSchedule/Zh/SearchCommuteTopic.jsp">上下班拼车</a></li>
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
			<form action="/TicketSchedule/PublishRide" method="Post" id="add_ride" class="standard requires_login_results" onsubmit="return subform()" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
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
                    	<dl><dd>
		                   	<div id="multipostwrapper" style="">
	                   	   		 <div class="date-info">
		                           		<label for="depart-date" class="depart-date">出发</label>
		                           		<div id="ddate">
		                           			<input type="text" name="depart-date" id="depart-date" readonly="readonly" class="slim datepicker depart-date hasDatepicker" maxlength="10" value="07/14/2013">
				                            <img class="ui-datepicker-trigger" src="/TicketSchedule/Picture/icon_calendar.png" for="depart-date" alt="..." title="...">
			                            </div>
			                     </div>
	   							 <div id="time-info">				
								 </div>
								 <div class="date-info2">
		                           		<label for="back-date" class="back-date">返回</label>
		                           		<div id="bdate">
			                            	<input type="text" name="back-date" id="back-date" readonly="readonly" class="slim datepicker depart-date hasDatepicker" maxlength="10" value="07/14/2013">
			                            	<img class="ui-datepicker-trigger" src="/TicketSchedule/Picture/icon_calendar.png" for="depart-date" alt="..." title="...">
	   									</div>
	   							 </div>
	   							 <div id="time-info2">				
								 </div>
								 <div id="ui-datepicker-div" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"
 										style="position: absolute; z-index:1;display:none;">
 								 </div>
 							</div>
							<!--
			                     <div id="singletripwrapper" class="singletripwrapper">
			                        	<span class="trip_num">Trip 1</span>
			                        	<span class="float_right trip_close dot">X</span><br>
			                        	  
			                        	<div class="tripbox">
			                               <div id="there_one_time">
			                               		<input type="hidden" id="depart_time" name="depart_time" class="there" value="1">
			                            	    <label for="depart-date" class="depart-date">Depart</label>
				                                <input type="text" name="date" id="depart-date" class="slim datepicker depart-date hasDatepicker" maxlength="10" value="07/14/2013">
				                                <img class="ui-datepicker-trigger" src="/TicketSchedule/Picture/icon_calendar.png" alt="..." title="...">

				   					<select name="depart_time" id="depart_time" class="slim depart_time"> </select>
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
		                     -->
							<div id="there_repeating" style="display: none;">
		                            <div id="commute-table">
										<ul id="col1">
		                                    <li class="first"><p>选择日期</p></li>
		                                    <li><input type="checkbox" name="there_1" id="there_1" class="checkbox" checked alt="Enable trip for Monday"><label for="there_1">周一</label></li>
		                                    <li><input type="checkbox" name="there_2" id="there_2" class="checkbox" checked alt="Enable trip for Tuesday"><label for="there_2">周二</label></li>                                                     
		                          			<li><input type="checkbox" name="there_3" id="there_3" class="checkbox" checked alt="Enable trip for Wednesday"><label for="there_3">周三</label></li>                                                 
		  									<li><input type="checkbox" name="there_4" id="there_4" class="checkbox" checked alt="Enable trip for Thursday"><label for="there_4">周四</label></li>                                                   
		 									<li><input type="checkbox" name="there_5" id="there_5" class="checkbox" checked alt="Enable trip for Friday"><label for="there_5">周五</label></li>                                                       
		                                    <li><input type="checkbox" name="there_6" id="there_6" class="checkbox" alt="Enable trip for Saturday"><label for="there_6">周六</label></li>                                                                     
		                                    <li><input type="checkbox" name="there_7" id="there_7" class="checkbox" alt="Enable trip for Sunday"><label for="there_7">周日</label></li>                                                                         
		                                </ul>
		                                <ul id="col2">
		                                    <li class="first">
		                                        <h3 class="th_time">出发</h3>
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
		                                        <h3 class="th_time">返回</h3>
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
		                                                <span>固定</span> <span class="last">自由</span>
		                                            </div>
		                                            <div class="pillbuttons">
			                                            <div class="first" style="cursor: pointer;">10min</div><div class="chosen" style="cursor: pointer;">20min</div><div class="last" style="cursor: pointer;">30min</div>
			                                            <input type="hidden" id="flex_global" name="flex_global" value="20">
			                                        </div>
		                                        </div>
		                                    </li>
		                                    <li><a class="edit" href="javascript:void(0)" id="edit_times_1" style="visibility: visible;">编辑</a></li>
		                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_2" style="visibility: visible;">编辑</a></li>
		                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_3" style="visibility: visible;">编辑</a></li>
		                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_4" style="visibility: visible;">编辑</a></li>
		                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_5" style="visibility: visible;">编辑</a></li>
		                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_6" style="visibility: hidden;">编辑</a></li>
		                                 	<li><a class="edit" href="javascript:void(0)" id="edit_times_7" style="visibility: hidden;">编辑</a></li>
		                                 </ul>
		                            </div>
		                   	</div>
		                   </dd></dl>
					</div>
				
				</div>
			</div>
 	            <div class="panel" id="UserType">
 	            <div class="panel-heading">交易信息</div>
					<div class="panel-body">
						<div class="tabbable tabs-top">
							<ul class="nav nav-tabs" id="userNav">
									<li class="" id="asDriver"><a href="javascript: asDriver()"><img src= "/TicketSchedule/Picture/car.jpg"></img>有车</a></li>
									<li class="active" id="asPassenger"><a href="javascript: asPassenger()"><img src= "/TicketSchedule/Picture/nocar.jpg"></img>无车</a></li>
							</ul>
						</div>
						<div id="bargin-content">
							<div id="seats-content" >
								<img src= "/TicketSchedule/Picture/seats.jpg"></img>
								<input type="text" id="seats" name="seats" value="3"/>
							</div>
							<div id="price-content" style="display:none">
			 					<img src= "/TicketSchedule/Picture/yuansign.jpg"></img>
								<input type="text" id="price" name="price" value="15"/>
							</div>
							<div id="distance-content" >
			 					<img src= "/TicketSchedule/Picture/dissign.png" title="大约距离"></img>
								<input type="text" id="distance" name="distance" value="" readonly="readonly" />
							    <input type="text" class="hidden" id="duration" name="duration" />
							</div>
						</div>
					</div>
				</div>
	            <!-- <div class="panel-heading">交易信息</div>
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
				</div> -->
			<span id="form_submit"><button type="submit" class="btn btn-primary">创建或更新行程</button></span>          
            <input type="text" class="hidden" id="defaultS" name="defaultS" value=""></input>
            <input type="text" class="hidden" id="defaultE" name="defaultE" value=""></input>
            <input type="text" class="hidden" id="origLat" name="origLat" value=""></input>
            <input type="text" class="hidden" id="origLng" name="origLng" value=""></input>
            <input type="text" class="hidden" id="destLat" name="destLat" value=""></input>
            <input type="text" class="hidden" id="destLng" name="destLng" value=""></input>
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
	<div id="baidu_tongji" style="display: none">
		<script type="text/javascript">
			var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://"
					: " http://");
			document
					.write(unescape("%3Cscript src='"
							+ _bdhmProtocol
							+ "hm.baidu.com/h.js%3F04d65d39238bfa4301b173d21ddcfeb7' type='text/javascript'%3E%3C/script%3E"));
		</script>
	</div>
</body>
</html>