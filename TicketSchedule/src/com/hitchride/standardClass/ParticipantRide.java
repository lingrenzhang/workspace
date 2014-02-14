package com.hitchride.standardClass;

//This class is to represent participantRide information
public class ParticipantRide extends RideInfo{

	public ParticipantRide(RideInfo part) {
		super(part);
	}
	private int associateMajorRideId; //Topic the ParticipantRide associating with.
	private double locationMappingScore;
	private double timeMappingScore;
}
