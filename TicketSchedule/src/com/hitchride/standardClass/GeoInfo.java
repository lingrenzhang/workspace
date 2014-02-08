package com.hitchride.standardClass;

//Maintain with google API reference.
public class GeoInfo {
	private int _status=0; //1:Only Location initialized,2:Only Lat,Lon initialized,3:Fully initialized
	private double _lat, _lon;
	public String _state, _city, _nbhd, _addr;
	private String _formatedAddr;
	public GeoInfo(String formatedAddr)
	{
		this._formatedAddr=formatedAddr;
		_status = 1;
	}
	public GeoInfo(double lat,double lon)
	{
		this._lat = lat;
		this._lon = lon;
		_status = 2;
	}
	public GeoInfo(String formatedAddr,double lat,double lon)
	{
		this._lat = lat;
		this._lon = lon;
		this._formatedAddr=formatedAddr;
		_status = 3;
	}
	
	public double get_lat() {
		switch (_status)
		{
			case 2:
			case 3:
				return _lat;
			case 1:
				//TO DO: Add self check
				return -1;
			default:
				//TO DO: Add exception
				return -1;
		}
	}

	public double get_lon() {
		switch (_status)
		{
			case 2:
			case 3:
				return _lon;
			case 1:
				//TO DO: Add self check
				return -1;
			default:
				//TO DO: Add exception
				return -1;
		}
	}
	
	public String get_formatedAddr() {
		switch (_status)
		{
			case 1:
			case 3:
				return _formatedAddr;
			case 2:
				//TO DO: Add self check
				return "";
			default:
				//TO DO: Add exception
				return null;
		}
	}
}
