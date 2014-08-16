package com.hitchride;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.access.TransientRideAccess;
import com.hitchride.access.TransientTopicAccess;
import com.hitchride.global.AllUsers;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.TransientRide;
import com.hitchride.standardClass.User;

/**
 * Servlet implementation class UpdateTMiddlePoint
 */
public class UpdateTMiddlePoint extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTMiddlePoint() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(this)
		{
			String method = request.getParameter("method");
			if (method.equalsIgnoreCase("delete"))
			{
				int trid = Integer.parseInt(request.getParameter("trId"));
				int deleteId = Integer.parseInt(request.getParameter("deleteId"));
				TransientTopicAccess.removeMiddleP(trid, deleteId);
				
				response.setStatus(200);
				response.getWriter().write("{status: OK}");
			}
			if (method.equalsIgnoreCase("insert"))
			{
				int trid = Integer.parseInt(request.getParameter("trId"));
				String mAddr = request.getParameter("mAddr");
				Double mLat = Double.parseDouble(request.getParameter("mLat"));
				Double mLng = Double.parseDouble(request.getParameter("mLng"));
				GeoInfo middleP = new GeoInfo(mAddr,mLat,mLng);
				TransientTopicAccess.addMiddleP(trid, middleP);
				
				response.setStatus(200);
				response.getWriter().write("{status: OK}");
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
