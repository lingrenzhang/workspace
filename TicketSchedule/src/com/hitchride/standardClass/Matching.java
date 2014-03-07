package com.hitchride.standardClass;

public interface Matching {
	public void ComputeMatching(OwnerRideInfo ownerRide,ParticipantRide partRide);
	public void QuickGeoCompute(RideInfo ownerRide,RideInfo partRide);
	public int getLocationMatching();
	public int getSchedulingMatching();
	public int getBarginMatching();
}
	