package com.hitchride;

/*
mPostTransientRide API

Method: GET
Field: 
trId: TransientRide ID

Return: json
Format:
{
"status": "successful|failed",
"reason": String,
"trId": TransientRide ID,
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
import com.hitchride.access.TransientRideAccess;
import com.hitchride.access.TransientTopicAccess;
import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllTopics;
import com.hitchride.mPostTransientRide.mPostTransientRideJson;
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
public class mGetTransientRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class User{
		int uid=0;
		String name="";
		String pic="";
		String cellPhone="";
	}
	
	class Loc {
		double lat=0.0;
		double lon=0.0;
		String addr="";
	}
	
	class mGetTransientRideJson {
		String status = "failed";
		String reason = "";
		String redirect = "";
		int trId=0;
		User owner= new User();
		Loc origLoc=new Loc();
		Loc destLoc=new Loc();
		int dist=0;
		int dura=0;
		String date="";
		String time="";
		String flexTime="";
		boolean userType=false;
		int totalSeats=0;
		int availSeats=0;
		double price=0;
		Loc [] middle;
		User [] parti;
	}

	
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mGetTransientRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		mGetTransientRideJson json = new mGetTransientRideJson();
		//boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
		boolean islogin = true;
		if (!islogin)
		{
			json.reason="not login";
			json.redirect="login.html";
		}
		else{
			int trId = Integer.parseInt( request.getParameter("trId"));
			TransientRide tride = TransientRideAccess.getTransisentRideById(trId);
		    TransientTopic ttopic = TransientTopicAccess.getTransientTopicById(trId);
		    if (tride==null) {
		    	json.reason="no such trId";
		    } else {
		    	json.owner.uid=tride.owner.get_uid();
		    	json.owner.name=tride.owner.get_name();
		    	json.owner.pic=tride.owner.get_avatarID();
		    	json.owner.cellPhone=tride.owner._cellphone;
		    	
		    	json.origLoc.lat=tride.origLoc.get_lat();
		    	json.origLoc.lon=tride.origLoc.get_lon();
		    	json.origLoc.addr=tride.origLoc._addr;
		    	
		    	json.destLoc.lat=tride.destLoc.get_lat();
		    	json.destLoc.lon=tride.destLoc.get_lon();
		    	json.destLoc.addr=tride.destLoc._addr;
		    	
		    	json.dist=tride.dist;
		    	json.dura=tride.dura;
		    	
		    	json.date=tride.rideDate.toString();
		    	json.time=tride.rideTime.toString();
		    	
		    	json.flexTime=tride.rideFlex.toString();
		    	json.userType=tride.userType;
		    	json.totalSeats=tride.totalSeats;
		    	json.availSeats=tride.availSeats;
		    	json.price=tride.price;
		    	
		    	json.status="successful";
		    }
		}
		Gson gson = new Gson();
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(gson.toJson(json));
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
