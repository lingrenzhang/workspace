<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="com.hitchride.standardClass.Topic"%>
<%@ page import="com.hitchride.standardClass.ParticipantRide"%>
<%@ page import="com.hitchride.standardClass.Message"%>
<%@ page import="com.hitchride.standardClass.User"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
	User user = (User) request.getSession().getAttribute("user");
    Topic topicInfo = (Topic) request.getAttribute("topic");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MessageCenter</title>

<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/messagecenter.css" type="text/css" rel="stylesheet">

</head>
<body>
<div id="MessageCenter">
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo">
			</div>
		</div>
	</div>
	<div id="content_wrapper">
		<div class="user_wrapper">
			<div class="user_info" id="from">
				<div class="userpic">
						<div class="username"><%=user.get_name() %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
				</div>
			</div>
			<% List<ParticipantRide> parRides= topicInfo.parRides; 
			   for (Iterator<ParticipantRide> parRideI = parRides.iterator(); parRideI.hasNext();) 
			   {
				   ParticipantRide parRide = parRideI.next();
			 %> 
				<div class="user_info" id="to">
					<div class="userpic">
						<div class="username"><%=parRide.username %></div>
						<img src=<%="/TicketSchedule/UserProfile/"+"default.jpg" %> alt="Profile Picture"></img>
						<span class="passenger"></span>
					</div>
					<div class="user_status">
						<%=parRide.get_status() %>
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
					<div class="username"><%=topicInfo.owner.get_name() %></div>
					<img src=<%="/TicketSchedule/UserProfile/"+topicInfo.owner.get_avatarID() %> alt="Profile Picture"></img>
					<span class="passenger"></span>
				</div>
				<div class="inner_content">
					<h3>
						<span class="inner"><%=topicInfo.ownerRide.origLoc._city %>
						<span class="trip_type round_trip"><%=topicInfo.ownerRide.destLoc._city %></span>
						</span>
					</h3>
					<h4>
						From: <%=topicInfo.ownerRide.origLoc._addr+", "+topicInfo.ownerRide.origLoc._city%>
						To: <%=topicInfo.ownerRide.origLoc._addr+ ", "+topicInfo.ownerRide.origLoc._city%>
					</h4>
					<h4>
						<%= topicInfo.ownerRide.getBarMessage() %>
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
			<div class="comment_wrapper">
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
					<input class="hidden" name="fromId" value=<%=user.get_uid() %>>
					<input class="hidden" name="toId" value=<%=topicInfo.ownerRide.get_ownerId() %>>
					<input class="hidden" name="rideId" value=<%=topicInfo.ownerRide.recordId %>>
			
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


