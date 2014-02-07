package com.hitchride;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.access.UserTbAccess;

/**
 * Servlet implementation class ShowUser
 */
public class ShowUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowUser() {
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
		UserTbAccess userDB=new UserTbAccess();
		ResultSet rs=userDB.selectByName(request.getParameter("name"),false);
		if (rs!=null)
		{
			request.setAttribute("results", rs);
			RequestDispatcher dispatcher = request.getRequestDispatcher("../ShowUser.jsp");
			dispatcher .forward(request, response); 
		}
		else
		{
		}
		
		
	}

}
