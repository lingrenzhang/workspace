package com.hitchride.servlet.manage;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.database.access.CarpoolTbAccess;
/**
 * Servlet implementation class InsertGeo
 */
public class InsertGeo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertGeo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("EnterInsGeoServlet");
		String q=request.getQueryString();
		System.out.println(q);
		String[] arg=q.split("&");
		//String check;
		int recordId=Integer.parseInt(arg[0]);
		float origlat=Float.parseFloat(arg[1]);
		float origlng=Float.parseFloat(arg[2]);
		float destlat=Float.parseFloat(arg[3]);
		float destlng=Float.parseFloat(arg[4]);
		try {
			System.out.println("Inserting record: " + recordId + " "+origlat +":"+origlng+";"+
					destlat+":"+destlng+";");
			CarpoolTbAccess.insertLocation(recordId, origlat, origlng, destlat, destlng,true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("Done");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
