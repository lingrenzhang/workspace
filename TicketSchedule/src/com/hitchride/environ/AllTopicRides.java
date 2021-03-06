package com.hitchride.environ;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.hitchride.CommuteOwnerRide;
import com.hitchride.database.access.CommuteOwnerRideAccess;

public class AllTopicRides {
	private static AllTopicRides allTopicRides;
	//All OwnerRideInfo reference can be directly accessed through RID
	public Hashtable<Integer,CommuteOwnerRide> _topicRides;
	
	private AllTopicRides(){
			_topicRides = new Hashtable<Integer,CommuteOwnerRide>();
			initialFromNew();
	}
	
	public static AllTopicRides getTopicRides(){
		if (null == allTopicRides){
			{
				allTopicRides = new AllTopicRides();
			}
		}
		return allTopicRides;
	}

    private void initialFromNew(){
    	_topicRides = CommuteOwnerRideAccess.LoadAllOwnerRide();
    	//Link rides to user
    	Enumeration<Integer> e = _topicRides.keys();
    	while (e.hasMoreElements())
    	{
    		int rid = e.nextElement();
    		CommuteOwnerRide ownerRide = _topicRides.get(rid);
    		ownerRide._rideInfo = AllRides.getRides().getRide(rid);
    		ownerRide._rideInfo.get_user().tRides.add(ownerRide);
    		System.out.println("OwnerRide: "+ rid +" relation registered");
    	}
    }
    
	public void insert_TopicRide(CommuteOwnerRide tRide) {
		this._topicRides.put(tRide.id,tRide);
	}
	
	public CommuteOwnerRide getRide(int key)
	{
		return this._topicRides.get(key);
	}
}
