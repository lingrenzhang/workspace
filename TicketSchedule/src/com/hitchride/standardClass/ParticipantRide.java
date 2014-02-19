package com.hitchride.standardClass;

//This class is to represent participantRide information
public class ParticipantRide {
    public RideInfo _rideinfo;
    public int _pid;
	private int _assoOwnerRideId; //Topic the ParticipantRide associating with.
    private MatchScore _Match;
    private int _status; //0 for not associating with OwnerRide -> 1 (drive by participant)
    					 //1 for link to owner, waiting response ->0,2,3 (drive by owner)
                         //2 for owner add additional requirement ->0,1 (drive by participant)
    					 //3 for owner commit -> 0,1,4 (drive by participant, log)
                         //4 for participant Confirm -> (Deal done.)
	public ParticipantRide(RideInfo rideinfo) {
		this._rideinfo = rideinfo;
		this._pid = rideinfo.recordId;
		this.set_Match(new MatchScore());
		set_status(0);
	}
	public int get_status() {
		return _status;
	}
	public void set_status(int _status) {
		this._status = _status;
	}
	public MatchScore get_Match() {
		return _Match;
	}
	public void set_Match(MatchScore _Match) {
		this._Match = _Match;
	}
	
	public int get_assoOwnerRideId() {
		return _assoOwnerRideId;
	}
	public void set_assoOwnerRideId(int _assoOwnerRideId) {
		this._assoOwnerRideId = _assoOwnerRideId;
	}
	
	

}
