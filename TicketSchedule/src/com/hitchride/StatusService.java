package com.hitchride;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllRides;
import com.hitchride.global.AllUsers;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
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
		synchronized(this)
		{
			QueryStringParser qs = new QueryStringParser(request.getQueryString());
			int fromSta =qs.getInt("fromStatus");
			int toSta =qs.getInt("toStatus");
			int fromUser = qs.getInt("fromUser");
			int toUser = qs.getInt("toUser");
			int toId = qs.getInt("topicId");
			RideInfo ride = (RideInfo) request.getSession().getAttribute("actRide");
			Topic topic = (Topic) request.getSession().getAttribute("topic");
	
			User fromU = (User) AllUsers.getUsers().getUser(fromUser);
			User toU = (User) AllUsers.getUsers().getUser(toUser);
			
			int status = 10*fromSta + toSta;
			ParticipantRide pride=null; //Sync DB status to persistent storage instantly. 
			                            //Check details when encounter resource limitation.
			switch (status)
			{
				case 1:
					if (ride.get_user()==null)  //When ride coming from search
					{
						AllRides.getRides().insert_availride(ride);
						//ride.set_user(fromU); (issue for GSON.Loop inside)
						ride.userId=fromU.get_uid();
						ride.insertToDB();
						pride = new ParticipantRide(ride);
						AllPartRides.getPartRides().insert_pride(pride);
						//pride._rideInfo.set_user((User) fromU); (issue for GSON. Loop inside)
						fromU.pRides.add(pride);
						pride.insertToDB();
					}
					else
					{
						pride = AllPartRides.getPartRides().get_participantRide(ride.recordId);
					}
					
					pride.set_assoOwnerRideId(topic.get_topicId());
					pride.set_status(1);
					topic._requestPride.add(pride);
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
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
					pride.updateDB();
					break;
				default:
					System.out.println("Not leagal status change.");
					break;
			}
				
			Message msg = new Message(fromSta, toSta, fromU, toU, topic);
			msg.sendMessage();
			topic.updateDB();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
