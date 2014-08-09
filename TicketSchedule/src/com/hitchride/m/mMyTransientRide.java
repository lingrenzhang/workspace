package com.hitchride.m;

/*
 mMyTransientRide API

 Method: GET
 Field: 

 Return: json
 Format:
 See Class mMyTransientRideJson for detail
 */

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.hitchride.access.TransientRideAccess;
import com.hitchride.access.TransientTopicAccess;
import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllTopics;
import com.hitchride.m.mPostTransientRide.mPostTransientRideJson;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.TransientRide;
import com.hitchride.standardClass.TransientTopic;
import com.hitchride.standardClass.User;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class TransientRideCenter
 */
public class mMyTransientRide extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class mUser {
		int uid = 0;
		String name = "";
		String pic = "";
		String cellPhone = "";
	}

	class mLoc {
		double lat = 0.0;
		double lon = 0.0;
		String addr = "";
	}

	class mRide {
		int trId = 0;
		mUser owner = new mUser();
		mLoc origLoc = new mLoc();
		mLoc destLoc = new mLoc();
		int dist = 0;
		int dura = 0;
		String date = "";
		String time = "";
		String flexTime = "";
		boolean userType = false;
		int totalSeats = 0;
		int availSeats = 0;
		double price = 0;
	}

	class mMyTransientRideJson {
		String status = "failed";
		String reason = "";
		String redirect = "";
		mRide[] tRides;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public mMyTransientRide() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		mMyTransientRideJson json = new mMyTransientRideJson();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			json.reason = "not login";
			json.redirect = "login.html";
		} else {
			user.refreshtTride();
			json.tRides = new mRide[user.tTride.size()];
			System.out.println(user.tTride.size());
			int i=0;
			for (Iterator<Integer> rideI = user.tTride.iterator(); rideI
					.hasNext();) {
				int trId = rideI.next();
				TransientRide tride = TransientRideAccess
						.getTransisentRideById(trId);
				if (tride != null) {
					mRide mTride= new mRide();
					mTride.trId = trId;

					mTride.owner.uid = tride.owner.get_uid();
					mTride.owner.name = tride.owner.get_name();
					mTride.owner.pic = tride.owner.get_avatarID();
					mTride.owner.cellPhone = tride.owner._cellphone;

					mTride.origLoc.lat = tride.origLoc.get_lat();
					mTride.origLoc.lon = tride.origLoc.get_lon();
					mTride.origLoc.addr = tride.origLoc._addr;

					mTride.destLoc.lat = tride.destLoc.get_lat();
					mTride.destLoc.lon = tride.destLoc.get_lon();
					mTride.destLoc.addr = tride.destLoc._addr;

					mTride.dist = tride.dist;
					mTride.dura = tride.dura;

					mTride.date = tride.rideDate.toString();
					mTride.time = tride.rideTime.toString();

					mTride.flexTime = tride.rideFlex.toString();
					mTride.userType = tride.userType;
					mTride.totalSeats = tride.totalSeats;
					mTride.availSeats = tride.availSeats;
					mTride.price = tride.price;
					json.tRides[i]=mTride;
				}
				i++;
			}
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
