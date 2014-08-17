package com.hitchride.servlet.commuteride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllPartRides;

/**
 * Servlet implementation class UpdateParticipant
 */
public class UpdateParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateParticipant() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(this)
		{
			boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
			if (!islogin)
			{
				request.getSession().setAttribute("fromLocation", "/TicketSchedule/CommuteTopicCenter");
				request.getSession().setAttribute("queryString", request.getQueryString());
				request.getSession().setMaxInactiveInterval(60*120);
				response.sendRedirect("/TicketSchedule/Login.jsp");
			}
			else
			{
				String qs = request.getQueryString();
				String[] pars = qs.split("&");
				int prid = Integer.parseInt( pars[0].split("=")[1]);
				
				int pstatus = Integer.parseInt(pars[1].split("=")[1]);
	
				AllPartRides.getPartRides().updatePartRideStat(prid,pstatus);
				response.getWriter().write("Done");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
