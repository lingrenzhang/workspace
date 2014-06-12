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
    url=url+"&t="+Math.random(); //IE cache issue. The no Cahche is not working somehow...
    url=encodeURI(url);
	xmlhttp.open("GET",url,false);
	xmlhttp.setRequestHeader("Cache-Control","no-cache");
	xmlhttp.send();
	return xmlhttp.responseText;
}


//Return null for not found.
function getURLPara(key)
{
	var value;
	var url = document.location.href;	
	var reg = new RegExp("(?:"+key+"\=)(\\w+)(?:&*)");
	value = reg.exec(url);
	if (value!=null)
	{
		return value[1];
	}
	else
	{
		return null;
	}
}



function formatDateSwitch(count)
{
	switch (count)
	{
		case 1: //from MM/DD/YYYY to Day Mon DD YYYY
			
			break;
	}
	
}