package com.hitchride.util;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class TimeFormatHelper {
	static public Date setDate(String date)
	{
		String dates[] = date.split("/");
		int month = Integer.parseInt(dates[0])-1;
		int day = Integer.parseInt(dates[1]);
		int year = Integer.parseInt(dates[2])-1900;

		Date date1 = new Date(year,month,day);
		return date1;
		
	}
	
	public static final Time zerotime = new Time(0);
	public static final int systemOffset = TimeZone.getDefault().getOffset(0);
	
	public static String getFormatedTime(Time time)
	{
		DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);
		String s =df.format(time);
		return s;
	}
	public static String getQuarterTime(Time time)
	{
		String suffix="";
		int totalquar = (int) (time.getTime()/900000);
		if (totalquar<0)
			totalquar = totalquar+96;
		if (totalquar>48)
		{
			totalquar=(totalquar-48);
			suffix="pm";
		}
		else
		{
			suffix="am";
		}
		
		String min="";
		int quarter = totalquar%4;
		switch (quarter)
		{
		  case 0:
			  min="00";
			  break;
		  case 1:
			  min="15";
			  break;
		  case 2:
			  min="30";
			  break;
		  case 3:
			  min="45";
			  break;
		}
		Integer hour = (int) Math.floor(totalquar/4);
		return hour.toString() +":"+min+suffix;
		
	}
	
	public static void main(String[] args)
	{
		Time time = new Time(36*900000);
		System.out.println(getQuarterTime(time));
	}
}
