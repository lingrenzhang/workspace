;
$(".requireLogin").click(function(){
	if (islogin == false)
	{
		window.location.href="/TicketSchedule/Login.jsp";
	}
}

);

function getJson(url)
{
	var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
      	xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
	xmlhttp.open("GET",url,false);
	xmlhttp.send();
	return xmlhttp.responseText;
}

function formatDateSwitch(count)
{
	switch (count)
	{
		case 1: //from MM/DD/YYYY to Day Mon DD YYYY
			
			break;
	}
	
}