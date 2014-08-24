//This Service is used to generate message content for commute topic center
//Use broadcast here

package com.hitchride.servlet.commuteride;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.CommuteTopic;
import com.hitchride.Message;
import com.hitchride.User;
import com.hitchride.environ.AllTopics;
import com.hitchride.environ.AllUsers;

/**
 * Servlet implementation class CommuteMessageService
 */
public class CommuteMessageService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommuteMessageService() {
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
			CommuteTopic topic = AllTopics.getTopics().get_topic(topicId);
			if (method.equalsIgnoreCase("delete"))
			{
				//Should be message unique ID finally.
			}
			if (method.equalsIgnoreCase("create"))
			{
				int fromId = Integer.parseInt(request.getParameter("fromId"));
				User from = (User) AllUsers.getUsers().getUser(fromId);
				
				int toId = Integer.parseInt(request.getParameter("toId"));
				User to = (User) AllUsers.getUsers().getUser(toId);
				
				String messageContent = new String(request.getParameter("comment").getBytes("iso-8859-1"), "UTF-8");
				Message message = new Message(messageContent,from,to,topic);
				message.sendMessage();
				/*
				List<User> targetlist = topic.getUsersExcept(fromId);
				for(Iterator<User> itarget = targetlist.iterator();itarget.hasNext();)
				{
					User to = itarget.next();
					Message message = new Message(messageContent,from,to,topic);
					message.sendMessage();
				}
				*/
			}
			response.sendRedirect("/TicketSchedule/CommuteTopicCenter?topicId="+topic.get_topicId()+"&type=commute");
		}
	}

}
