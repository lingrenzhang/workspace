package com.hitchride.global;

import java.util.Hashtable;

import com.hitchride.access.TopicTbAccess;
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
	
	
}
