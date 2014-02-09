package com.hitchride.global;

import java.util.Hashtable;

import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.User;


//Singleton design mode here
public class Environment {
	private static Environment env;
	private Environment(){
		_actUsers = new Hashtable<Integer,User>(10000);
		_availRides = new Hashtable<Integer,OwnerRideInfo>();
		initialAvaRideLoad();
	}
	
	private void initialAvaRideLoad() {
		// TODO Auto-generated method stub
		// Load available Ride from DB.
		
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
}