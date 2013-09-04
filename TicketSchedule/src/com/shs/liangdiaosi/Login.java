package com.shs.liangdiaosi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		
		response.sendRedirect("/TicketSchedule/Login.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        String method = request.getParameter("method");
        if (method.equals("register")){
        	int i=1;
        }
		StringBuffer address = request.getRequestURL();
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
					session.setAttribute("userName", rs.getString("givenname"));
					session.setAttribute("emailAddress", rs.getString("emailAddress"));
					session.setAttribute("userLevel",rs.getString("userLevel"));
					session.setAttribute("avatarID", rs.getString("avatarID"));
					
					request.getSession().setMaxInactiveInterval(60*120);
					String from = (String) session.getAttribute("fromLocation");
					request.removeAttribute("fromLocation");
					if (from!=null)
					{
						String queryString = (String)session.getAttribute("queryString");
						session.removeAttribute("queryString");
						if (queryString == null)
						{
							response.sendRedirect(from);
						}
						else
						{
							response.sendRedirect(from+"?"+queryString);
						}
					}
					else
					{
						response.sendRedirect("/TicketSchedule/Postride.jsp");
					}
				}
				else
				{
					request.setAttribute("isFailed", "true");
					response.sendRedirect("/TicketSchedule/Login.jsp");
				}
			}
			else
			{
				request.setAttribute("isFailed", "true");
				response.sendRedirect("/TicketSchedule/Login.jsp");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

}
