package com.hitchride.m;

/*
 mDelTransientRide API

 Method: POST
 Field: 
 trId: TransientRide ID

 Return: json
 Format:
 {
 "status": "successful|failed",
 "reason": String,
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
import com.hitchride.CommutePartiRide;
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
import com.hitchride.environ.AllUsers;
import com.hitchride.m.mPostTransientRide.mPostTransientRideJson;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class TransientRideCenter
 */
public class mDelTransientRide extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class mDelTransientRideJson {
		String status = "failed";
		String reason = "";
		String redirect = "";
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public mDelTransientRide() {
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
		mDelTransientRideJson json = new mDelTransientRideJson();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			json.reason = "not login";
			json.redirect = "login.html";
		} else {
			int trId = Integer.parseInt(request.getParameter("trId"));
			TransientRide tride = TransientRideAccess
					.getTransisentRideById(trId);
			TransientTopic ttopic = TransientTopicAccess
					.getTransientTopicById(trId);
			if (tride == null) {
				json.reason = "no such trId";
			} else {
				int ownerUid = tride.owner.get_uid();
				if (ownerUid == user.get_uid()) {
					for (int i = 0; i < ttopic.nParticipant; i++) {
						ttopic.parti[i].deletepTrideById(trId);
					}
					User holder = (User) AllUsers.getUsers().getUser(ownerUid);
					holder.deletetTrideById(trId);

					TransientTopicAccess.deleteTransientTopic(trId);
					TransientRideAccess.deleteTransientRide(trId);
					json.status = "successful";
					json.redirect = "searchtransientride.html";
				} else {
					json.reason = "not owner";
				}
			}
			Gson gson = new Gson();
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(gson.toJson(json));
		}
	}
}