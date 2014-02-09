package com.hitchride.standardClass;


//Use observer design pattern for the relationship between User and RideDiscussion
//This is the abstract subject
public interface RideStatusChange {
	public void attach(RideListener rideListener);
	public void detach(RideListener rideListener);
	void notifyUserRideChange();
}
