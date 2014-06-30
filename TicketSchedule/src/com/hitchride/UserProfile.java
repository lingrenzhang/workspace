package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.access.UserTbAccess;
import com.hitchride.standardClass.User;

/**
 * Servlet implementation class UserProfile
 */
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String givenname = request.getParameter("givenname");
		String surename = request.getParameter("surename");
		String address = request.getParameter("address");
		String oldpwd =request.getParameter("oldpwd");
		String newpwd =request.getParameter("newpwd");
		int uid = Integer.parseInt(request.getParameter("uid"));
		UserTbAccess.updateUserProfile(givenname, surename, address, uid);
		response.sendRedirect("/TicketSchedule/UserCenter.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String givenname = new String(request.getParameter("givenname").getBytes("iso-8859-1"), "UTF-8");
		String surename = new String(request.getParameter("surename").getBytes("iso-8859-1"), "UTF-8");
		String address = new String(request.getParameter("address").getBytes("iso-8859-1"), "UTF-8");
		String oldpwd =request.getParameter("oldpwd");
		String newpwd =request.getParameter("newpwd");
		
		int uid = Integer.parseInt(request.getParameter("uid"));
		if (newpwd.equals("")|| newpwd.equals("null"))
		{
			
			UserTbAccess.updateUserProfile(givenname, surename, address, uid);
			User user = (User) request.getSession().getAttribute("user");
			user.set_name(givenname);
			user.set_surename(surename);
			response.sendRedirect("/TicketSchedule/Zh/UserCenter.jsp");
		}
		else
		{
			if (UserTbAccess.verifyPassword(uid, oldpwd))
			{
				UserTbAccess.updateUserProfile(givenname, surename, address,newpwd,uid);
				User user = (User) request.getSession().getAttribute("user");
				user.set_name(givenname);
				user.set_surename(surename);
				response.sendRedirect("/TicketSchedule/Zh/UserCenter.jsp");
			}
			else
			{
				response.sendRedirect("/TicketSchedule/Zh/UserCenter.jsp?content=profile&msg=wrongPWD");
			}
		}
	}

}
