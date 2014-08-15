package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllTopics;
import com.hitchride.global.AllUsers;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;

/**
 * Servlet implementation class MessageService
 */
public class MessageService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(this)
		{
			String method = request.getParameter("method");
			int topicId = Integer.parseInt(request.getParameter("topicId"));
			//OwnerRideInfo ownRide = (OwnerRideInfo) AllRides.getRides().getOwnerRide(rideId);
			Topic topic = AllTopics.getTopics().get_topic(topicId);
			if (method.equalsIgnoreCase("delete"))
			{
				//Should be message unique ID finally.
			}
			if (method.equalsIgnoreCase("create"))
			{
				int fromId = Integer.parseInt(request.getParameter("fromId"));
				int toId = Integer.parseInt(request.getParameter("toId"));
				String messageContent = new String(request.getParameter("comment").getBytes("iso-8859-1"), "UTF-8");
				User from = (User) AllUsers.getUsers().getUser(fromId);
				User to = (User) AllUsers.getUsers().getUser(toId);
				Message message = new Message(messageContent,from,to,topic);
				message.sendMessage();
	
			}
			response.sendRedirect("/TicketSchedule/servlet/RideCenter?topicId="+topic.get_topicId()+"&type=commute");
		}
	}

}
