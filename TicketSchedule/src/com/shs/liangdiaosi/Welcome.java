package com.shs.liangdiaosi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shs.liangdiaosi.Access.UserTbAccess;

/**
 * Servlet implementation class Welcome
 */
public class Welcome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Welcome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter returnHTML;
		response.setContentType("text/html;charset=utf-8");
		returnHTML = response.getWriter();
		returnHTML.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		returnHTML.println("<html><head><title>");
		returnHTML.println("A simple GET servlet");
		returnHTML.println("</title></head><body>");
		returnHTML.println("<h2>Welcome! Greetings, this it the servlet answering</h2>");
		returnHTML.println("</body></html>");
		returnHTML.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		UserTbAccess userDB=new UserTbAccess();
		userDB.insertValue(request.getParameter("name"), request.getParameter("pwd"), "","");
		response.sendRedirect("../ShowUser.jsp");

	}

}
