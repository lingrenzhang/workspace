package com.hitchride.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.hitchride.access.CarpoolTbAccess;
import com.hitchride.access.UserTbAccess;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.User;



//Singleton design mode here
public class Environment {
	private static Environment env;
	private Environment(){
		_users = new Hashtable<Integer,User>(100000);
		initialAllUser();
		_availRides = new Hashtable<Integer,OwnerRideInfo>();
		initialAvaRideLoad();
		_actUsers = new Hashtable<Integer,User>(10000);
	}
	
	private void initialAllUser() {
		UserTbAccess utb = new UserTbAccess();
		System.out.println("_users initializing: Loaded from DB.");
		try {
			ResultSet users = utb.showall();
			while(users.next())
			{
				User user = new User();
				user.set_uid(users.getInt("userID"));
				user.set_name(users.getString("givenname"));
				_users.put(user.get_uid(), user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("_users DB load failed");
		}
	}

	private void initialAvaRideLoad() {
		// TODO Auto-generated method stub
		// Load available Ride from DB.
		System.out.println("_availRides initializing: Loaded from DB.");
		try {
			ResultSet rides = CarpoolTbAccess.rideInitialLoad();
			while(rides.next())
			{
				OwnerRideInfo ride = new OwnerRideInfo(rides,true);
				_availRides.put(ride.recordId,ride);
				System.out.println("Ride "+ ride.recordId + " initialized");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Environment getEnv(){
		if (null == env){
			env = new Environment();
		}
		return env;
		
	}
	
	//All user object reference can be directly accessed through UID
	public Hashtable<Integer,User> _users; //All Users. Represent by UID.
	//All Act user object reference can be directly accessed through UID
	public Hashtable<Integer,User> _actUsers; //Active users. Represent by UID.
	//All OwnerRideInfo reference can be directly accessed through RID
	public Hashtable<Integer,OwnerRideInfo> _availRides;  //All available rides. Represent by RID.
	
	
	public User getUser(int UID)
	{
		User user = _users.get(UID);
		return user;
	}
	public User getActUser(int UID)
	{
		User user = _actUsers.get(UID);
		return user;
	}
	
	public OwnerRideInfo get(int RID)
	{
		OwnerRideInfo ownerRide = _availRides.get(RID);
		return ownerRide;
	}

	public void addActiveUser(int uID) {
	     User user = _users.get(uID);
	     _actUsers.put(uID, user);
	}
	
}