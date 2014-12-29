package com.hitchride.util;

import com.hitchride.GeoInfo;
import com.hitchride.TransientTopic;
import com.hitchride.User;

public class GsonWrapperForTransientTopic {
	
	public int id;
	private int ownerId = 0; //This one is not saved in db schema and can not be changed by client. 
	public int nmiddlePoints=0;
	public GeoInfo[] middle;
	public int nParticipant=0;
	public int[] partiuid;
	public GsonWrapperForUser[] parti;

	public GsonWrapperForTransientTopic(TransientTopic ttopic) {
		this.id =ttopic.id;
		this.ownerId = ttopic.ownerId;
		this.nmiddlePoints=ttopic.nmiddlePoints;
		this.middle = ttopic.middle;
		this.nParticipant = ttopic.nParticipant;
		this.partiuid = ttopic.partiuid;
		parti = new GsonWrapperForUser[ttopic.parti.length];
		for(int i=0;i<ttopic.nParticipant;i++){
			if(ttopic.parti[i]!=null){
				parti[i]=new GsonWrapperForUser(ttopic.parti[i]);
			}
		}
	}
}
