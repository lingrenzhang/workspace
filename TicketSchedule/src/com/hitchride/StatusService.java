package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchiride.util.QueryStringParser;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.UserInfo;

/**
 * Servlet implementation class StatusService
 */
public class StatusService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatusService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QueryStringParser qs = new QueryStringParser(request.getQueryString());
		int fromSta =qs.getInt("fromStatus");
		int toSta =qs.getInt("toStatus");
		int fromUser = qs.getInt("fromUser");
		int toUser = qs.getInt("toUser");
		int oRId = qs.getInt("ownRideId");

		UserInfo fromU = Environment.getEnv().getUser(fromUser);
		UserInfo toU = Environment.getEnv().getUser(toUser);
		OwnerRideInfo ori = Environment.getEnv().getOwnerRide(oRId);
		Message msg = new Message(fromSta, toSta, fromU, toU, ori);
		{
			Environment.getEnv().insert_message(msg);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
