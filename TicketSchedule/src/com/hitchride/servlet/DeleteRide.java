package com.hitchride.servlet;
//Only free ride is allowed to be deleted.
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteRide;
import com.hitchride.User;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllRides;

/**
 * Servlet implementation class DeleteRide
 */
public class DeleteRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		int rid = Integer.parseInt(request.getParameter("rid"));
		CommutePartiRide pride = AllPartRides.getPartRides().get_participantRide(rid);
		CommuteRide ride = AllRides.getRides().getRide(rid);
		if (pride==null || ride==null)
		{
			System.out.println("The ride to be delete does not exist. Check logic please.");
			response.getWriter().println("The ride to be delete does not exist. Check logic please.");
		}
		else
		{
			if (ride.get_user().get_uid()!=user.get_uid())
			{
				System.out.println("You are not allowed to delete the particular ride");
				response.getWriter().println("You are not allowed to delete the particular ride");
			}
			else
			{
				pride.delete();
				AllPartRides.getPartRides().remove(pride);
				user.pRides.remove(pride);
				ride.delete();
				AllRides.getRides().remove(ride);
				response.sendRedirect("/TicketSchedule/Zh/UserCenter.jsp");
				System.out.println("Ride "+ride.recordId + " has been deleted.");
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
