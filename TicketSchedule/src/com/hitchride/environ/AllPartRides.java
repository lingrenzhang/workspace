package com.hitchride.environ;

import java.util.Enumeration;
import java.util.Hashtable;

import com.hitchride.CommutePartiRide;
import com.hitchride.database.access.CommutePartiRideAccess;

public class AllPartRides {
	private static AllPartRides allPartRides;
	public Hashtable<Integer,CommutePartiRide> _partRides;  //All available rides. Represent by RID.
	public static AllPartRides getPartRides(){
		if (null == allPartRides){
			{
				allPartRides = new AllPartRides();
			}
		}
		return allPartRides;
	}
	
	private AllPartRides(){
        _partRides = new Hashtable<Integer,CommutePartiRide>();
        initializeParticipantRide();
	}

	private void initializeParticipantRide() {
		_partRides.clear();
		_partRides = CommutePartiRideAccess.LoadAllpRide();
    	//Link rides to user
    	Enumeration<Integer> e = _partRides.keys();
    	while (e.hasMoreElements())
    	{
    		int rid = e.nextElement();
    		CommutePartiRide partRide = _partRides.get(rid);
    		partRide._rideInfo.get_user().pRides.add(partRide);
    		System.out.println("ParticpantRide: "+ rid +" relation registered");
    	}
	}

	public CommutePartiRide get_participantRide(int Ride) {
		CommutePartiRide partRide = _partRides.get(Ride);
		return partRide;
	}

	public Enumeration<Integer> getAllPartRide() {
		
		return _partRides.keys();
	}


	public void updatePartRideStat(int prid, int pstatus) {
		CommutePartiRide pride = _partRides.get(prid);
		pride.set_status(pstatus);
		
	}

	
	public void insert_pride(CommutePartiRide pride)
	{
		this._partRides.put(pride.id, pride);
	}
	
	public void remove(CommutePartiRide pride)
	{
		this._partRides.remove(pride.id);
	}
	public void remove(int pid)
	{
		this._partRides.remove(pid);
	}
}
