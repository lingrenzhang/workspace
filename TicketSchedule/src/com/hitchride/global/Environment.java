package com.hitchride.global;

//Singleton design mode here
public class Environment {
	private static Environment env;
	private Environment(){
		//Confirm the proper initialize routine.
            AllUsers.getUsers();
			AllRides.getRides();
			DummyData.getDummyEnv();
			AllTopicRides.getTopicRides();
			AllTopics.getTopics();
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