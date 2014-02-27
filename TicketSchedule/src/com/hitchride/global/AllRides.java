package com.hitchride.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.hitchride.access.CarpoolTbAccess;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;

public class AllRides {
	private static AllRides allRides;
	private AllRides(){
			_availRides = new Hashtable<Integer,RideInfo>();
			_topicRides = new Hashtable<Integer,OwnerRideInfo>();
			initialAvaRideLoad();
	}
	
	public static AllRides getRides(){
		if (null == allRides){
			{
				allRides = new AllRides();
			}
		}
		return allRides;
		
	}

	//All OwnerRideInfo reference can be directly accessed through RID
	//public Hashtable<Integer,OwnerRideInfo> _availRides;  //All available rides. Represent by RID.
	public Hashtable<Integer,RideInfo> _availRides;
	public Hashtable<Integer,OwnerRideInfo> _topicRides;

	
	private int _availRidesKey = 0;


	
	private void initialAvaRideLoad() {
		// TODO Auto-generated method stub
		// Load available Ride from DB.
		System.out.println("_availRides initializing: Loaded from DB.");
		int i=0;
		try {
			ResultSet rides = CarpoolTbAccess.rideInitialLoad();
			while(rides.next())
			{
				RideInfo ride = new RideInfo(rides,true);
				_availRides.put(ride.recordId,ride);

				OwnerRideInfo ownerRide = new OwnerRideInfo(ride);
				_topicRides.put(ride.recordId,ownerRide);
				ownerRide.get_user().rides.add(ownerRide);
                _availRidesKey=ride.recordId+1;
				i++;
				//System.out.println("Ride "+ ride.recordId + " initialized");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("#"+i+" rides loaded.");
	}
	
	
	public OwnerRideInfo getOwnerRide(int RID)
	{
		OwnerRideInfo ownerRide = _topicRides.get(RID);
		return ownerRide;
	}
	
	public void inser_availride(RideInfo part)
	{
		this._availRidesKey++;
		part.recordId = _availRidesKey;
		this._availRides.put(this._availRidesKey, part);

	}
}
