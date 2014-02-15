<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="com.hitchride.standardClass.Topic"%>
<%@ page import="com.hitchride.standardClass.ParticipantRide"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
	String from = (String) request.getSession().getAttribute("userName");
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
						<div class="username"><%=from %></div>
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
						<div class="match_Bar" stype="width : <%=parRide.get_Match().getBarginMatching() %>px "></div>
					</div>
				</div>
			<%
			   }
			%>
			<div>
				<button type="submit" id="sendbutton">Send</button>
			</div>
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
				</div>
			</div>
	        <div class="discussion_wrapper">
          	   <div class="timeline-comment-wrapper js-comment-container">
  			   <a href="/seanjiang86"><img alt="Xiyao Jiang" class="timeline-comment-avatar" height="48" src="https://0.gravatar.com/avatar/9560c50251e98d726b253a2a12e44d12?d=https%3A%2F%2Fidenticons.github.com%2F657103d3557fac4f3d1e0d7fdd951ab6.png&amp;r=x&amp;s=140" width="48"></a>
				<div class="timeline-comment timeline-comment-current-user">
  					<div id="issue-27195547" class="comment js-comment js-task-list-container is-task-list-enabled" data-body-version="3da9838b84788a9e7d63bc8b439c1b25">
      					<div class="timeline-comment-header ">
					         <div class="timeline-comment-actions">
       					         <a class="octicon octicon-pencil js-comment-edit-button" href="#" title="Edit comment"></a>
        					 </div>
				        	 <div class="timeline-comment-header-text">
        					  	 <strong>
        					      <a href="/seanjiang86" class="author">seanjiang86</a>
     					         </strong>
					               commented
					             <a href="#issue-27195547" class="timestamp"><time class="js-relative-date" data-title-format="YYYY-MM-DD HH:mm:ss" datetime="2014-02-07T23:06:28-08:00" title="2014-02-08 15:06:28">an hour ago</time></a>
					         </div>
     				 	</div>

    					<div class="comment-content">
       						<p class="comment-form-stale">The content you are editing has changed. Reload the page and try again.</p>
      						<div class="edit-comment-hide">
        						<div class="comment-body markdown-body markdown-format js-comment-body">
            						<p>Basic idea is to provide some standard data structure for runtime use.<br>
									Like:<br>
									1.Geo related class<br>
   										This is basically a mapping of Google api object. Used to increase system robust and more easy to extend our system function.<br>
   										The class is a wrapper of GeoObject in javascript to the Java environment. In addition, it will contain some specific field/function used in our system. Also, it provides some self-check function so when certain information is missing before calling a specific function in our match/management system, it will automatically collect information from google(through JSON) again before use. <br>
    									There should already be basic Android SDK about this. Make simple prototype in our system and do more research about the Google API.</p>
									<p>2.Record related object<br>
  										Mange ride information.</p>
						        </div>
     						 </div>

       						<div class="context-loader">Sending Request…</div>
        					<div class="form-content js-write-bucket js-uploadable-container upload-enabled is-default" data-model="assets">
         						<form accept-charset="UTF-8" action="/lingrenzhang/workspace/issues/25" class="js-comment-update" data-remote="true" data-type="json" method="post"><div style="margin:0;padding:0;display:inline"><input name="_method" type="hidden" value="put"><input name="authenticity_token" type="hidden" value="hr4xNO0MoUdR406SaVFHD7zVVCDjfQ5WZYa8LXWyxE8="></div>
            						<div class="fieldWithErrors"><textarea class="comment-form-textarea js-comment-field js-task-list-field js-size-to-fit" data-suggester="issue_25_suggester" id="issue-27195547-body" name="issue[body]" tabindex="1">Basic idea is to provide some standard data structure for runtime use.
									Like:
									1.Geo related class
   										This is basically a mapping of Google api object. Used to increase system robust and more easy to extend our system function.
   										The class is a wrapper of GeoObject in javascript to the Java environment. In addition, it will contain some specific field/function used in our system. Also, it provides some self-check function so when certain information is missing before calling a specific function in our match/management system, it will automatically collect information from google(through JSON) again before use. 
    									There should already be basic Android SDK about this. Make simple prototype in our system and do more research about the Google API.
									2.Record related object
  										Mange ride information.
									3.Message related object
									4.User related object
    									User Management, User Group/Customer Managment
									</textarea></div>
          				            <div class="form-actions">
              							<a href="#" class="minibutton danger comment-cancel-button js-comment-cancel-button" data-confirm-text="Are you sure you want to cancel? You have unsaved changes that will be reverted.">Cancel</a>
             					 		<button type="submit" class="minibutton" tabindex="1" data-disable-with="">Update Comment</button>
           						    </div>
			   						<div class="suggester-container">
              							<div class="suggester js-navigation-container" id="issue_25_suggester" data-url="/lingrenzhang/workspace/suggestions/issue/27195547">
              							</div>
            						</div>
								</form>        
							</div>
        					<div class="comment-form-error comment-form-bottom js-comment-update-error"></div>
    					</div>
  					</div>
				</div>
			   </div>
			    <!-- Rendered timeline since 2014-02-07 23:06:28 -->
				<div id="js-timeline-marker" class="js-socket-channel js-updatable-content" data-channel="lingrenzhang/workspace:issue:27195547" data-url="/lingrenzhang/workspace/issues/25/show_partial?partial=timeline_marker&amp;since=1391843188" data-last-modified="Sat, 08 Feb 2014 07:06:28 GMT">
				</div>
     	   </div>
	        <div class="discussion-timeline-actions">
				<div class="timeline-comment-wrapper timeline-new-comment js-comment-container">
 					 <a href="/seanjiang86"><img alt="Xiyao Jiang" class="timeline-comment-avatar" height="48" src="https://0.gravatar.com/avatar/9560c50251e98d726b253a2a12e44d12?d=https%3A%2F%2Fidenticons.github.com%2F657103d3557fac4f3d1e0d7fdd951ab6.png&amp;r=x&amp;s=140" width="48"></a>
					 <form accept-charset="UTF-8" action="/lingrenzhang/workspace/issue_comments" class="js-new-comment-form" data-remote="true" data-type="json" method="post"><div style="margin:0;padding:0;display:inline"><input name="authenticity_token" type="hidden" value="hr4xNO0MoUdR406SaVFHD7zVVCDjfQ5WZYa8LXWyxE8="></div>
    					<div class="timeline-comment">
      						<input type="hidden" name="issue" value="25">
      						<div class="js-previewable-comment-form previewable-comment-form write-selected" data-preview-url="/preview?repository=10860755">
  								<div class="comment-form-head tabnav">
    								<ul class="tabnav-tabs">
      									<li><a href="#write_bucket_461" class="tabnav-tab write-tab js-write-tab selected">Write</a></li>
     									<li><a href="#preview_bucket_461" class="tabnav-tab preview-tab js-preview-tab">Preview</a></li>
    								</ul>
    							    <span class="tabnav-right">
      									<span class="tabnav-widget text">Comments are parsed with <a href="https://help.github.com/articles/github-flavored-markdown" class="gfm-help" target="_blank">GitHub Flavored Markdown</a></span>
    								</span>
  								</div>
								<div class="comment-form-error js-comment-form-error" style="display:none">    There was an error creating your Issue: 
								</div>
  								<div id="write_bucket_461" class="write-content js-write-bucket js-uploadable-container upload-enabled is-default" data-model="assets">
 									<a href="#fullscreen_comment_body_461" class="enable-fullscreen js-enable-fullscreen tooltipped leftwards " original-title="Zen Mode">
  										<span class="octicon octicon-screen-full"></span>
									</a>
								    <textarea name="comment[body]" tabindex="1" id="comment_body_461" placeholder="Leave a comment" class="comment-form-textarea js-comment-field js-size-to-fit js-quote-selection-target input-with-fullscreen-icon" data-suggester="461_new_preview_suggester"></textarea>
								</div>
 								<div id="preview_bucket_461" class="preview-content js-preview-bucket">
  									<div id="openstruct-69820763123120" class="comment js-comment js-task-list-container" data-body-version="">
   									 	<div class="comment-content">
											<div class="edit-comment-hide">
        										<div class="comment-body markdown-body  js-comment-body">
            										<p>Nothing to preview</p>
        										</div>
      										</div>
									    </div>
 									</div>
							    </div>
								<div class="suggester-container">
    								<div class="suggester js-navigation-container" id="461_new_preview_suggester" data-url="/lingrenzhang/workspace/suggestions/issue/27195547">
   									</div>
  								</div>
							</div>

      						<div class="form-actions">
   								<div id="js-new-comment-form-actions" class="js-socket-channel js-updatable-content" data-channel="lingrenzhang/workspace:issue:27195547:state" data-url="/lingrenzhang/workspace/issues/25/show_partial?partial=form_actions">
							    <button type="submit" name="comment_and_close" value="1" class="button js-comment-and-button" data-original-text="Close" data-comment-text="Close &amp; Comment" tabindex="3" data-disable-with="">
   										 Close
  								</button>
								<button type="submit" class="button primary" tabindex="2" data-disable-with="">
    									Comment
  								</button>
							</div>
      						</div>
					    </div>
					 </form>
				</div>

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
	    	<div class="bargin_wrapper">
	    		BarginInfo
	    	</div>
	    </div>

	</div>           
</div>
</body>
</html>


