<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="com.hitchride.standardClass.Topic"%>
<%@ page import="com.hitchride.standardClass.ParticipantRide"%>
<%@ page import="com.hitchride.standardClass.Message"%>
<%@ page import="com.hitchride.standardClass.User"%>
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
    if (!isOwnerMode) //Refresh matchScore
    {
    	MatchScore score1 = new MatchScore();
    }
%>

<script>
    var isOwnerMode = <%=isOwnerMode%>
	var topicId = <%=topicInfo.get_topicId()%>;
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
	    xmlhttp.open("GET","/TicketSchedule/servlet/StatusService?fromStatus=0&toStatus=1&fromUser="+fromUser+"&toUser="+toUser+"&topicId="+topicId,
	    		false);
	    xmlhttp.send();
	    window.location.reload();
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
    	 +"&toStatus="+toStatus+"&fromUser="+fromUser+"&toUser="+toUser+"&topicId="+topicId,
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
           	 +"&toStatus="+toStatus+"&fromUser="+fromUser+"&toUser="+toUser+"&topicId="+topicId,
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
           	 +"&toStatus="+toStatus+"&fromUser="+fromUser+"&toUser="+toUser+"&topicId="+topicId,
        	    		false);
	    xmlhttp.send();
	    window.location.reload();
    	
    }

</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>RideCenter</title>

<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/ridecenter.css" type="text/css" rel="stylesheet">
<link rel="shortcut icon" href="/TicketSchedule/favicon.ico" type="image/x-icon" /> 

</head>
<body onload="initialize()">

<div id="RideCenter">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
	</div>
	
	<div id="content_wrapper">

		<div class="user_wrapper">
		    <%if (!isOwnerMode) 
	    	{%>
				<div class="user_info" id="from">
				  <a href = "/TicketSchedule/UserCenter.jsp">
					<div class="userpic">
							<div class="username"><%=user.get_name() %></div>
							<img src=<%=user.get_head_portrait_path() %> alt="Profile Picture"></img>
							<span class="passenger"></span>
					</div>
				</a>
					<%if (!alreadyPart){%>
					<div class="user_operation" id="user_operation">
						<button type=button onclick="join()">Join</button>
					</div>
					<%}else{ 
						ParticipantRide pRide = topicInfo.getpRideByuserId(user.get_uid());
					%>
					<%= pRide.get_status_user_control() %>
					<%} %>
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
				   if (parRide.get_userId()!=user.get_uid()){
			 %> 
				<div class="user_info" id="to">
					<div class="userpic">
						<div class="username"><%=parRide.get_username() %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
					</div>
					<div class="user_status">
						<%= (isOwnerMode)? parRide.get_status_owner_control() : parRide.get_status_message() %>
					</div>
					<div class="user_match">
						<div class="match_Loc" style="width : <%=parRide.get_Match().getLocationMatching() %>px "></div>
						<div class="match_Sch" style="width : <%=parRide.get_Match().getSchedulingMatching() %>px "></div>
						<div class="match_Bar" style="width : <%=parRide.get_Match().getBarginMatching() %>px "></div>
					</div>
				</div>
			<%		}
			   }
			%>
			<% parRides= topicInfo.parRides; 
			   for (Iterator<ParticipantRide> parRideI = parRides.iterator(); parRideI.hasNext();) 
			   {
				   ParticipantRide parRide = parRideI.next();
				   if (parRide.get_userId()!=user.get_uid()){
			 %> 
				<div class="user_info" id="to">
					<div class="userpic">
						<div class="username"><%=parRide.get_username() %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
					</div>
					<div class="user_status">
						<%= (isOwnerMode)? parRide.get_status_owner_control() : parRide.get_status_message() %>
					</div>
					<div class="user_match">
						<div class="match_Loc" style="width : <%=parRide.get_Match().getLocationMatching() %>px "></div>
						<div class="match_Sch" style="width : <%=parRide.get_Match().getSchedulingMatching() %>px "></div>
						<div class="match_Bar" style="width : <%=parRide.get_Match().getBarginMatching() %>px "></div>
					</div>
				</div>
			<%
				   }
			   }
			%>
		</div>

		<div class="discussion_timeline_wrapper">
		    <%= isOwnerMode? "<a href=\"/TicketSchedule/UserCenter.jsp\">" : " "  %>
	        <div class="entry">
				<div class="userpic">
					<div class="username"><%=topicInfo.owner.get_name()%></div>
					<img src=<%= topicInfo.owner.get_head_portrait_path() %> alt="Profile Picture"></img>
					<span class="passenger"></span>
				</div>
				<%=topicInfo.displayOnWebRideCenter() %>
			</div>
			<%= isOwnerMode? "</a>" : "" %>
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
					<input class="hidden" name="topicId" id="topicId" value=<%=topicInfo.get_topicId()%>>
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


