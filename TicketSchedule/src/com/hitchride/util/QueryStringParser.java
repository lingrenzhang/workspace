package com.hitchride.util;
import java.util.Hashtable;

public class QueryStringParser {
	private Hashtable<String,String> content;
	
	//private boolean isStrongKeyCheck=false;
	//private boolean isStrongValueCheck=false;
	//Only support string now.
	public QueryStringParser(String queryString) throws NullPointerException
	{
		content = new Hashtable<String,String>(10);
		String[] units = queryString.split("&");
		if (units!=null)
		{
			for(int i=0;i<units.length;i++)
			{
				String[] keyvalue = units[i].split("=");
				String key = keyvalue[0];
				String value = keyvalue.length==1? "": keyvalue[1];
				content.put(key, value);
			}
		}
		else
		{
			throw new NullPointerException("Input parameter can not be null");
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
