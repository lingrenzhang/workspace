package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hitchride.access.UserTbAccess;
import com.hitchride.global.AllUsers;
import com.hitchride.standardClass.User;
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
		int groupId = Integer.parseInt(request.getParameter("groupID"));
		String password = request.getParameter("password");
		String givenname = request.getParameter("givenname");
		String surname = request.getParameter("surname");
		String address = request.getParameter("address");
		String avatarID = request.getParameter("avatarID"); //Not required field
		if (avatarID==null)
			avatarID="default.jpg";
		userTb.insertValue(userName,groupId, password, givenname, surname, address, 1,avatarID);
		int userid = userTb.getIDbyName(givenname);
		User user = new User();
		
		user.set_groupId(groupId);
		user.set_userLevel(1);
		user.set_uid(userid);
		user.set_emailAddress(userName);
		user.set_name(givenname);
		
		AllUsers.getUsers()._users.put(userid, user);
		AllUsers.getUsers().addActiveUser(userid);
		HttpSession session = request.getSession();
		session.setAttribute("IsLogin", "true");
		session.setAttribute("user", user);
					
		request.getSession().setMaxInactiveInterval(60*120);
		response.sendRedirect("/TicketSchedule/UserCenter.jsp");
	}

}
