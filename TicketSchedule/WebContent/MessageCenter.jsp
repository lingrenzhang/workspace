<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="com.hitchride.standardClass.Topic"%>
<%@ page import="com.hitchride.standardClass.ParticipantRide"%>
<%@ page import="com.hitchride.standardClass.Message"%>
<%@ page import="com.hitchride.standardClass.User"%>
<%@ page import="com.hitchride.standardClass.Participant"%>
<%@ page import="com.hitchride.standardClass.MatchScore"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
    Topic topicInfo = (Topic) request.getSession().getAttribute("topic");
    Boolean isOwnerMode = (Boolean) request.getAttribute("isOwnerMode");
    Boolean alreadyPart = (Boolean) request.getAttribute("alreadyPart");
    User user = (User) request.getSession().getAttribute("user");
    Boolean isCommute = topicInfo.ownerRide._rideInfo.schedule.isCommute();
    MatchScore score = new MatchScore();
%>

<script>
    var isOwnerMode = <%=isOwnerMode%>
	var rideId = <%=topicInfo.ownerRide._rideInfo.recordId%>;
	var isalreadyPart;
    var fromUser;
    var toUser;
	if (isOwnerMode==false)
	{
		isalreadyPart= <%=alreadyPart %>;
		fromUser = <%=user.get_uid()  %>;
		toUser = <%=topicInfo.ownerRide._rideInfo.get_user().get_uid()%>;
	}
	else
	{
		fromUser = <%=topicInfo.ownerRide._rideInfo.get_user().get_uid()%>;
	}


    function initialize(){
		if (isalreadyPart == false)
		{
			document.getElementById("comment_wrapper").setAttribute("class", "comment_wrapper hidden");
		}
		fromId = document.getElementById("fromId").getAttribute("value");
		toId = document.getElementById("toId").getAttribute("value");
		rideId = document.getElementById("rideId").getAttribute("value");
    }
    
    
    function join()
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
	    xmlhttp.open("GET","/TicketSchedule/servlet/StatusService?fromStatus=0&toStatus=1&fromUser="+fromUser+"&toUser="+toUser+"&ownRideId="+rideId,
	    		false);
	    xmlhttp.send();
	    document.getElementById("comment_wrapper").setAttribute("class", "comment_wrapper");
	    document.getElementById("user_operation").innerHTML = "<div>Waiting owner accept</div>";
    }
  	
    
    function accept(event)
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
    	if (isOwnerMode)
    	{
    		toUser = event.currentTarget.parentElement.getAttribute("uid");
    	}
        var fromStatus = event.currentTarget.parentElement.getAttribute("fromStatus");
        var toStatus;
    	if (fromStatus == 3)
    	{
    		toStatus = 4;
    	}
    	if (fromStatus == 1)
    	{
    		toStatus = 3;
    	}
    	if (fromStatus == 2)
    	{
    		if (isOwnerMode)
    		{
    			toStatus= 3;
    		}
    		else
    		{
    			toStatus = 1;
    		}
    	}

    	xmlhttp.open("GET","/TicketSchedule/servlet/StatusService?fromStatus="+fromStatus
    	 +"&toStatus="+toStatus+"&fromUser="+fromUser+"&toUser="+toUser+"&ownRideId="+rideId,
 	    		false);
 	    xmlhttp.send();
 	    window.location.reload();
    	
    }
    
    
    function moreinfo(event)
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
    	if (isOwnerMode)
    	{
    		toUser = event.currentTarget.parentElement.getAttribute("uid");
    	}
        var fromStatus = event.currentTarget.parentElement.getAttribute("fromStatus");
        var toStatus = 2;
        xmlhttp.open("GET","/TicketSchedule/servlet/StatusService?fromStatus="+fromStatus
           	 +"&toStatus="+toStatus+"&fromUser="+fromUser+"&toUser="+toUser+"&ownRideId="+rideId,
        	    		false);
	    xmlhttp.send();
	    window.location.reload();
    	
    }
    
    function decline(event)
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
    	if (isOwnerMode)
    	{
    		toUser = event.currentTarget.parentElement.getAttribute("uid");
    	}
        var fromStatus = event.currentTarget.parentElement.getAttribute("fromStatus");
        var toStatus = 0;
        xmlhttp.open("GET","/TicketSchedule/servlet/StatusService?fromStatus="+fromStatus
           	 +"&toStatus="+toStatus+"&fromUser="+fromUser+"&toUser="+toUser+"&ownRideId="+rideId,
        	    		false);
	    xmlhttp.send();
	    window.location.reload();
    	
    }

</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MessageCenter</title>

<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/messagecenter.css" type="text/css" rel="stylesheet">

</head>
<body onload="initialize()">

<div id="MessageCenter">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
	</div>
	<div id="content_wrapper">

		<div class="user_wrapper">
		    <%if (!isOwnerMode && !alreadyPart) 
	    	{%>
			<div class="user_info" id="from">
				<div class="userpic">
						<div class="username"><%=user.get_name() %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
				</div>
				<div class="user_operation" id="user_operation">
					<button type=button onclick="join()">Join</button>
				</div>
				<div class="user_match">
					<div class="match_Loc" style="width : <%=score.getLocationMatching() %>px "></div>
					<div class="match_Sch" style="width : <%=score.getSchedulingMatching() %>px "></div>
					<div class="match_Bar" style="width : <%=score.getBarginMatching() %>px "></div>
				</div>
			</div>
			<%}%>
			<% List<ParticipantRide> parRides= topicInfo._requestPride; 
			   for (Iterator<ParticipantRide> parRideI = parRides.iterator(); parRideI.hasNext();) 
			   {
				   ParticipantRide parRide = parRideI.next();
			 %> 
				<div class="user_info" id="to">
					<div class="userpic">
						<div class="username"><%=parRide._rideinfo.username %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
					</div>
					<div class="user_status">
						<%if (!isOwnerMode){ %>
							<%= (parRide.userId == user.get_uid())? parRide.get_status_user_control() : parRide.get_status_message()  %>
					    <%}else {%>
					        <%= parRide.get_status_owner_control()%>
					    <%} %>
					</div>
					<div class="user_match">
						<div class="match_Loc" style="width : <%=parRide.get_Match().getLocationMatching() %>px "></div>
						<div class="match_Sch" style="width : <%=parRide.get_Match().getSchedulingMatching() %>px "></div>
						<div class="match_Bar" style="width : <%=parRide.get_Match().getBarginMatching() %>px "></div>
					</div>
				</div>
			<%
			   }
			%>
			<% parRides= topicInfo.parRides; 
			   for (Iterator<ParticipantRide> parRideI = parRides.iterator(); parRideI.hasNext();) 
			   {
				   ParticipantRide parRide = parRideI.next();
			 %> 
				<div class="user_info" id="to">
					<div class="userpic">
						<div class="username"><%=parRide._rideinfo.username %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
					</div>
					<div class="user_status">
						<%if (!isOwnerMode){ %>
							<%= (parRide.userId == user.get_uid())? parRide.get_status_user_control() : parRide.get_status_message()  %>
					    <%}else {%>
					        <%= parRide.get_status_owner_control()%>
					    <%} %>
					</div>
					<div class="user_match">
						<div class="match_Loc" style="width : <%=parRide.get_Match().getLocationMatching() %>px "></div>
						<div class="match_Sch" style="width : <%=parRide.get_Match().getSchedulingMatching() %>px "></div>
						<div class="match_Bar" style="width : <%=parRide.get_Match().getBarginMatching() %>px "></div>
					</div>
				</div>
			<%
			   }
			%>
		</div>

		<div class="discussion_timeline_wrapper">
	        <div class="entry">
				<div class="userpic">
					<div class="username"><%=topicInfo.owner.get_name()%></div>
					<img src=<%="/TicketSchedule/UserProfile/"+topicInfo.owner.get_avatarID() %> alt="Profile Picture"></img>
					<span class="passenger"></span>
				</div>
				<div class="inner_content">
					<h3>
						<span class="inner"><%=topicInfo.ownerRide._rideInfo.origLoc._city %>
						<span class="trip_type round_trip"><%=topicInfo.ownerRide._rideInfo.destLoc._city %></span>
						</span>
					</h3>
					<h4>
						From: <%=topicInfo.ownerRide._rideInfo.origLoc._addr+", "+topicInfo.ownerRide._rideInfo.origLoc._city%>
						To: <%=topicInfo.ownerRide._rideInfo.origLoc._addr+ ", "+topicInfo.ownerRide._rideInfo.origLoc._city%>
					</h4>
					<h4>
						<%= topicInfo.ownerRide._rideInfo.getBarMessage() %>
					</h4>
				</div>
			</div>
			<div class="discussion_wrapper">
				<% for(Iterator<Message> mI=topicInfo.messages.iterator();mI.hasNext();)
				  { Message message =mI.next();
				    if(!message.isSystemMessage())
				     {
				%>
					<div class="discussion">
						<strong>
							<a href="#userinfo" class="auther"><%=message.getFrom().get_name()%></a>
						</strong>
						<span><%="comments on " + message.getMessageGenerateDate().toString() %>
						</span><br>
						<span class="message_content"><%=message.getMessageContent() %>
						</span>
					</div>
				    <%} %>
				<%} %>
			</div>

			<div class="comment_wrapper" id="comment_wrapper">
				<div class="comment_header">
					<strong>
						<a href="#userinfo" class="auther"><%=user.get_name()%></a>
					</strong>
				</div>
				<form action="./MessageService" method="post">
					<div class="comment_content">
						<textarea name="comment" id="comment_body" placeholder="Leave a comment" class="comment-form-textarea"></textarea>
					</div>
					<button type="submit" id="comment">comment</button>	
					<input class="hidden" name="fromId" id="fromId" value=<%=user.get_uid() %>>
					<input class="hidden" name="toId" id="toId" value=<%=topicInfo.ownerRide.get_ownerId() %>>
					<input class="hidden" name="rideId" id="rideId" value=<%=topicInfo.ownerRide._rideInfo.recordId %>>
			        <input class="hidden" name="method" value="create">
				</form>
			</div>

            <div class="sysMess_wrapper">
            <% for(Iterator<Message> mI=topicInfo.messages.iterator();mI.hasNext();)
				  { Message message =mI.next();
				    if (message.isSystemMessage())
				    {
			%>
				<span class="time"><%=message.getMessageGenerateDate().toString() %>
				</span><br>
				<span class="message_content"><%=message.getMessageContent() %>
				</span><br>
				<%} %>
			<%} %>
            </div>
        </div>
	    <div class="topic_wrapper">
	    	<div class="schedule_wrapper">
	    	    <table class="schedule_commute">
	    	    	<thead>
	    	    		<tr>
	    	    			<th><span title="Sunday">Su</span></th>
	    	    			<th><span title="Monday">Mo</span></th>
	    	    			<th><span title="Tuesday">Tu</span></th>
	    	    			<th><span title="Wedensday">We</span></th>
	    	    			<th><span title="Thursday">Th</span></th>
	    	    			<th><span title="Friday">Fr</span></th>
	    	    			<th><span title="Saturday">Sa</span></th>
	    	    		</tr>
	    	    	</thead>
	    	    	<tbody>
	    	    		<tr>
	    	    			<td><img src="/TicketSchedule/Picture/4-3.png"></td>
	    	    			<td><img src="/TicketSchedule/Picture/4-3.png"></td>
	    	    			<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
	    	    		</tr>
	    	    		<tr>
	    	    			<td><img src="/TicketSchedule/Picture/4-3.png"></td>
	    	    			<td><img src="/TicketSchedule/Picture/4-3.png"></td>
	    	    			<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
							<td><img src="/TicketSchedule/Picture/4-3.png"></td>
	    	    		</tr>
	    	    	</tbody>
	    	    </table>
	    	</div>
	    	<div class="bargain_wrapper">
	    		BarginInfo
	    	</div>
	    </div>

	</div>           
</div>
</body>
</html>


