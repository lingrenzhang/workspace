package com.hitchride.util;
import java.lang.reflect.Type;
import java.sql.Time;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hitchride.global.AllRides;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.RideInfo;
public class JsonHelper {
	public GsonBuilder gson; 
	public Gson gjson;
	public JsonHelper(){
		gson = new GsonBuilder();
		gson.registerTypeAdapter(java.sql.Time.class, new sqlTimeSerializer());
		gson.disableHtmlEscaping();
		gjson = gson.create();
	}
	
	private class sqlTimeSerializer implements JsonSerializer<java.sql.Time> {
		@Override
		public JsonElement serialize(Time src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}
		
	}
	
	public String toJson(Object object)
	{
		return gjson.toJson(object);
	}
	
	public static void main(String[] args)
	{
		Time time = new Time(12*3600000);
		Gson g = new Gson();
		RideInfo i = AllRides.getRides().getRide(10);
		GeoInfo geo = i.destLoc;
		
		System.out.println(time.toString());
		System.out.println(g.toJson(time));
		System.out.println(g.toJson(geo));
		JsonHelper json = new JsonHelper();
		System.out.println(json.toJson(time));
		System.out.println(json.toJson(geo));
		
	}
}
