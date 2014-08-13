var currentDate;
var selectDate;
var displayDate;

function getCalander(widgetId)
{
	document.getElementById(widgetId).innerHTML=
	"<div class='ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all'>"+
	"<a class='ui-datepicker-prev ui-corner-all' onclick='prevMonth()' title='Prev'>"+
	"	<span class='ui-icon ui-icon-circle-triangle-e'>Prev</span>"+
	"</a>"+
	"	<a class='ui-datepicker-next ui-corner-all' onclick='nextMonth()' title='Next'>"+
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
}


function select_Date(value)
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
		link.href="javascript:select_Date("+countday+")";
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
			link.href="javascript:select_Date("+countday+")";
			
			tbodynode.rows[week].cells[i].appendChild(link);
		}
		week++;
	}
}

window.onresize = function(e){
	var datepicker=$("#ui-datepicker-div");
	var mapcanvas=$("#map-canvas");
	datepicker.css({"display":"none"});
	mapcanvas.css({"display":""});
};

window.onclick = function(e){
	var datepicker=$("#ui-datepicker-div");
	var mapcanvas=$("#map-canvas");
	var left = parseFloat(datepicker.css("left"));
	var right = left + parseFloat(datepicker.css("width"))+25; 
	var top = parseFloat(datepicker.css("top"))-30;
	var down = top+parseFloat(datepicker.css("height"))+30;
	
	if ((e.pageX<left)||(e.pageX>right)
		||(e.pageY<top)||(e.pageY>down))
	{
  	   datepicker.css({"display":"none"});
  	   mapcanvas.css({"display":""});
	}
};

function nextMonth()
{
	displayCalender(displayDate.getFullYear(),displayDate.getMonth()+1);

}

function prevMonth()
{
	displayCalender(displayDate.getFullYear(),displayDate.getMonth()-1);
}

function initCalandar(url,countentId)
{
	getCalander(url);
	currentDate = new Date();
	selectDate= new Date();
	displayDate= new Date();
	
	$(".datetime").click(function(){
	    //Data picker related
		var mapcanvas=$("#map-canvas");
		mapcanvas.fadeToggle();
		
	    var search= $("#"+countentId).offset();
	    var datepicker=$("#ui-datepicker-div");
	    datepicker.css({"left" : search.left,"top" : search.top+35});
	    datepicker.fadeToggle();
	    var sdate=document.getElementById(countentId).value;
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
	});

	$(".ui-state-default").click(function(){
		this.className="ui-state-default ui-state-active";
		selectDate.setMonth(displayDate.getMonth());
		selectDate.setYear(displayDate.getYear());
		selectDate.setDate(this.innerHtml);
		document.getElementById(countentId).value=(selectDate.getMonth()+1)+"/"+selectDate.getDate()+"/"+selectDate.getFullYear();
	});

	$("#commute_day").click(function(){
		$(this).className=$(this).className + " active";
	});
}	
	
	
		

