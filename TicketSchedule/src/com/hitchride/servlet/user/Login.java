package com.hitchride.servlet.user;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hitchride.User;
import com.hitchride.database.access.UserAccess;
import com.hitchride.environ.AllUsers;
import com.hitchride.environ.Environment;

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
		Environment.getEnv();
		String method = request.getParameter("method");
        if (method.equals("register")){
        	int i=1;
        }
		HttpSession session;
		session = request.getSession();
		String UserName = request.getParameter("email"); 
		
		UserAccess userTb=new UserAccess();
		ResultSet rs = userTb.selectByName(UserName,false);

		try {
			if (rs.next())
			{
				if(rs.getString("password").equalsIgnoreCase(request.getParameter("password")))
				{
					session.setAttribute("IsLogin", "true");

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
						if("Zh".equalsIgnoreCase(request.getParameter("language")))
						{
							response.sendRedirect("/TicketSchedule/Zh/SearchTransientRide.jsp");
						}
						else
						{
							response.sendRedirect("/TicketSchedule/SearchTransientRide.jsp");
						}
					}
				}
				else
				{
					request.setAttribute("isFailed", "true");
					if("Zh".equalsIgnoreCase(request.getParameter("language")))
					{
						response.sendRedirect("/TicketSchedule/Zh/Login.jsp?err=1");
					}
					else
					{
						response.sendRedirect("/TicketSchedule/Login.jsp?err=1");
					}
				}
			}
			else
			{
				request.setAttribute("isFailed", "true");
				if("Zh".equalsIgnoreCase(request.getParameter("language")))
				{
					response.sendRedirect("/TicketSchedule/Zh/Login.jsp?err=1");
				}
				else
				{
					response.sendRedirect("/TicketSchedule/Login.jsp?err=1");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

}
