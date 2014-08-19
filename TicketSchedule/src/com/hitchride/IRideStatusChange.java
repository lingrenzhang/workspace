package com.hitchride;


//Use observer design pattern for the relationship between User and RideDiscussion
//This is the abstract subject
public interface IRideStatusChange {
	public void attach(IRideListener rideListener);
	public void detach(IRideListener rideListener);
	void notifyUserRideChange();
}
