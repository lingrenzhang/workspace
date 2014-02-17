package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.Time;

//Save schedule information of a ride
public class Schedule implements Cloneable{
	private boolean _isRoundTrip; 
	private boolean _isCommute;
	
	public Time forwardTime, forwardFlexibility;
	public Time backTime, backFlexibility;	// for round trip only
	private boolean[] _dayOfWeek;
	
	// for travel only
	public Date tripDate;

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
		_dayOfWeek=new boolean[7];
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
}
