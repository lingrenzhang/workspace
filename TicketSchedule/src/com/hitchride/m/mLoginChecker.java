package com.hitchride.m;

/*
 mLoginChecker API

 Method: GET
 Field: 

 Return: json
 Format:
 {
 "status": "successful|failed",
 "reason": String,
 "uid": int,
 "redirect": target URL
 }
 */

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.hitchride.CommuteOwnerRide;
import com.hitchride.CommuteParticipantRide;
import com.hitchride.CommuteRide;
import com.hitchride.CommuteTopic;
import com.hitchride.TransientRide;
import com.hitchride.TransientTopic;
import com.hitchride.User;
import com.hitchride.database.access.TransientRideAccess;
import com.hitchride.database.access.TransientTopicAccess;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllTopics;
import com.hitchride.m.mPostTransientRide.mPostTransientRideJson;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class TransientRideCenter
 */
public class mLoginChecker extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class mLoginCheckerJson {
		String status = "failed";
		String reason = "";
		int uid = 0;
		String redirect = "";
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public mLoginChecker() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		mLoginCheckerJson json = new mLoginCheckerJson();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			json.reason = "not login";
			json.redirect = "login.html";
		} else {
			json.redirect = "searchtransientride.html";
			json.uid = user.get_uid();
			json.status = "successful";
		}
		Gson gson = new Gson();
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(gson.toJson(json));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}
}
