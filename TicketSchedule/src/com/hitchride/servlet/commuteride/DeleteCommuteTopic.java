package com.hitchride.servlet.commuteride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.GeoInfo;
import com.hitchride.TransientTopic;
import com.hitchride.User;
import com.hitchride.database.access.TransientRideAccess;
import com.hitchride.database.access.TransientTopicAccess;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllUsers;

/**
 * Servlet implementation class DeleteTopic
 */
public class DeleteCommuteTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCommuteTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(this)
		{
			String topicType = request.getParameter("topicType");
			if (topicType.equalsIgnoreCase("transient"))
			{
				int trid = Integer.parseInt(request.getParameter("trId"));
				int deleteId = Integer.parseInt(request.getParameter("deleteId"));
				TransientTopic tr = TransientTopicAccess.getTransientTopicById(trid);
				for (int i=0;i<tr.nParticipant;i++)
				{
					tr.parti[i].deletepTrideById(trid);
				}
				User holder = (User) AllUsers.getUsers().getUser(deleteId);
				holder.deletetTrideById(trid);
				
				
				TransientTopicAccess.deleteTransientTopic(trid);
				TransientRideAccess.deleteTransientRide(trid);
			
				response.setStatus(200);
				response.getWriter().write("{status: OK}");
			}
			if (topicType.equalsIgnoreCase("normal"))
			{
				
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
