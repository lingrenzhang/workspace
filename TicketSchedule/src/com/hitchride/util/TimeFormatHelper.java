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
	
	public static Time getTripTime(String timeexp,Time forwardFlex)
	{
		/*
		<option value="anytime" selected="selected">anytime</option>
        <option value="early">early (12a-8a)</option>
        <option value="morning">morning (8a-12p)</option>
        <option value="afternoon">afternoon (12p-5p)</option>
        <option value="evening">evening (5p-9p)</option>
        <option value="night">night (9p-12a)</option>
        */
		if (timeexp.equals("anytime"))
		{
			Time tripTime = new Time(12*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(12*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("early"))
		{
			Time tripTime = new Time(4*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(4*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("morning"))
		{
			Time tripTime = new Time(10*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(2*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("afternoon"))
		{
			Time tripTime = new Time(29*1800000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(5*1800000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("evening"))
		{
			Time tripTime = new Time(19*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(2*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("night"))
		{
			Time tripTime = new Time(21*1800000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(3*1800000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		return getTime(timeexp);
	}

	public static Time getTime(String timeexp)
	{
		if (timeexp==null || ("NO TRIP").equalsIgnoreCase(timeexp))
		{
			return new Time(36*3600000-TimeFormatHelper.systemOffset);
		}
		String[] time = timeexp.split(":");
		int hour;
		int minute;
		if (time[1].contains("PM"))
		{
			hour = Integer.parseInt(time[0])+12;
			minute = Integer.parseInt(time[1].substring(0, 2));
		}
		else
		{
			hour = Integer.parseInt(time[0]);
			minute = Integer.parseInt(time[1].substring(0, 2));
		}
		Time formattime = new Time(hour*3600000+minute*60000-TimeFormatHelper.systemOffset);
		return formattime;
	}
	
	
	public static void main(String[] args)
	{
		Time time = new Time(36*900000);
		System.out.println(getQuarterTime(time));
	}
}
