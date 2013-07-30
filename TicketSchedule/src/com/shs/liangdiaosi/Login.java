package com.shs.liangdiaosi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		PrintWriter out = response.getWriter();
		request.getSession().setAttribute("IsLogin", "true");
		request.getSession().setMaxInactiveInterval(60*30);
		out.print("User: "+request.getParameter("username")+ " logon");
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
		request.getSession().setMaxInactiveInterval(60*30);
		out.print("User: "+request.getParameter("email")+ " logon");
		out.print(address);
		response.sendRedirect("/TicketSchedule/Postride.jsp");
		
	}

}
