package com.hitchride.global;

import java.util.Iterator;
import java.util.List;

import com.hitchride.access.TransientRideAccess;
import com.hitchride.access.TransientTopicAccess;
import com.hitchride.authorization.AuthorizationHelper;
import com.hitchride.standardClass.TransientTopic;
import com.hitchride.standardClass.User;

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
			loadTransientTopic(); //Must execute after all user have been loaded.
			
			
	}
	
	public static Environment getEnv(){
		if (null == env){
			{
				env = new Environment();
			}
		}
		return env;
	}
	
	public static void loadTransientTopic()
	{
		List<TransientTopic> ttopics = TransientTopicAccess.initPartis();
		for (Iterator<TransientTopic> ttopicI=ttopics.iterator();ttopicI.hasNext();)
		{
			TransientTopic ttopic = ttopicI.next();
			int ownerId = ttopic.get_ownerId();
			if (ownerId!=0)
			{	
				User user = (User) AllUsers.getUsers().getUser(ownerId);
				user.tTride.add(ttopic.transientRideId);
				for (int i = 0;i<ttopic.nParticipant;i++)
				{
					ttopic.parti[i].pTride.add(ttopic.transientRideId);
				}
			}
		}
		
		ttopics=null; //Not sure whether necessary, so the resource could be recollected by GC.
	}
	

}