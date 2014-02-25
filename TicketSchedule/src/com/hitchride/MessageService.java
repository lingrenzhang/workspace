package com.hitchride;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllRides;
import com.hitchride.global.AllTopics;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
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
			String messageContent = request.getParameter("comment");
			User from = (User) Environment.getEnv().getUser(fromId);
			User to = (User) Environment.getEnv().getUser(toId);
			Message message = new Message(messageContent,from,to,topic);
			message.sendMessage();

		}
		response.sendRedirect("/TicketSchedule/servlet/RideCenter?topicId="+topic.get_topicId()+"&type=commute");

	}

}
