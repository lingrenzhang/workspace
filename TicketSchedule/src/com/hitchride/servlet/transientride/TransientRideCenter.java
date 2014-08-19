package com.hitchride.servlet.transientride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hitchride.util.GsonWrapperForTransientRide;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;

/**
 * Servlet implementation class TransientRideCenter
 */
public class TransientRideCenter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransientRideCenter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
		if (!islogin)
		{
			request.getSession().setAttribute("fromLocation", "/TicketSchedule/Zh/TransientTopic.jsp");
			request.getSession().setAttribute("queryString", request.getQueryString());
			request.getSession().setMaxInactiveInterval(60*120);
			response.sendRedirect("/TicketSchedule/Login.jsp");
		}
		else{
			QueryStringParser qsPar = new QueryStringParser(request.getQueryString());
			int trId = qsPar.getInt("trId");
			TransientRide tride = TransientRideAccess.getTransisentRideById(trId);
		    TransientTopic ttopic = TransientTopicAccess.getTransientTopicById(trId);
		    
		    if (tride==null) //Tride is deleted while doing the query.
		    {
		    	//ttopic can not be null due to the foreign key constrain so not take special care.
		    	response.getWriter().write("{\"result\": \"fail\"}");
		    }
		    else
		    {
			    //Runtime bug that sometimes tride is initilialized but ttopic is not.
			    if (tride!=null && ttopic ==null ) 
			    {
			    	ttopic = new TransientTopic(trId);
			    	ttopic.insertToDB();
			    }
			    
			    JsonHelper jsonhelp = new JsonHelper();
			    GsonWrapperForTransientRide gtride = new GsonWrapperForTransientRide(tride);
			    String trideJson = jsonhelp.toJson(gtride);
				String ttopicJson = jsonhelp.toJson(ttopic);
				String result = "{\"tride\":"+trideJson+",\"ttopic\":"+ttopicJson+",\"result\": \"ok\"}";
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().write(result);
		    }
		}
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommuteRide ride = (CommuteRide) request.getSession().getAttribute("actRide");
		if (ride==null)
		{
			response.sendRedirect("/TicketSchedule/ManageRide.jsp");
		}
		else
		{
			CommuteOwnerRide ownRide = new CommuteOwnerRide(ride);
			ride.get_user().tRides.add(ownRide);
			CommutePartiRide pRide = AllPartRides.getPartRides().get_participantRide(ride.recordId);
			ride.get_user().pRides.remove(pRide);
			pRide.delete();
			
			AllPartRides.getPartRides().remove(ride.recordId);
			
			AllTopicRides.getTopicRides().insert_TopicRide(ownRide);
			ownRide.insertToDB();
			CommuteTopic topic= new CommuteTopic();
			topic.ownerRide=ownRide;
			topic.set_topicId(ride.recordId);
			topic.owner=ride.get_user();
			AllTopics.getTopics().insert_topic(topic);
			topic.insertToDB();
			response.sendRedirect("/TicketSchedule/servlet/TransientRideCenter?transientRideId="+ride.recordId);
		}
	}
}
