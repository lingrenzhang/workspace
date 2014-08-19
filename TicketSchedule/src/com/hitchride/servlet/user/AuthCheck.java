package com.hitchride.servlet.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.database.access.UserGroupTbAccess;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class AuthCheck
 */
public class AuthCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthCheck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QueryStringParser qsp =  new QueryStringParser(request.getQueryString());
		String authCode=qsp.getString("authCode");
		int groupId = UserGroupTbAccess.checkAuth(authCode);
		{
			if (groupId==-1) 
			{
				response.getWriter().write("Invalid auth code. Leave it blank or check the code");
			}
			else
			{
				response.getWriter().write(""+groupId);
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
