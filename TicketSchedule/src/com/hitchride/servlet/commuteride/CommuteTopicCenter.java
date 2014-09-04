package com.hitchride.servlet.commuteride;
//Pre-process of different cases of how the CommuteTopicCenter is loaded.
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.Message;
import com.hitchride.CommuteOwnerRide;
import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteRide;
import com.hitchride.CommuteTopic;
import com.hitchride.User;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllRides;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllTopics;
import com.hitchride.environ.AllUsers;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class RideCenter
 */
public class CommuteTopicCenter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommuteTopicCenter() {
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
				request.getSession().setAttribute("fromLocation", "/TicketSchedule/CommuteTopicCenter");
				request.getSession().setAttribute("queryString", request.getQueryString());
				request.getSession().setMaxInactiveInterval(60*120);
				response.sendRedirect("/TicketSchedule/Login.jsp");
			}
			else{
				QueryStringParser qsPar = new QueryStringParser(request.getQueryString());
				int topicId = qsPar.getInt("topicId");
			    CommuteTopic topic = AllTopics.getTopics().get_topic(topicId);
			    request.getSession().setAttribute("topic",topic);
			    User user = (User) request.getSession().getAttribute("user");
			    Boolean isOwnerMode = (user.get_uid() == topic.ownerRide._rideInfo.get_user().get_uid());
			    request.setAttribute("isOwnerMode", isOwnerMode);
				
			    CommuteRide ride=null;
			    ride = AllRides.getRides().getRide(topicId);
			    try
			    {
			    	int partiId = qsPar.getInt("partiId");
			    	ride = AllRides.getRides().getRide(partiId); 
			    	request.getSession().setAttribute("actRide", ride);
			    }
			    catch(Exception e)
			    {
			    	//Owner mode or new ride.
			    	System.out.println("No partiId invovled.");
			    }
			    
			    if (ride== null) //Check if there is tempor active ride.
			    {
			    	ride = (CommuteRide) request.getSession().getAttribute("actRide");
			    }
				if (ride==null && !isOwnerMode )
				{
					//Participant is not holding any ride. Force client to create ride.
					response.sendRedirect("/TicketSchedule/Zh/ManageRide.jsp"); 
				}
				else
				{
					if(ride.id==0) //This is a new ride 
					{
						AllRides.getRides().insert_availride(ride);
						ride.insertToDB();

						if (AllPartRides.getPartRides().get_participantRide(ride.id)==null)
						{
							CommutePartiRide pride = new CommutePartiRide(ride);
							pride.set_status(0);
							pride.set_assoOwnerRideId(-1);
							user.pRides.add(pride);
							AllPartRides.getPartRides().insert_pride(pride);
							pride.insertToDB();
						}
					}
					
				    if (!isOwnerMode)
				    {
					    Boolean alreadyPart = false;
					    for(Iterator<CommutePartiRide> prI = topic.parRides.iterator(); prI.hasNext();)
					    {
					    	    CommutePartiRide pride=prI.next();
					    		if (pride._rideInfo.get_user().get_uid()==user.get_uid())
					    		{
					    			alreadyPart = true;
					    		}
					    }
					    for(Iterator<CommutePartiRide> prI = topic._requestPride.iterator(); prI.hasNext();)
					    {
					    	    CommutePartiRide pride=prI.next();
					    		if (pride._rideInfo.get_user().get_uid()==user.get_uid())
					    		{
					    			alreadyPart = true;
					    		}
					    }
					    if (!alreadyPart)
					    {
					    	CommutePartiRide pride = new CommutePartiRide(ride);
					    	pride.set_status(0);
						    request.setAttribute("participantRide", pride);
					    }
					    request.setAttribute("alreadyPart", alreadyPart);
				    }
				    else
				    {
				    	
				    }
				    
					RequestDispatcher rd = request.getRequestDispatcher("/Zh/CommuteTopicCenter.jsp");
					rd.forward(request, response);
				}
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
		CommuteRide ride = (CommuteRide) request.getSession().getAttribute("actRide");
		if (ride==null)
		{
			response.sendRedirect("/TicketSchedule/Zh/ManageRide.jsp");
		}
		else
		{
			if(ride.id==0) //The actRide is temp and has not been registered.
			{
				AllRides.getRides().insert_availride(ride);
				ride.insertToDB();
			}
			CommuteOwnerRide ownRide = new CommuteOwnerRide(ride);
			ride.get_user().tRides.add(ownRide);
			CommutePartiRide pRide = AllPartRides.getPartRides().get_participantRide(ride.id);
			if (pRide!=null)
			{
				ride.get_user().pRides.remove(pRide);
				pRide.delete();
				AllPartRides.getPartRides().remove(ride.id);
			}
			
			AllTopicRides.getTopicRides().insert_TopicRide(ownRide);
			ownRide.insertToDB();
			CommuteTopic topic= new CommuteTopic();
			topic.ownerRide=ownRide;
			topic.set_topicId(ride.id);
			topic.owner=ride.get_user();
			AllTopics.getTopics().insert_topic(topic);
			topic.insertToDB();
			response.sendRedirect("/TicketSchedule/CommuteTopicCenter?topicId="+ride.id);
		}
	}

}
