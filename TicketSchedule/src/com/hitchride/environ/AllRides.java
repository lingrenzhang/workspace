package com.hitchride.environ;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteRide;
import com.hitchride.database.access.CarpoolTbAccess;
import com.hitchride.database.access.CommuteRideAccess;


public class AllRides {
	private static AllRides allRides;
	private AllRides(){
			_availRides = new Hashtable<Integer,CommuteRide>();
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
	public Hashtable<Integer,CommuteRide> _availRides;
	private int _maxRidesKey = 0;

    private void initialFromNew(){
    	_availRides = CommuteRideAccess.LoadAllRide();
    	_maxRidesKey = CommuteRideAccess.getMaxRideId();
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
				CommuteRide ride = new CommuteRide(rides,true);
				_availRides.put(ride.id,ride);
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
	
	public CommuteRide getRide(int key)
	{
		return this._availRides.get(key);
	}
	
	public void insert_availride(CommuteRide ride)
	{
		if (ride.id==0 || ride.id<= this._maxRidesKey )
		{
			this._maxRidesKey++;
			ride.id = _maxRidesKey;
			this._availRides.put(this._maxRidesKey, ride);
			System.out.println("Ride ID not initialized before inserting to hash table.");
		}
		else
		{
			this._availRides.put(this._maxRidesKey, ride);
			this._maxRidesKey = ride.id;
		}
	}
	
	public void udpate_availride(CommuteRide ride)
	{
		if (this._availRides.get(ride.id)==null)
		{
			System.out.println("Warning: This is a new ride. Insert now and please check DB integrity.");
		}
		this._availRides.put(ride.id, ride);
	}
	
	public void remove(CommuteRide ride)
	{
		this._availRides.remove(ride.id);
	}
	public void remove(int pid)
	{
		this._availRides.remove(pid);
	}

}
