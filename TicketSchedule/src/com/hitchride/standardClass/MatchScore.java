package com.hitchride.standardClass;

public class MatchScore implements Matching {

	private int _LocationMatching;
	private int _ScheduleingMatching;
	private int _BargingMatching;
	
	private OwnerRideInfo _ownerRide;
	private ParticipantRide _partRide;
	
	public MatchScore()
	{
		this._LocationMatching = 50;
		this._ScheduleingMatching = 50;
		this._BargingMatching = 50;
	}
	
	public MatchScore(OwnerRideInfo ownerRide, ParticipantRide partRide)
	{
		this._ownerRide = ownerRide;
		this._partRide = partRide;
		this.ComputeMatching(ownerRide, partRide);
		this._LocationMatching = 50;
		this._ScheduleingMatching = 50;
		this._BargingMatching = 50;
	}
	
	@Override
	public void ComputeMatching(OwnerRideInfo ownerRide,
			ParticipantRide partRide) {
		
		// TODO Auto-generated method stub

	}

	@Override
	public int getLocationMatching() {
		return this._LocationMatching;
	}

	@Override
	public int getSchedulingMatching() {
		return this._ScheduleingMatching;
	}

	@Override
	public int getBarginMatching() {
		return this._BargingMatching;
	}

}
