package com.hitchride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllRides;
import com.hitchride.global.AllTopics;
import com.hitchride.global.DummyData;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
import com.hitchride.util.QueryStringParser;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

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
		User user = (User) request.getSession().getAttribute("user");
		QueryStringParser qs = new QueryStringParser(request.getQueryString());
		String content = qs.getString("content");
		if (content.equalsIgnoreCase("messages"))
		{
			StringBuilder value = new StringBuilder();
			value.append("<div class=\"panel-heading\">Your message</div>");
			value.append("<div class=\"panel-body\">");
			for( Iterator<Message> mesI = user.message.iterator();mesI.hasNext();){ 
			    value.append(mesI.next().getHTMLMessage());
			}
			value.append("</div></div>");
			user.numofnewMessage = 0;
			response.getWriter().write(value.toString());
		}
		if (content.equalsIgnoreCase("topics"))
		{
			StringBuilder value = new StringBuilder(500);
			String topicType = qs.getString("topicType");
			if (topicType.equalsIgnoreCase("own"))
			{
				value.append("<div class=\"panel-heading\">Topic you own</div>");
				value.append("<div class=\"panel-body\">");
				for(Iterator<RideInfo> rideI = user.rides.iterator();rideI.hasNext(); )
				{
					OwnerRideInfo ownerRide = AllRides.getRides().getOwnerRide(rideI.next().recordId);
					if (ownerRide!=null)
					{
						value.append("<a href=\"/TicketSchedule/servlet/RideCenter?topicId="+ownerRide.recordId+"\">");
						value.append("<div class=\"ride_wrapper\">");	
						value.append(ownerRide.getGeoHTML());
						value.append(ownerRide.getScheduleHTML());
						value.append("</div></a>");
						Topic topic= AllTopics.getTopics().get_topic(ownerRide.recordId);
						
					}
				}
				response.getWriter().write(value.toString());
			}
			
			if (topicType.equalsIgnoreCase("participate"))
			{
				value.append("<div class=\"panel-heading\">Topic you participate</div>");
				value.append("<div class=\"panel-body\">");
				for(Iterator<RideInfo> rideI = user.rides.iterator();rideI.hasNext(); )
				{
					ParticipantRide parRide = DummyData.getDummyEnv().get_participantRide(rideI.next().recordId);
					if (parRide!=null)
					{
						value.append("<a href=\"/TicketSchedule/servlet/RideCenter?topicId="+parRide.get_assoOwnerRideId()+"\">");
						value.append("<div class=\"ride_wrapper\">");	
						value.append(parRide.getGeoHTML());
						value.append(parRide.getScheduleHTML());
						value.append("</div></a>");
						Topic topic= AllTopics.getTopics().get_topic(parRide.get_assoOwnerRideId());
					}
				}
				response.getWriter().write(value.toString());
			}
			
			if (topicType.equalsIgnoreCase("free"))
			{
				value.append("<div class=\"panel-heading\">Free ride</div>");
				value.append("<div class=\"panel-body\">");
				for(Iterator<RideInfo> rideI = user.rides.iterator();rideI.hasNext(); )
				{
					RideInfo ride = rideI.next();
					ParticipantRide parRide = DummyData.getDummyEnv().get_participantRide(ride.recordId);
					OwnerRideInfo ownerRide = AllRides.getRides().getOwnerRide(ride.recordId);
					if (parRide==null && ownerRide==null)
					{
						request.getSession().setAttribute("actRide", ride);
						value.append("<a href=\"/TicketSchedule/servlet/Search\">");
						value.append("<div class=\"ride_wrapper\">");	
						value.append(ride.getGeoHTML());
						value.append(ride.getScheduleHTML());
						value.append("</div></a>");
					}
				}
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
