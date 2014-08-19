package com.hitchride.environ;
import com.hitchride.database.access.MessageAccess;


public class GlobalCount {
	private static GlobalCount gCount;
	private GlobalCount(){
          messageCount = MessageAccess.getMaxMessageId();
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
