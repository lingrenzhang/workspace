package com.hitchride.global;

import com.hitchride.access.TransientRideAccess;
import com.hitchride.authorization.AuthorizationHelper;

//Singleton design mode here
public class Environment {
	private static Environment env;
	public AuthorizationHelper auth;
	public int maxTranRideId;
	private Environment(){
		//Confirm the proper initialize routine.
			//auth = new AuthorizationHelper();
            AllUsers.getUsers();
			AllRides.getRides();
			AllPartRides.getPartRides();
			DummyData.getDummyEnv();
			AllTopicRides.getTopicRides();
			AllTopics.getTopics();
			maxTranRideId = TransientRideAccess.getMaxTransientRideId();
	}
	
	public static Environment getEnv(){
		if (null == env){
			{
				env = new Environment();
			}
		}
		return env;
		
	}
}