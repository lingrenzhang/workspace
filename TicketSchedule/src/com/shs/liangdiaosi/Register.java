package com.shs.liangdiaosi;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shs.liangdiaosi.Access.UserTbAccess;
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
		String password = request.getParameter("password");
		String givenname = request.getParameter("givenname");
		String surename = request.getParameter("surename");
		String address = request.getParameter("address");
		userTb.insertValue(userName, password, givenname, surename, address, 1);

		HttpSession session = request.getSession();
		session.setAttribute("IsLogin", "true");
		session.setAttribute("userName", givenname);
		session.setAttribute("emailAddress", userName);
		session.setAttribute("userLevel",1);
					
		request.getSession().setMaxInactiveInterval(60*120);
		response.sendRedirect("/TicketSchedule/Postride.jsp");
	}

}
