package com.hitchride.global;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.hitchride.access.TopicTbAccess;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.Topic;

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
	
	public Hashtable<Integer,Topic> _topics;
	
	private void initialTopics() {
		_topics = TopicTbAccess.LoadAllTopic();
	}
	
	public Topic get_topic(int key) {
		return _topics.get(key);
	}
	
	public void insert_topic(Topic topic) {
		_topics.put(topic.get_topicId(),topic);
	}
	
	public void delete_topic(int key)
	{
		_topics.remove(key);
	}
	
	
	//Not good for runtime. Think of proper second index and small unit of API later.
	public List<Topic> getTopicRideAsList() 
	{
		List<Topic> result=new ArrayList<Topic>();
		Enumeration<Topic> topicElement = this._topics.elements();
		while (topicElement.hasMoreElements())
		{
			result.add(topicElement.nextElement());
		}
		return result;
	}
}
