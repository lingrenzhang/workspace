package com.hitchride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllTopics;
import com.hitchride.global.DummyData;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
import com.hitchride.util.QueryStringParser;


/**
 * Servlet implementation class UserCenter
 */
public class UserCenter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserCenter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
		if (!islogin)
		{
			request.getSession().setAttribute("fromLocation", "/TicketSchedule/UserCenter.jsp");
			request.getSession().setAttribute("queryString", request.getQueryString());
			request.getSession().setMaxInactiveInterval(60*120);
			response.sendRedirect("/TicketSchedule/Login.jsp");
		}
		else
		{
			User user = (User) request.getSession().getAttribute("user");
			QueryStringParser qs = new QueryStringParser(request.getQueryString());
			String content = qs.getString("content");
			if (content.equalsIgnoreCase("messages"))
			{
				StringBuilder value = new StringBuilder();
			    value.append("<div class=\"panel panel-default\">");
				value.append("<div class=\"panel-heading\">Your message</div>");
				value.append("<div class=\"panel-body\">");
				for( Iterator<Message> mesI = user.message.iterator();mesI.hasNext();){ 
				    value.append(mesI.next().getHTMLMessage());
				}
				value.append("</div></div></div>");
				user.numofnewMessage = 0;
				response.getWriter().write(value.toString());
			}
			if (content.equalsIgnoreCase("topics"))
			{
				StringBuilder value = new StringBuilder(1000);
			    value.append("<div class=\"panel panel-default\">");
				value.append("<div class=\"panel-heading\">Topic you own</div>");
				value.append("<div class=\"panel-body\">");
				for(Iterator<OwnerRideInfo> rideI = user.tRides.iterator();rideI.hasNext(); )
				{
					OwnerRideInfo ownerRide = AllTopicRides.getTopicRides().getRide(rideI.next()._recordId);
					if (ownerRide!=null)
					{
						value.append("<a href=\"/TicketSchedule/servlet/RideCenter?topicId="+ownerRide._recordId+"\">");
						value.append("<div class=\"ride_wrapper\">");	
						value.append(ownerRide.getGeoHTML());
						value.append(ownerRide.getScheduleHTML());
						value.append("</div></a>");
						Topic topic= AllTopics.getTopics().get_topic(ownerRide._recordId);
						
					}
					
				}
				value.append("</div>");
				
				
			    value.append("<div class=\"panel panel-default\">");
				value.append("<div class=\"panel-heading\">Topic you participate</div>");
				value.append("<div class=\"panel-body\">");
				for(Iterator<ParticipantRide> prideI = user.pRides.iterator();prideI.hasNext(); )
				{
					ParticipantRide parRide = AllPartRides.getPartRides().get_participantRide(prideI.next()._pid);
					if (parRide.get_status()!=0)
					{
						
						value.append("<a href=\"/TicketSchedule/servlet/RideCenter?topicId="+parRide.get_assoOwnerRideId()+"\">");
						value.append("<div class=\"ride_wrapper\">");	
						value.append(parRide._rideInfo.getGeoHTML());
						value.append(parRide._rideInfo.getScheduleHTML());
						value.append("</div></a>");
						Topic topic= AllTopics.getTopics().get_topic(parRide.get_assoOwnerRideId());
					}
				}
				value.append("</div>");
				
				
			    value.append("<div class=\"panel panel-default\">");
				value.append("<div class=\"panel-heading\">Free ride</div>");
				value.append("<div class=\"panel-body\">");
				for(Iterator<ParticipantRide> prideI = user.pRides.iterator();prideI.hasNext(); )
				{
					ParticipantRide parRide = AllPartRides.getPartRides().get_participantRide(prideI.next()._pid);
					if (parRide.get_status()==0)
					{
						value.append("<a href=\"/TicketSchedule/servlet/Search\">");
						value.append("<div class=\"ride_wrapper\">");	
						value.append(parRide._rideInfo.getGeoHTML());
						value.append(parRide._rideInfo.getScheduleHTML());
						value.append("</div></a>");
					}
				}
				value.append("</div>");
				response.getWriter().write(value.toString());
			}
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
