package com.hitchride;

/*
Login API

Method: POST
Field: email/password
Return: json
Format:
{
	  "status": "successful|failed",
	  "reason": String
}
*/    


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.hitchride.access.UserTbAccess;
import com.hitchride.global.AllUsers;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.User;

/**
 * Servlet implementation class Login
 */
public class mLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    class mLoginJson {
    	String status="failed";
    	String reason="";
    }
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		mLoginJson json = new mLoginJson();;
		Environment.getEnv();
		
		HttpSession session;
		session = request.getSession();
		String UserName = request.getParameter("email"); 
		UserTbAccess userTb=new UserTbAccess();
		ResultSet rs = userTb.selectByName(UserName,false);

		try {
			if (rs.next())
			{
				if(rs.getString("password").equalsIgnoreCase(request.getParameter("password")))
				{
					
					session.setAttribute("IsLogin", "true");

					/*
					user.set_name(rs.getString("givenname"));
					user.set_uid(rs.getInt("userid"));
					user.set_avatarID(rs.getString("avatarID"));
					user.set_userLevel(rs.getString("userLevel"));
					user.set_emailAddress(rs.getString("emailAddress"));
					session.setAttribute("user", user);
					*/

					int UID=rs.getInt("userID");
					User user = (User) AllUsers.getUsers().getUser(UID);
					session.setAttribute("user", user);
					AllUsers.getUsers().addActiveUser(UID);
					
					request.getSession().setMaxInactiveInterval(60*120);
					String from = (String) session.getAttribute("fromLocation");
					request.removeAttribute("fromLocation");
					if (from!=null)
					{
						String queryString = (String)session.getAttribute("queryString");
						session.removeAttribute("queryString");
					}
					else
					{
					}
					json.status="successful";
				}
				else
				{
					json.reason="wrong password";
				}
			}
			else
			{
				json.reason="wrong username";
			}
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(json));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}

}