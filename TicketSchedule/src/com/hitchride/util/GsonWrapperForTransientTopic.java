package com.hitchride.util;

import com.hitchride.Schedule;
import com.hitchride.CommuteTopic;

public class GsonWrapperForTransientTopic {
	public int _topicId;
    public double origLoc_lat;
    public double origLoc_lon;
    public String origLoc_addr;
    public double destLoc_lat;
    public double destLoc_lon;
    public String destLoc_addr;
    public boolean rideInfo_userType;
    public int rideInfo_totalSeats;
    public double rideInfo_price;
    public String owner_givenname;
    public String owner_avatarID;
    public Schedule schedule;  //The class only has data structure. Use it for simplicity.
 
    public GsonWrapperForTransientTopic(CommuteTopic t){
    	this._topicId = t.get_topicId();
    	this.origLoc_lat = t.ownerRide._rideInfo.origLoc.get_lat();
    	this.origLoc_lon = t.ownerRide._rideInfo.origLoc.get_lon();
    	this.origLoc_addr = t.ownerRide._rideInfo.origLoc._addr;
    	this.destLoc_lat =t.ownerRide._rideInfo.destLoc.get_lat();
    	this.destLoc_lon =t.ownerRide._rideInfo.destLoc.get_lon();
    	this.destLoc_addr =t.ownerRide._rideInfo.destLoc._addr;
    	this.rideInfo_userType = t.ownerRide._rideInfo.userType;
    	this.rideInfo_totalSeats =  t.ownerRide._rideInfo.totalSeats;
    	this.rideInfo_price = t.ownerRide._rideInfo.price;
    	this.owner_givenname = t.owner.get_name();
    	this.owner_avatarID = t.owner.get_avatarID();
    	this.schedule = t.ownerRide._rideInfo.schedule;
    }
}
