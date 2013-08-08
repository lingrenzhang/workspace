package com.shs.liangdiaosi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shs.liangdiaosi.Access.UserTbAccess;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Check whether user is valid, default as valid now.
		HttpSession session;
		session = request.getSession();
		String UserName = request.getParameter("email"); 
		UserTbAccess userTb=new UserTbAccess();
		userTb.selectByName("");
		session.setAttribute("IsLogin", "true");
		session.setAttribute("UserName", request.getParameter("email"));
		session.setAttribute("", "");
		

		
		request.getSession().setMaxInactiveInterval(60*30);
		response.sendRedirect("/TicketSchedule/Postride.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = response.getWriter();
		StringBuffer address = request.getRequestURL();
		request.getSession().setAttribute("IsLogin", "true");
		request.getSession().setAttribute("UserName", request.getParameter("email"));
		request.getSession().setMaxInactiveInterval(60*30);
		response.sendRedirect("/TicketSchedule/Postride.jsp");
		
	}

}
