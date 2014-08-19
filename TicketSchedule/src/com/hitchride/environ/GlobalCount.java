package com.hitchride.environ;
import com.hitchride.database.access.MessageTbAccess;


public class GlobalCount {
	private static GlobalCount gCount;
	private GlobalCount(){
          messageCount = MessageTbAccess.getMaxMessageId();
	}
	
	public static GlobalCount getGCount(){
		if (null == gCount){
			{
				gCount = new GlobalCount();
			}
		}
		return gCount;
	}
	
	public int messageCount;
}
