package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.Time;

import com.hitchride.util.TimeFormatHelper;

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
    
    public Schedule(){
    	
    }
    
    public Schedule(boolean isDummy)
    {
    	if (isDummy)
    	{
    		this._isRoundTrip = true;
    		this._isCommute = true;
    		this.set_dayOfWeek(12345);
    		this.tripDate = new Date(114,4,1);
    		this.tripTime = new Time(0-TimeFormatHelper.systemOffset);
    		this.forwardFlexibility = new Time(20*60000-TimeFormatHelper.systemOffset);
    		this.backFlexibility = new Time(20*60000-TimeFormatHelper.systemOffset);
    		Time forwardTime = new Time(8*3600000-TimeFormatHelper.systemOffset);
    		Time backTime = new Time(18*3600000-TimeFormatHelper.systemOffset);
    		for (int i=0;i<7;i++)
    		{
    			cftime[i]=forwardTime;
    			cbtime[i]=backTime;
    		}
    	}
    }
    
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
				if (last!=7)
				{
					_dayOfWeek[last]=true;
				}
				else
				{
					_dayOfWeek[0]=true;
				}
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
		for (int i=1;i<8;i++)
		{
			int k = (i==7)?0:i;
			if (this._dayOfWeek[k])
			{
				result = 10*result+i;
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
		String br = isHtml?"<br>":"\r\n";
		StringBuilder result = new StringBuilder(100);
		if (_isCommute)
		{
			for (int i=0;i<7;i++)
			{
				if (_dayOfWeek[i])
				{
					switch (i)
					{
						case 0:
							result.append(" Sun");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[0]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[0]));
							result.append(br);
						break;
						case 1:
							result.append(" Mon");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[1]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[1]));
							result.append(br);
							break;
						case 2:
							result.append(" Thu");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[2]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[2]));
							result.append(br);
							break;
						case 3:
							result.append(" Wed");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[3]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[3]));
							result.append(br);
							break;
						case 4:
							result.append(" Tue");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[4]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[4]));
							result.append(br);
							break;
						case 5:
							result.append(" Fri");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[5]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[5]));
							result.append(br);
							break;
						case 6:
							result.append(" Sat");
							result.append(" "+TimeFormatHelper.getQuarterTime(cftime[6]));
							result.append(" "+TimeFormatHelper.getQuarterTime(cbtime[6]));
							result.append(br);
							break;
					}
				}
			}
		}
		else{
			result.append("Trip Date: ");
			result.append(this.tripDate);
			result.append(br);
			
			result.append("Trip Time: ");
			result.append(TimeFormatHelper.getQuarterTime(this.tripTime));
			result.append(br);
			
			result.append("Flex: ");
			result.append(this.forwardFlexibility);
			result.append(br);
		}

		/*
		if (isRoundTrip())
		{
			if (isHtml){
				result.append("<br>");
				}
				else{
					result.append("\r\n");
				}
			result.append("Return Time: ");
			if (backTime!=null)
			{
				result.append(backTime.toString());
			}
			else
			{
				result.append("Round Trip not supported for travel now. Arrange another ride for your return please.");
			}
		}
		*/
		return result.toString();
	}
	

}
