package com.hitchride.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.hitchride.access.CarpoolTbAccess;
import com.hitchride.access.RideInfoAccess;
import com.hitchride.standardClass.RideInfo;


public class AllRides {
	private static AllRides allRides;
	private AllRides(){
			_availRides = new Hashtable<Integer,RideInfo>();
			//initialFromOld();
			initialFromNew();
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
	private int _maxRidesKey = 0;

    private void initialFromNew(){
    	_availRides = RideInfoAccess.LoadAllRide();
    	_maxRidesKey = RideInfoAccess.getMaxRideId();
    }
	
	private void initialFromOld() {
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
				//RideInfoAccess.insertRideInfo(ride);

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
	
	public RideInfo getRide(int key)
	{
		return this._availRides.get(key);
	}
	
	public void insert_availride(RideInfo ride)
	{
		if (ride.recordId==0 || ride.recordId<= this._maxRidesKey )
		{
			this._maxRidesKey++;
			ride.recordId = _maxRidesKey;
			this._availRides.put(this._maxRidesKey, ride);
			System.out.println("Ride ID not initialized before inserting to hash table.");
		}
		else
		{
			this._availRides.put(this._maxRidesKey, ride);
			this._maxRidesKey = ride.recordId;
		}
	}
	
	public void udpate_availride(RideInfo ride)
	{
		if (this._availRides.get(ride.recordId)==null)
		{
			System.out.println("Warning: This is a new ride. Insert now and please check DB integrity.");
		}
		this._availRides.put(ride.recordId, ride);
	}
}
