package com.hitchride.util;

import java.sql.Time;

import com.hitchride.CommuteRide;
import com.hitchride.Schedule;
import com.hitchride.TransientRide;

public class GsonWrapperForCommuteRide {
	 public int id;
	 public int userId;
	 public double origLoc_lat;
	 public double origLoc_lon;
	 public String origLoc_addr;
	 public double destLoc_lat;
	 public double destLoc_lon;
	 public String destLoc_addr;
	 public int dist;
	 public int dura;
	 public int totalSeats;
	 public double price;
	 public String owner_givenname;
	 public String owner_avatarID;
	 public boolean userType;
	 public String owner_cellphone;
	 public Schedule schedule;
	  
	 public GsonWrapperForCommuteRide(CommuteRide cr)
	 {
		this.userId = cr.userId;
	    this.id =cr.recordId;
	    this.origLoc_lat = cr.origLoc.get_lat();
	    this.origLoc_lon = cr.origLoc.get_lon();
	    this.origLoc_addr = cr.origLoc.get_formatedAddr();
	    this.destLoc_lat = cr.destLoc.get_lat();
	    this.destLoc_lon = cr.destLoc.get_lon();
	    this.destLoc_addr = cr.destLoc.get_formatedAddr();
	    this.dist = cr.dist;
	    this.dura = cr.dura;
	    this.totalSeats = cr.totalSeats;
	    this.price = cr.price;
	    this.owner_givenname = cr.get_user().get_name();
	    this.owner_avatarID = cr.get_user().get_avatarID();
	    this.userType = cr.userType;
	    this.owner_cellphone = cr.get_user()._cellphone;
	    this.schedule = cr.schedule;
	 }
}
