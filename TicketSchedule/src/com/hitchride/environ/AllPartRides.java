package com.hitchride.environ;

import java.util.Enumeration;
import java.util.Hashtable;

import com.hitchride.CommuteParticipantRide;
import com.hitchride.database.access.PartiRideAccess;

public class AllPartRides {
	private static AllPartRides allPartRides;
	public Hashtable<Integer,CommuteParticipantRide> _partRides;  //All available rides. Represent by RID.
	public static AllPartRides getPartRides(){
		if (null == allPartRides){
			{
				allPartRides = new AllPartRides();
			}
		}
		return allPartRides;
	}
	
	private AllPartRides(){
        _partRides = new Hashtable<Integer,CommuteParticipantRide>();
        initializeParticipantRide();
	}

	private void initializeParticipantRide() {
		_partRides.clear();
		_partRides = PartiRideAccess.LoadAllpRide();
    	//Link rides to user
    	Enumeration<Integer> e = _partRides.keys();
    	while (e.hasMoreElements())
    	{
    		int rid = e.nextElement();
    		CommuteParticipantRide partRide = _partRides.get(rid);
    		partRide._rideInfo.get_user().pRides.add(partRide);
    		System.out.println("ParticpantRide: "+ rid +" relation registered");
    	}
	}

	public CommuteParticipantRide get_participantRide(int Ride) {
		CommuteParticipantRide partRide = _partRides.get(Ride);
		return partRide;
	}

	public Enumeration<Integer> getAllPartRide() {
		
		return _partRides.keys();
	}


	public void updatePartRideStat(int prid, int pstatus) {
		CommuteParticipantRide pride = _partRides.get(prid);
		pride.set_status(pstatus);
		
	}

	
	public void insert_pride(CommuteParticipantRide pride)
	{
		this._partRides.put(pride._pid, pride);
	}
	
	public void remove(CommuteParticipantRide pride)
	{
		this._partRides.remove(pride._pid);
	}
	public void remove(int pid)
	{
		this._partRides.remove(pid);
	}
}
