package com.hitchride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllTopics;
import com.hitchride.standardClass.Message;
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
			    		if (pride._rideinfo.get_user().get_uid()==user.get_uid())
			    		{
			    			alreadyPart = true;
			    		}

			    }
			    for(Iterator<ParticipantRide> prI = topic._requestPride.iterator(); prI.hasNext();)
			    {
			    	    ParticipantRide pride=prI.next();
			    		if (pride._rideinfo.get_user().get_uid()==user.get_uid())
			    		{
			    			alreadyPart = true;
			    		}

			    }
			    if (!alreadyPart)
			    {
			    	RideInfo ride = (RideInfo) request.getSession().getAttribute("actRide");
			    	ParticipantRide pride = new ParticipantRide(ride);
			    	pride.set_status(0);
				    request.setAttribute("participantRide", pride);

			    }
			    request.setAttribute("alreadyPart", alreadyPart);
		    }
		    else
		    {
		    	
		    }
		    
			RequestDispatcher rd = request.getRequestDispatcher("/RideCenter.jsp");
			rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int from = Integer.parseInt(request.getParameter("from"));
		int to = Integer.parseInt(request.getParameter("to"));
		String messageContent = request.getParameter("notes");
		int recordID = Integer.parseInt(request.getParameter("recordID"));
		Message message = new Message(messageContent,from,to,recordID);
		
		Topic topic  = (Topic) request.getSession().getAttribute("topic");
		topic.messages.add(message);
		message.insertToDB();

		response.sendRedirect("/TicketSchedule/servlet/Search");
	}

}
