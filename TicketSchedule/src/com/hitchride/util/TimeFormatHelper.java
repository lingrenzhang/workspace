package com.hitchride.util;

import java.sql.Date;

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
}
