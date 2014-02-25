package com.hitchride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllRides;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
import com.hitchride.standardClass.UserInfo;
import com.hitchride.util.QueryStringParser;

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
		int toId = qs.getInt("topicId");
		RideInfo ride = (RideInfo) request.getSession().getAttribute("actRide");
		Topic topic = (Topic) request.getSession().getAttribute("topic");

		UserInfo fromU = Environment.getEnv().getUser(fromUser);
		UserInfo toU = Environment.getEnv().getUser(toUser);
		
		int status = 10*fromSta + toSta;
		ParticipantRide pride=null;
		switch (status)
		{
			case 1:
				pride = new ParticipantRide(ride);
				pride.set_user((User) fromU);
				pride.set_assoOwnerRideId(topic.get_topicId());
				pride.set_status(1);
				topic._requestPride.add(pride);
		    	break;
			case 10:
				for (Iterator<ParticipantRide> prideI=topic._requestPride.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == toU.get_uid()){
				    	pride = pridet;
				    }
					
        		}
				pride.set_assoOwnerRideId(-1);
				pride.set_status(0);
				topic._requestPride.remove(pride);
				break;
			case 12:
				for (Iterator<ParticipantRide> prideI=topic._requestPride.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == toU.get_uid()){
				    	pride = pridet;
				    }
					
        		}
				pride.set_status(2);
				topic._requestPride.remove(pride);
				topic.parRides.add(pride);
				break;
			case 13:
				for (Iterator<ParticipantRide> prideI=topic._requestPride.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == toU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_status(3);
				topic._requestPride.remove(pride);
				topic.parRides.add(pride);
				break;
			case 20:
				for (Iterator<ParticipantRide> prideI=topic.parRides.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == fromU.get_uid() ||pridet.get_userId() == toU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_assoOwnerRideId(-1);
				pride.set_status(0);
				topic.parRides.remove(pride);
				break;
			case 21:
				for (Iterator<ParticipantRide> prideI=topic.parRides.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == fromU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_status(1);
				topic.parRides.remove(pride);
				topic._requestPride.add(pride);
				break;
			case 23:
				for (Iterator<ParticipantRide> prideI=topic.parRides.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == toU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_status(3);
				break;
			case 30:
				for (Iterator<ParticipantRide> prideI=topic.parRides.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == fromU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_assoOwnerRideId(-1);
				pride.set_status(0);
				topic.parRides.remove(pride);
				break;
			case 32:
				for (Iterator<ParticipantRide> prideI=topic.parRides.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == fromU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_status(2);
				break;
			case 34:
				for (Iterator<ParticipantRide> prideI=topic.parRides.iterator(); prideI.hasNext();)
				{
				    ParticipantRide pridet = prideI.next();
				    if (pridet.get_userId() == fromU.get_uid()){
				    	pride = pridet;
				    }
        		}
				pride.set_status(4);
				break;
			default:
				System.out.println("Not leagal status change.");
				break;
		}
		
		
		
		Message msg = new Message(fromSta, toSta, fromU, toU, topic);
		msg.sendMessage();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
