//This part is for calandar picker

var displayDate;

function select_Date(value,contentHolderId,replaceWidgetId)
{
	var sdate = document.getElementById(contentHolderId).value;
	var dtArr=sdate.split("/");
	var currentDate = new Date();
	var selectDate=new Date();
    selectDate.setDate(dtArr[1]);
    selectDate.setMonth(dtArr[0]-1);
    selectDate.setFullYear(dtArr[2]);
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
	
	var origMonth= selectDate.getMonth();
	var origYear= selectDate.getFullYear();
	var origDate= selectDate.getDate();
	selectDate.setMonth(displayDate.getMonth());
	selectDate.setFullYear(displayDate.getFullYear());
	selectDate.setDate(value);
	

	if (selectDate<currentDate)
	{
		alert("该日已过期，请重新选择");
		selectDate.setMonth(origMonth);
		selectDate.setFullYear(origYear);
		selectDate.setDate(origDate);
		list[value-1].className="ui-state-default";
		if (origMonth==displayDate.getMonth()&&origYear==displayDate.getFullYear())
		{
			list[origDate-1].className="ui-state-default ui-state-active";
		}
	}
	else
	{
		var datepicker=$("#ui-datepicker-div");
		var mapcanvas=$("#"+replaceWidgetId);
		datepicker.css({"display":"none"});
		if (mapcanvas!=null)
		{
			mapcanvas.css({"display":""});
		}
	}
	document.getElementById(contentHolderId).value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
};

function displayCalender(year,month,contentHolderId,replaceWidgetId)
{
	
	var sdatev = document.getElementById(contentHolderId).value;
	var dtArr=sdatev.split("/");
	var selectDate=new Date();
    selectDate.setDate(dtArr[1]);
    selectDate.setMonth(dtArr[0]-1);
    selectDate.setFullYear(dtArr[2]);
    
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
		link.href="javascript:select_Date("+countday+",'"+contentHolderId+"','"+replaceWidgetId+"')";
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
			link.href="javascript:select_Date("+countday+",'"+contentHolderId+"','"+replaceWidgetId+"')";
			
			tbodynode.rows[week].cells[i].appendChild(link);
		}
		week++;
	}
	/*
	window.onresize = function(e){
		var datepicker=$("#ui-datepicker-div");
		datepicker.css({"display":"none"});
		if (replaceWidget!=null)
		{
			replaceWidget.css({"display":""});
		}
	};

	window.onclick = function(e){
		var datepicker=$("#ui-datepicker-div");
		var left = parseFloat(datepicker.css("left"));
		var right = left + parseFloat(datepicker.css("width"))+25; 
		var top = parseFloat(datepicker.css("top"))-30;
		var down = top+parseFloat(datepicker.css("height"))+30;
		
		if ((e.pageX<left)||(e.pageX>right)
			||(e.pageY<top)||(e.pageY>down))
		{
	  	   datepicker.css({"display":"none"});
	  	   if (replaceWidget!=null)
			{
				replaceWidget.css({"display":""});
			}
		}
	};
	*/
}


function nextMonth(contentHolderId,replaceWidgetId)
{
	displayCalender(displayDate.getFullYear(),displayDate.getMonth()+1,contentHolderId,replaceWidgetId);
}

function prevMonth(contentHolderId,replaceWidgetId)
{
	displayCalender(displayDate.getFullYear(),displayDate.getMonth()-1,contentHolderId,replaceWidgetId);
}

function Calandar(selectorHolder,pickerHolder,contentHolderId,replaceWidgetId)
{
	var replaceWidget=$("#"+replaceWidgetId);
	pickerHolder.innerHTML = 
	"<div class='ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all'>"+
	"<a id='datepicker-prev' class='ui-datepicker-prev ui-corner-all' title='Prev'>"+
	"	<span class='ui-icon ui-icon-circle-triangle-e'>Prev</span>"+
	"</a>"+
	"	<a id='datepicker-next' class='ui-datepicker-next ui-corner-all' title='Next'>"+
	"		<span class='ui-icon ui-icon-circle-triangle-w'>Next</span>"+
	"	</a>"+
	"	<div class='ui-datepicker-title'>"+
	"		<span class='ui-datepicker-month' id='picker-Month'></span>"+
	"		&nbsp;"+
	"		<span class='ui-datepicker-year' id='picker-Year'></span>"+
	"	</div>"+
	"</div>"+
	"<table class='ui-datepicker-calendar' id='ui-datepicker-calendar'>"+
	"	<thead>"+
	"		<tr>"+
	"			<th class='ui-datepicker-week-end'>"+
	"				<span title='Sunday'>Su</span>"+
	"			</th>"+
	"			<th>"+
	"				<span title='Monday'>Mo</span>"+
	"			</th>"+
	"			<th>"+
	"				<span title='Tuesday'>Tu</span>"+
	"			</th>"+
	"			<th>"+
	"				<span title='Wednesday'>We</span>"+
	"			</th>"+
	"			<th>"+
	"				<span title='Thursday'>Th</span>"+
	"			</th>"+
	"			<th>"+
	"				<span title='Friday'>Fr</span>"+
	"			</th>"+
	"			<th class='ui-datepicker-week-end'>"+
	"				<span title='Saturday'>Sa</span>"+
	"			</th>"+
	"		</tr>"+
	"	</thead>"+
	"	<tbody>"+
	"	</tbody>"+
	"</table>";
	
	var selectDate=new Date();
	
	this.getDate = function(){
		document.getElementById(contentHolderId).value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
		return (selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	};
	
	displayDate= new Date();
	selectorHolder.onclick = function(){
	    //Data picker related
		if(replaceWidget!=null)
		{
			replaceWidget.fadeToggle();
		}
		
	    var search= $("#"+contentHolderId).offset();
	    var datepicker=$("#ui-datepicker-div");
	    datepicker.css({"left" : search.left,"top" : search.top+35});
	    datepicker.fadeToggle();
	    var sdate=document.getElementById(contentHolderId).value;
	    
	    try
	    {
		    var dtArr=sdate.split("/");
			var selectDate=new Date();
		    selectDate.setDate(dtArr[1]);
		    selectDate.setMonth(dtArr[0]-1);
		    selectDate.setFullYear(dtArr[2]);
		    
		    displayDate.setDate(dtArr[1]);
		    displayDate.setMonth(dtArr[0]-1);
		    displayDate.setFullYear(dtArr[2]);
		 	var mon = displayDate.toDateString().substring(4,7);
			document.getElementById("picker-Month").innerHTML=mon;
			document.getElementById("picker-Year").innerHTML=displayDate.getFullYear();
			displayCalender(displayDate.getFullYear(),displayDate.getMonth(),contentHolderId,replaceWidgetId);
	    }
	    catch(err)
	    {
	    	selectDate=new Date();
		    displayDate=new Date();
		    var mon = displayDate.toDateString().substring(4,7);
			document.getElementById("picker-Month").innerHTML=mon;
			document.getElementById("picker-Year").innerHTML=displayDate.getFullYear();
			displayCalender(displayDate.getFullYear(),displayDate.getMonth(),contentHolderId,replaceWidgetId);
	    }
	    
	    document.getElementById("datepicker-prev").onclick = function(){
	    	prevMonth(contentHolderId,replaceWidgetId);
	    };
	    document.getElementById("datepicker-next").onclick = function(){
	    	nextMonth(contentHolderId,replaceWidgetId);
	    };
	    
	 };

	$(".ui-state-default").click(function(){
		this.className="ui-state-default ui-state-active";
		selectDate.setMonth(displayDate.getMonth());
		selectDate.setYear(displayDate.getYear());
		selectDate.setDate(this.innerHtml);
		document.getElementById(contentHolderId).value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	});

	$("#commute_day").click(function(){
		$(this).className=$(this).className + " active";
	});
}	


//Following part is for time picker
//User different valueId so no 
function TimePicker(widgetHolder,valueId,pictureName)
{
	if (valueId==null)
	{
		valueId = "ride_time";
	}
	if (pictureName == null)
	{
		pictureName = "/TicketSchedule/Picture/clock.jpg";
	}
	this.widgetHolder = widgetHolder;
	this.valueId = valueId;
	this.pictureName = pictureName;
				var widgetScript = "<img src= '"+pictureName+"'/>"
				+"<select class='time_picker ap_picker' name='"+valueId+"_ap' id='"+valueId+"_ap'>"
				+"<option value='AM'>上午</option>"
				+"<option value='PM'>下午</option>"
				+"</select>"
				+"<select class='time_picker hour_picker' name='"+valueId+"_hour' id='"+valueId+"_hour' class='slim'>"
				+"<option value='0'>0</option>"
				+"<option value='1'>1</option>"
				+"<option value='2'>2</option>"
				+"<option value='3'>4</option>"
				+"<option value='4'>4</option>"
				+"<option value='5'>5</option>"
				+"<option value='6'>6</option>"
				+"<option value='7'>7</option>"
				+"<option value='8'>8</option>"
				+"<option value='9'>9</option>"
				+"<option value='10'>10</option>"
				+"<option value='11'>11</option>"
				+"</select>点"
				+"<select class='time_picker minute_picker' name='"+valueId+"_minute' id='"+valueId+"_minute' class='slim'>"
				+"<option value='00'>00</option>"
				+"<option value='10'>10</option>"
				+"<option value='20'>20</option>"
				+"<option value='30'>30</option>"
				+"<option value='40'>40</option>"
				+"<option value='50'>50</option>"
				+"</select>分";
		widgetHolder.innerHTML = widgetScript;
	
	this.setDefaultTime = function(){
		var ctime = new Date();
		var hour = ctime.getHours();
		var minutes = ctime.getMinutes();
		var mu = Math.floor(minutes/10)+4;
		if (mu>=6)
		{ 
		  mu = mu-6;
		  hour = hour+1;
		}
		document.getElementById(valueId+"_minute").selectedIndex = mu;
		if (hour>=12)
		{
			hour=hour - 12;
			if (hour!=12)
			{
				document.getElementById(valueId+"_ap").value="PM";
				document.getElementById(valueId+"_hour").value=hour;
			}
			else
			{
				//Next day
				document.getElementById(valueId+"_ap").value="AM";
				document.getElementById(valueId+"_hour").value= 0 ;
			}
		}
		else
		{
			document.getElementById(valueId+"_hour").value=hour;
		}
	};
	
	this.getHour = function(){
		var time_hour;
		if (document.getElementById(valueId+"_ap").value=="AM")
		{
			time_hour=  document.getElementById(valueId+"_hour").value;
		}
		else
		{
			time_hour=  Number(document.getElementById(valueId+"_hour").value)+Number(12);
			if (time_hour>=24)
				time_hour =time_hour-24;
		}
		return time_hour;
	};
	
	this.getMinute = function(){
		var time_minute = document.getElementById(valueId+"_minute").value;
		return time_minute;
	};
};



