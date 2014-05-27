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
	var mapcanvas=$("#map-canvas");
	var left = parseFloat(datepicker.css("left"));
	var right = left + parseFloat(datepicker.css("width"));
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

function initCalandar(url)
{
	currentDate = new Date();
	selectDate= new Date();
	displayDate= new Date();
	
	$(".datetime_icon").click(function(){
	    //Data picker related
		var mapcanvas=$("#map-canvas");
		mapcanvas.fadeToggle();
		
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
	
}	
	
	
		

