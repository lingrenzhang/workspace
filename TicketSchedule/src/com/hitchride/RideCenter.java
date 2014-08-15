package com.hitchride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllTopics;
import com.hitchride.global.AllUsers;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class RideCenter
 */
public class RideCenter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RideCenter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		{
		    boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
			if (!islogin)
			{
				request.getSession().setAttribute("fromLocation", "/TicketSchedule/servlet/RideCenter");
				request.getSession().setAttribute("queryString", request.getQueryString());
				request.getSession().setMaxInactiveInterval(60*120);
				response.sendRedirect("/TicketSchedule/Login.jsp");
			}
			else{
				
				RideInfo ride = (RideInfo) request.getSession().getAttribute("actRide");
				if (ride==null)
				{
					response.sendRedirect("/TicketSchedule/Zh/ManageRide.jsp");
				}
				
				
				
				QueryStringParser qsPar = new QueryStringParser(request.getQueryString());
				int topicId = qsPar.getInt("topicId");
			    Topic topic = AllTopics.getTopics().get_topic(topicId);
			    request.getSession().setAttribute("topic",topic);
			    User user = (User) request.getSession().getAttribute("user");
			    Boolean isOwnerMode = (user.get_uid() == topic.ownerRide._rideInfo.get_user().get_uid());
			    request.setAttribute("isOwnerMode", isOwnerMode);
			    
			    if (!isOwnerMode)
			    {
				    Boolean alreadyPart = false;
				    for(Iterator<ParticipantRide> prI = topic.parRides.iterator(); prI.hasNext();)
				    {
				    	    ParticipantRide pride=prI.next();
				    		if (pride._rideInfo.get_user().get_uid()==user.get_uid())
				    		{
				    			alreadyPart = true;
				    		}
	
				    }
				    for(Iterator<ParticipantRide> prI = topic._requestPride.iterator(); prI.hasNext();)
				    {
				    	    ParticipantRide pride=prI.next();
				    		if (pride._rideInfo.get_user().get_uid()==user.get_uid())
				    		{
				    			alreadyPart = true;
				    		}
				    }
				    if (!alreadyPart)
				    {
				    	ParticipantRide pride = new ParticipantRide(ride);
				    	pride.set_status(0);
					    request.setAttribute("participantRide", pride);
				    }
				    request.setAttribute("alreadyPart", alreadyPart);
			    }
			    else
			    {
			    	
			    }
			    
				RequestDispatcher rd = request.getRequestDispatcher("/Zh/RideCenter.jsp");
				rd.forward(request, response);
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		int from = Integer.parseInt(request.getParameter("from"));
		int to = Integer.parseInt(request.getParameter("to"));
		String messageContent = request.getParameter("notes");
		int recordID = Integer.parseInt(request.getParameter("recordID"));
		Message message = new Message(messageContent,from,to,recordID);
		
		Topic topic  = (Topic) request.getSession().getAttribute("topic");
		topic.messages.add(message);
		message.insertToDB();

		response.sendRedirect("/TicketSchedule/servlet/Search");
		*/
		RideInfo ride = (RideInfo) request.getSession().getAttribute("actRide");
		if (ride==null)
		{
			response.sendRedirect("/TicketSchedule/Zh/ManageRide.jsp");
		}
		else
		{
			OwnerRideInfo ownRide = new OwnerRideInfo(ride);
			ride.get_user().tRides.add(ownRide);
			ParticipantRide pRide = AllPartRides.getPartRides().get_participantRide(ride.recordId);
			ride.get_user().pRides.remove(pRide);
			pRide.delete();
			
			AllPartRides.getPartRides().remove(ride.recordId);
			
			AllTopicRides.getTopicRides().insert_TopicRide(ownRide);
			ownRide.insertToDB();
			Topic topic= new Topic();
			topic.ownerRide=ownRide;
			topic.set_topicId(ride.recordId);
			topic.owner=ride.get_user();
			AllTopics.getTopics().insert_topic(topic);
			topic.insertToDB();
			response.sendRedirect("/TicketSchedule/servlet/RideCenter?topicId="+ride.recordId);
		}
	}

}
