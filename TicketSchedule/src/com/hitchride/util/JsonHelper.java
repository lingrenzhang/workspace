package com.hitchride.util;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

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
		gson.registerTypeAdapter(java.sql.Date.class, new sqlDateSerializer());
		gson.disableHtmlEscaping();
		gjson = gson.create();
	}
	
	private class sqlTimeSerializer implements JsonSerializer<java.sql.Time> {
		@Override
		public JsonElement serialize(Time src, Type typeOfSrc,
				JsonSerializationContext context) {
			DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
			return new JsonPrimitive(df.format(src));
		}
	}
	
	private class sqlDateSerializer implements JsonSerializer<java.sql.Date> {
		@Override
		public JsonElement serialize(Date src, Type typeOfSrc,
				JsonSerializationContext context) {
			//format on UI 
				StringBuilder result = new StringBuilder();
				result.append((src.getMonth()+1)+"/");
				result.append((src.getDate())+"/");
				result.append((src.getYear()+1900));
			return new JsonPrimitive(result.toString());
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
