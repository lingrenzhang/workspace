package com.hitchride.servlet.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.database.access.UserAccess;
import com.hitchride.database.access.UserGroupAccess;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class UniqueUsernameCheck
 */
public class UniqueUsernameCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UniqueUsernameCheck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		QueryStringParser qsp =  new QueryStringParser(request.getQueryString());
		String username=java.net.URLDecoder.decode(qsp.getString("username"), "UTF-8");

		boolean isUsernameUnique = UserAccess.checkUsernameUnique(username);

		{
			if(isUsernameUnique)
			{
				response.getWriter().write("0");
			}
			else
			{
				response.getWriter().write("1");
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
