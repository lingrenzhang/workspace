package com.hitchride.servlet.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hitchride.User;
import com.hitchride.database.access.UserAccess;
import com.hitchride.emailTemplate.RegisterTemplate;
import com.hitchride.environ.AllUsers;
import com.hitchride.util.SendMail;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserAccess userTb = new UserAccess();
		String userName = new String(request.getParameter("emailAddress")
				.getBytes("iso-8859-1"), "UTF-8");
		int groupId = Integer.parseInt(request.getParameter("groupID"));
		String password = request.getParameter("password");
		String givenname = new String(request.getParameter("givenname")
				.getBytes("iso-8859-1"), "UTF-8");
		String surname = new String(request.getParameter("surname").getBytes(
				"iso-8859-1"), "UTF-8");
		String address;
		if (request.getParameter("address") != null) {
			address = new String(request.getParameter("address").getBytes(
					"iso-8859-1"), "UTF-8");
		} else {
			address = "";
		}
		String avatarID = request.getParameter("avatarID"); // Not required
															// field
		String cellphone = request.getParameter("cellphone");
		if (avatarID == "")
			avatarID = null;

		String rootDir = request.getSession().getServletContext().getRealPath("");

		//This part only for development environment. Can fix code in runtime environment if required.
		Properties props=System.getProperties(); 
        String osname =  props.getProperty("os.name");
        System.out.println("Operation System is: "+osname);
        int last_sec_slash;
        if (osname.toUpperCase().contains("WIN"))
        {
        	int last_slash = rootDir.lastIndexOf("\\");
    		last_sec_slash = rootDir.lastIndexOf("\\", last_slash - 1);
        }
        else
        {
        	int last_slash = rootDir.lastIndexOf("/"); // use "\\" for windows os
    		last_sec_slash = rootDir.lastIndexOf("/", last_slash - 1);  // use "\\" for windows os
        }
		
		String realDir = rootDir.substring(0, last_sec_slash); // should be Tomcat Root
		String picDirPath = realDir + "/pics/tmp";

		ResultSet rs = userTb.selectByName(userName, false);
		try {
			if (rs.next()) {
				response.sendRedirect("../Zh/register.jsp?err=existed_user");
			} else {
				try {
					userTb.insertValue(userName, groupId, password, givenname,
							surname, address, 1, avatarID, cellphone);
					if(!(avatarID == null)){
						Path source = Paths.get(picDirPath + "/" + avatarID);
					    Path target = Paths.get(picDirPath.substring(0, picDirPath.lastIndexOf("/")) + "/" + avatarID);
					    Files.move(source, target);
					}
					RegisterTemplate t = new RegisterTemplate();
					SendMail s = new SendMail(t, userName);
					s.send();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int userid = userTb.getIDbyName(givenname);
				User user = new User();

				user.set_groupId(groupId);
				user.set_userLevel(1);
				user.set_uid(userid);
				user.set_emailAddress(userName);
				user.set_name(givenname);
				user.set_avatarID(avatarID);
				user._cellphone=cellphone;

				AllUsers.getUsers()._users.put(userid, user);
				AllUsers.getUsers().addActiveUser(userid);
				HttpSession session = request.getSession();
				session.setAttribute("IsLogin", "true");
				session.setAttribute("user", user);

				request.getSession().setMaxInactiveInterval(60 * 120);
				response.sendRedirect("../Zh/UserCenter.jsp");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
