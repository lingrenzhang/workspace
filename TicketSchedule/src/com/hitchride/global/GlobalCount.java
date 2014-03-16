package com.hitchride.global;
import com.hitchride.access.MessageTbAccess;


public class GlobalCount {
	private static boolean IsSystemInitialized=false;
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
