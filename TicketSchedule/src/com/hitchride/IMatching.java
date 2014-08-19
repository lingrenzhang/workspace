package com.hitchride;

public interface IMatching {
	public void ComputeMatching(CommuteOwnerRide ownerRide,CommutePartiRide partRide);
	public void QuickGeoCompute(CommuteRide ownerRide,CommuteRide partRide);
	public int getLocationMatching();
	public int getSchedulingMatching();
	public int getBarginMatching();
}
	