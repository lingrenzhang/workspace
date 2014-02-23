package com.hitchride.standardClass;

//This class is to represent participantRide information
//Having close relation to UI. Think about decouple it.
public class ParticipantRide extends RideInfo{
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

	public String get_status_message(){
		switch (_status)
		{
			case 0: break;
			case 1: return "Waiting owner accept";
			case 2: return "Communicating details";
			case 3: return "Waiting participant confirm";
			case 4: return "<img src=\"/TicketSchedule/Picture/Dealdone.png\" alt=\"Dealdone\"></img>";
		}
		return "Not valid status";
	}
	
	public String get_status_user_control(){
		switch (_status)
		{
			case 0: return 
					"<div class=\"user_operation\" id=\"user_operation\">"
					+"<button type=button onclick=\"join()\">Join</button>"
					+"</div>";
			case 1: return "Waiting owner accept";
			case 2: return "In discussion"
			        + "<div class=\"user_operation\" id=\"user_operation\" uid="+this.get_userId()+ " fromStatus=2>"
					+ "<img src=\"/TicketSchedule/Picture/Accept.png\" alt=\"Accept\" onclick=\"accept(event)\"></img>"
					+ "<img src=\"/TicketSchedule/Picture/Decline.png\" alt=\"Decline\" onclick=\"decline(event)\"></img>"
					+"</div>";
			case 3: return "Request accepted, confirm please"
					+ "<div class=\"user_operation\" id=\"user_operation\" uid="+this.get_userId()+" fromStatus=3>"
					+ "<img src=\"/TicketSchedule/Picture/Accept.png\" alt=\"Accept\" onclick=\"accept(event)\"></img>"
					+ "<img src=\"/TicketSchedule/Picture/MoreInfo.png\" alt=\"MoreInfo\" onclick=\"moreinfo(event)\"></img>"
					+ "<img src=\"/TicketSchedule/Picture/Decline.png\" alt=\"Decline\" onclick=\"decline(event)\"></img>"
					+"</div>";
			case 4: return "<img src=\"/TicketSchedule/Picture/Dealdone.png\" alt=\"Dealdone\"></img>";
		}
		return "Not valid status";
	}
	
	public String get_status_owner_control(){
		switch (_status)
		{
			case 0: break;
			case 1: return "Waiting your accept"+
					"<div class=\"owner_operation\" id=\"owner_operation\" uid="+this.get_userId()+" fromStatus=1>"
			        +"<img src=\"/TicketSchedule/Picture/Accept.png\" alt=\"Accept\" onclick=\"accept(event)\"></img>"
					+"<img src=\"/TicketSchedule/Picture/MoreInfo.png\" alt=\"MoreInfo\" onclick=\"moreinfo(event)\"></img>"
					+"<img src=\"/TicketSchedule/Picture/Decline.png\" alt=\"Decline\" onclick=\"decline(event)\"></img>"
					+"</div>";
			case 2: return "In discussion"+
					"<div class=\"owner_operation\" id=\"owner_operation\" uid="+this.get_userId()+" fromStatus=2>"
					+"<img src=\"/TicketSchedule/Picture/Accept.png\" alt=\"Accept\" onclick=\"accept(event)\"></img>"
					+"<img src=\"/TicketSchedule/Picture/Decline.png\" alt=\"Decline\" onclick=\"decline(event)\"></img>"
					+"</div>";
			case 3: return "Waiting confirm";
			case 4: return "<img src=\"/TicketSchedule/Picture/Dealdone.png\" alt=\"Dealdone\"></img>";
		}
		return "Not valid status";
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
	
	public int get_userId()
	{
		return this._rideinfo.get_user().get_uid();
	}

}
