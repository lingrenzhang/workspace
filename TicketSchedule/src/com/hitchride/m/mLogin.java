package com.hitchride.m;

/*
 mLogin API

 Method: POST
 Field: 
 email(as user name)
 password

 Return: json
 Format:
 {
 "status": "successful|failed",
 "reason": String,
 "redirect": target URL
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
import com.hitchride.User;
import com.hitchride.database.access.UserAccess;
import com.hitchride.environ.AllUsers;
import com.hitchride.environ.Environment;

/**
 * Servlet implementation class Login
 */
public class mLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class mLoginJson {
		String status = "failed";
		String reason = "";
		String redirect = "";
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public mLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		mLoginJson json = new mLoginJson();
		Environment.getEnv();

		HttpSession session;
		session = request.getSession();
		String UserName = request.getParameter("email");
		UserAccess userTb = new UserAccess();
		ResultSet rs = userTb.selectByName(UserName, false);

		try {
			if (rs.next()) {
				if (rs.getString("password").equalsIgnoreCase(
						request.getParameter("password"))) {

					session.setAttribute("IsLogin", "true");

					int UID = rs.getInt("userID");
					User user = (User) AllUsers.getUsers().getUser(UID);
					session.setAttribute("user", user);
					AllUsers.getUsers().addActiveUser(UID);

					request.getSession().setMaxInactiveInterval(60 * 120);

					json.status = "successful";
					json.redirect = "searchtransientride.html";
				} else {
					json.reason = "wrong password";
				}
			} else {
				json.reason = "wrong username";
			}
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(json));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
