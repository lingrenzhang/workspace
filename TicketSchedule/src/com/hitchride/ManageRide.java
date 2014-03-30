package com.hitchride;

import java.io.IOException;

import com.hitchride.global.AllRides;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ManageRide
 */
public class ManageRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QueryStringParser qs = new QueryStringParser(request.getQueryString());
		int rid = qs.getInt("rid");
		RideInfo ride = AllRides.getRides().getRide(rid);
		JsonHelper jh = new JsonHelper();
		String rideJson = jh.toJson(ride);
		System.out.println(rideJson);
		response.getWriter().write(rideJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
