package com.hitchride.util;

import java.sql.Time;

import com.hitchride.TransientRide;

public class GsonWrapperForTransientRide {
    public int transientRideId;
    public int userId;
    public double origLoc_lat;
    public double origLoc_lon;
    public String origLoc_addr;
    public double destLoc_lat;
    public double destLoc_lon;
    public String destLoc_addr;
    public int totalSeats;
    public double price;
    public String owner_givenname;
    public String owner_avatarID;
    public Time rideTime;
    public boolean userType;
    public String owner_cellphone;
    
    public GsonWrapperForTransientRide(TransientRide tr)
    {
    	this.userId = tr.userId;
    	this.transientRideId =tr.transientRideId;
    	this.origLoc_lat = tr.origLoc.get_lat();
    	this.origLoc_lon = tr.origLoc.get_lon();
    	this.origLoc_addr = tr.origLoc.get_formatedAddr();
    	this.destLoc_lat = tr.destLoc.get_lat();
    	this.destLoc_lon = tr.destLoc.get_lon();
    	this.destLoc_addr = tr.destLoc.get_formatedAddr();
    	this.totalSeats = tr.totalSeats;
    	this.price = tr.price;
    	this.owner_givenname = tr.getOwner().get_name();
    	this.owner_avatarID = tr.getOwner().get_avatarID();
    	this.rideTime = tr.rideTime;
    	this.userType = tr.userType;
    	this.owner_cellphone = tr.getOwner()._cellphone;
    }
    
}
