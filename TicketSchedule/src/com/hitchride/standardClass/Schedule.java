package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.Time;

//Save schedule information of a ride
public class Schedule {
	private boolean _isRoundTrip; 
	private boolean _isCommute;
	
	public Time forwardTime, forwardFlexibility;
	public Time backTime, backFlexibility;	// for round trip only
	public Boolean[] dayOfWeek;
	
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
}
