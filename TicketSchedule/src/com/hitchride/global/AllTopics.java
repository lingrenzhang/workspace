package com.hitchride.global;

import java.util.Hashtable;

import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;

public class AllTopics {
	private static AllTopics allTopics;
	private AllTopics(){
			_topics= new Hashtable<Integer,Topic>(1000);
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
	private int _topicKey=0;
	
	private void initialTopics() {
		Topic topic = new Topic(838);
		_topics.put(838,topic);
		System.out.println("Topic "+topic + "initialized." );
		
	}
	
	
	public Topic get_topic(int key) {
		return _topics.get(key);
	}
	
	public void insert_topic(Topic topic) {
		topic.set_topicId(this._topicKey);
		_topics.put(this._topicKey,topic);
		this._topicKey++;
	}
	
	public void delete_topic(int key)
	{
		_topics.remove(key);
	}
	
	
}
