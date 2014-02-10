package com.hitchride.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
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
		_nactuser=0;
	}
	
	private void initialAllUser() {
		UserTbAccess utb = new UserTbAccess();
		System.out.println("_users initializing: Loaded from DB.");
		int i=0;
		try {
			ResultSet users = utb.showall();
			while(users.next())
			{
				User user = new User();
				user.set_uid(users.getInt("userID"));
				user.set_name(users.getString("givenname"));
				user.set_avatarID(users.getString("avatarID"));
				_users.put(user.get_uid(), user);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("_users DB load failed");
		}
		System.out.println("#"+i+" users loaded.");
	}

	private void initialAvaRideLoad() {
		// TODO Auto-generated method stub
		// Load available Ride from DB.
		System.out.println("_availRides initializing: Loaded from DB.");
		int i=0;
		try {
			ResultSet rides = CarpoolTbAccess.rideInitialLoad();
			while(rides.next())
			{
				OwnerRideInfo ride = new OwnerRideInfo(rides,true);
				_availRides.put(ride.recordId,ride);
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
	
	public int _nactuser;
	
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
	     _nactuser++;
	}
	
	//Return the first user object when String name matches
	//Temper solution, think about DB structure later.
	public User getUser(String name)
	{
		Enumeration<User> enu = _users.elements();
		while (enu.hasMoreElements())
		{
			User result = enu.nextElement();
			if (result.get_name().equalsIgnoreCase(name))
			return result;
		}
		return null;
	}
}