package com.hitchride.util;
import java.util.Hashtable;

public class QueryStringParser {
	private Hashtable<String,String> content;
	
	//private boolean isStrongKeyCheck=false;
	//private boolean isStrongValueCheck=false;
	
	//Only support string now.
	public QueryStringParser(String queryString)
	{
		content = new Hashtable<String,String>(10);
		String[] units = queryString.split("&");
		for(int i=0;i<units.length;i++)
		{
			String key = units[i].split("=")[0];
			String value = units[i].split("=")[1];
			content.put(key, value);
		}
	}
	
	public String getString(String key)
	{
		String value = (String) content.get(key);
		return value;
	}
	
	public int getInt(String key)
	{
		int value = Integer.parseInt(content.get(key));
		return value;
	}
}