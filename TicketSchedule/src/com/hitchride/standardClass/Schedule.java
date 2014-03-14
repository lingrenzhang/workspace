package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.Time;

//Save schedule information of a ride
public class Schedule implements Cloneable{
	private boolean _isRoundTrip; 
	private boolean _isCommute;
	
	public Time forwardTime,forwardFlexibility;
	public Time backTime, backFlexibility;	// for round trip only
	
	// for travel only
	public Date tripDate;
	public Time tripTime;
	
	// for commute
	private boolean[] _dayOfWeek = new boolean[]{false,false,false,false,false,false,false};
    public Time[] cftime = new Time[7];
    public Time[] cbtime = new Time[7];
    
	public boolean isRoundTrip() {
		return _isRoundTrip;
	}
	public void set_isRoundTrip(boolean _isRoundTrip) {
		this._isRoundTrip = _isRoundTrip;
	}
	public boolean isCommute() {
		return _isCommute;
	}
	public void set_isCommute(boolean _isCommute) {
		this._isCommute = _isCommute;
	}
	
	
	public void set_dayOfWeek(int dayofWeek)
	{
		while (dayofWeek>0)
		{
			int last = dayofWeek % 10;
			if ((last>0) &&(last<8))
			{
				_dayOfWeek[last-1]=true;
			}
			dayofWeek = (int) (dayofWeek-last)/10;
		}
	}
	
	public boolean[] get_dayofWeek()
	{
		return this._dayOfWeek;
	}
	
	public int get_days(){
		int result=0;
		for (int i=0;i<7;i++)
		{
			if (this._dayOfWeek[i])
			{
				result = 10*result+i+1;
			}
		}
		return result;
	}
	
	@Override
	public Schedule clone() 
	{
		Schedule schCopy = null;
		try {
			schCopy = (Schedule)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //All values are value type
		return schCopy;
	}
	
	public String getSchedule(boolean isHtml)
	{
		StringBuilder result = new StringBuilder(100);
		if (_isCommute)
		{
			result.append("Commute on:");
			for (int i=0;i<6;i++)
			{
				if (_dayOfWeek[i])
				{
					switch (i)
					{
						case 0:
							result.append(" Sun");
						break;
						case 1:
							result.append(" Mon");
							break;
						case 2:
							result.append(" Thu");
							break;
						case 3:
							result.append(" Wed");
							break;
						case 4:
							result.append(" Tue");
							break;
						case 5:
							result.append(" Fri");
							break;
						case 6:
							result.append(" Sat");
							break;
					}
				}
			}
		}
		else{
			result.append("Trip Date: ");
			result.append(tripDate);
		}
		if (isHtml){
		result.append("<br>");
		}
		else{
			result.append("\r\n");
		}

		
		result.append("Forward Time: ");
		result.append(forwardTime.toString());
		
		if (isRoundTrip())
		{
			if (isHtml){
				result.append("<br>");
				}
				else{
					result.append("\r\n");
				}
			result.append("Return Time: ");
			result.append(backTime.toString());
		}
		
		return result.toString();
	}
}
