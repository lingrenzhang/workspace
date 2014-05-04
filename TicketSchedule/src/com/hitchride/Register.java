package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hitchride.access.UserTbAccess;
/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		// TODO Auto-generated method stub
		UserTbAccess  userTb = new UserTbAccess();
		String userName = request.getParameter("emailAddress");
		String groupId = request.getParameter("groupID");
		String password = request.getParameter("password");
		String givenname = request.getParameter("givenname");
		String surname = request.getParameter("surname");
		String address = request.getParameter("address");
		String avatarID = request.getParameter("avatarID"); //Not required field
		if (avatarID==null)
			avatarID="default.jpg";
		userTb.insertValue(userName, password, givenname, surname, address, 1,avatarID);

		HttpSession session = request.getSession();
		session.setAttribute("IsLogin", "true");
		session.setAttribute("userName", givenname);
		session.setAttribute("emailAddress", userName);
		session.setAttribute("userLevel",1);
		session.setAttribute("avatarID",avatarID);
					
		request.getSession().setMaxInactiveInterval(60*120);
		response.sendRedirect("/TicketSchedule/UserCenter.jsp");
	}

}
