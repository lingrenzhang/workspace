package com.hitchride.environ;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.hitchride.CommuteOwnerRide;
import com.hitchride.CommuteTopic;
import com.hitchride.database.access.CommuteTopicAccess;

public class AllTopics {
	private static AllTopics allTopics;
	private AllTopics(){
			initialTopics();
	}
	
	public static AllTopics getTopics(){
		if (null == allTopics){
			{
				allTopics = new AllTopics();
			}
		}
		return allTopics;
	}
	
	public Hashtable<Integer,CommuteTopic> _topics;
	
	private void initialTopics() {
		_topics = CommuteTopicAccess.LoadAllTopic();
	}
	
	public CommuteTopic get_topic(int key) {
		return _topics.get(key);
	}
	
	public void insert_topic(CommuteTopic topic) {
		_topics.put(topic.get_topicId(),topic);
	}
	
	public void delete_topic(int key)
	{
		_topics.remove(key);
	}
	
	
	//Not good for runtime. Think of proper second index and small unit of API later.
	public List<CommuteTopic> getTopicRideAsList() 
	{
		List<CommuteTopic> result=new ArrayList<CommuteTopic>();
		Enumeration<CommuteTopic> topicElement = this._topics.elements();
		while (topicElement.hasMoreElements())
		{
			result.add(topicElement.nextElement());
		}
		return result;
	}
}
