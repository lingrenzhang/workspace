package com.hitchride.calc;

import java.sql.*;
import java.util.*;

import com.hitchride.calc.ScoreCalculator;
import com.hitchride.calc.rideInfoParameters;

public class CalculatorTest {

	public static void main(String[] args) {
		ScoreCalculator calc = new ScoreCalculator();
		rideInfoParameters map = new rideInfoParameters();
		map.commute = true;
		map.roundtrip = false;
		map.userType = false;
		// Stanford coordinates
		map.origLat = 37.42573;
		map.origLon = -122.166094;
		
		// Ranch 99 Cupertino
		map.destLat = 37.338022;
		map.destLon = -122.015118;
		
		// Time
		long time_ms = 1000*60*60*8; // 8 o'clock
		long flex_ms = 1000*60*15; // 15 minutes
		map.forwardTime = new Time(time_ms);
		map.forwardFlexibility = new Time(flex_ms);
		
		List<rideInfoParameters> list = calc.filterByCoordinates(map, 20);
		for(rideInfoParameters item : list){
			System.out.println(item.recordId+"\tFrom: " + item.origCity + ", " + item.origState + "\tTo:" + item.destCity + ", " + item.destState);
		}
	}
}
