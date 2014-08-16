package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.access.TransientRideAccess;
import com.hitchride.access.TransientTopicAccess;
import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllUsers;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.TransientRide;
import com.hitchride.standardClass.User;

/**
 * Servlet implementation class UpdateTParticipant
 */
public class UpdateTParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTParticipant() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(this)
		{
			String method = request.getParameter("method");
			if (method.equalsIgnoreCase("delete"))
			{
				int trid = Integer.parseInt(request.getParameter("trId"));
				int deleteId = Integer.parseInt(request.getParameter("deleteId"));
				TransientTopicAccess.removeParti(trid, deleteId);
				User user = (User) AllUsers.getUsers().getUser(deleteId);
				user.deletepTrideById(trid);
				
				TransientRide tride = TransientRideAccess.getTransisentRideById(trid);
				User touser = (User) AllUsers.getUsers().getUser(tride.userId);
				Message message = new Message(user,touser,1,tride,"");
				message.sendMessage();
				
				response.setStatus(200);
				response.getWriter().write("{status: OK}");
			}
			if (method.equalsIgnoreCase("insert"))
			{
				int trid = Integer.parseInt(request.getParameter("trId"));
				int insertId = Integer.parseInt(request.getParameter("insertId"));
				TransientTopicAccess.addParti(trid, insertId);
				User user = (User) AllUsers.getUsers().getUser(insertId);
				user.insertpTrideById(trid);
				
				TransientRide tride = TransientRideAccess.getTransisentRideById(trid);
				User touser = (User) AllUsers.getUsers().getUser(tride.userId);
				Message message = new Message(user,touser,0,tride,"");
				message.sendMessage();
				
				response.setStatus(200);
				response.getWriter().write("{status: OK}");
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("UpdateTParti called");
	}

}
