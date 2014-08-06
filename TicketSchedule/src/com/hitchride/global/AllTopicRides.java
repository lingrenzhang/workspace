package com.hitchride.global;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.hitchride.access.TopicRideAccess;
import com.hitchride.standardClass.OwnerRideInfo;

public class AllTopicRides {
	private static AllTopicRides allTopicRides;
	//All OwnerRideInfo reference can be directly accessed through RID
	public Hashtable<Integer,OwnerRideInfo> _topicRides;
	
	private AllTopicRides(){
			_topicRides = new Hashtable<Integer,OwnerRideInfo>();
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
    	_topicRides = TopicRideAccess.LoadAllOwnerRide();
    	//Link rides to user
    	Enumeration<Integer> e = _topicRides.keys();
    	while (e.hasMoreElements())
    	{
    		int rid = e.nextElement();
    		OwnerRideInfo ownerRide = _topicRides.get(rid);
    		ownerRide._rideInfo = AllRides.getRides().getRide(rid);
    		ownerRide._rideInfo.get_user().tRides.add(ownerRide);
    		System.out.println("OwnerRide: "+ rid +" relation registered");
    	}
    }
    
	public void insert_TopicRide(OwnerRideInfo tRide) {
		this._topicRides.put(tRide._recordId,tRide);
	}
	
	public OwnerRideInfo getRide(int key)
	{
		return this._topicRides.get(key);
	}
}
