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
	private int _topicKey=0;
	
	private void initialTopics() {
		_topics = TopicTbAccess.LoadAllTopic();
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
