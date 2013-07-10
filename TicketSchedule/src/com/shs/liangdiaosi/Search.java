package com.shs.liangdiaosi;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.shs.liangdiaosi.Calc.*;
/**
 * Servlet implementation class Search
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getQueryString();
		rideInfoParameters myArgs = new rideInfoParameters();
		
		myArgs.roundtrip = false;
		myArgs.userType = false;
		// Stanford coordinates
		myArgs.origLat = 37.42573;
		myArgs.origLon = -122.166094;
		
		// Ranch 99 Cupertino
		myArgs.destLat = 37.338022;
		myArgs.destLon = -122.015118;
		
		// Time
		long time_ms = 1000*60*60*8; // 8 o'clock
		long flex_ms = 1000*60*15; // 15 minutes
		myArgs.forwardTime = new Time(time_ms);
		myArgs.forwardFlexibility = new Time(flex_ms);
		
		List<rideInfoParameters> resultList = new ArrayList();
		ScoreCalculator sc = new ScoreCalculator();
		sc.filterByCoordinates(myArgs, 20);
		
		RequestDispatcher rd = request.getRequestDispatcher("/search.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
