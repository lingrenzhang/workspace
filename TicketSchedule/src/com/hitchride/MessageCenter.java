package com.hitchride;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.access.MessageTbAccess;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.Participant;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;

/**
 * Servlet implementation class MessageCenter
 */
public class MessageCenter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageCenter() {
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
			request.getSession().setAttribute("fromLocation", "/TicketSchedule/servlet/MessageCenter");
			request.getSession().setAttribute("queryString", request.getQueryString());
			request.getSession().setMaxInactiveInterval(60*30);
			response.sendRedirect("/TicketSchedule/Login.jsp");
		}
		else
		{
			String qs = request.getQueryString();
			String[] pars = qs.split("&");
			String id = pars[0].split("=")[1];
			String rid = pars[1].split("=")[1];
			String type = pars[2].split("=")[1];
			request.setAttribute("rid",rid );
		    Topic topic = new Topic(rid);
		    request.setAttribute("topic",topic);
		    User user = (User) request.getSession().getAttribute("user");
		    Boolean isOwnerMode = (user.get_uid() == topic.ownerRide.get_ownerId());
		    request.setAttribute("isOwnerMode", isOwnerMode);
		    Participant puser=null;
		    if (!isOwnerMode)
		    {
			    Boolean alreadyPart = false;
			    for(Iterator<ParticipantRide> prI = topic.parRides.iterator(); prI.hasNext();)
			    {
			    	    ParticipantRide pride=prI.next();
			    		if (pride.userId==user.get_uid())
			    		{
			    			puser = new Participant(pride.get_user());
			    			alreadyPart = true;
			    		}
			    }
			    if (!alreadyPart)
			    {
			    	puser = new Participant(user); //Wrapper user as participant
			    	puser.set_userStatus(0);

			    }
			    request.setAttribute("alreadyPart", alreadyPart);
			    request.setAttribute("participant", puser);
		    }
		    else
		    {
		    	
		    }
		    
			RequestDispatcher rd = request.getRequestDispatcher("/MessageCenter.jsp");
			rd.forward(request, response);
		}
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
		message.insertToDB();

		response.sendRedirect("/TicketSchedule/servlet/Search");
	}

}
