package com.shs.liangdiaosi.Calc;

import java.util.HashMap;
import java.sql.*;
import java.util.*;

public class CalculatorTest {

	public static void main(String[] args) {
		ScoreCalculator calc = new ScoreCalculator();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("roundtrip", false);
		map.put("userType", false);
		// Stanford coordinates
		map.put("origLat", 37.42573);
		map.put("origLon", -122.166094);
		
		// Ranch 99 Cupertino
		map.put("destLat", 37.338022);
		map.put("destLon", -122.015118);
		
		// Time
		long time_ms = 1000*60*60*8; // 8 o'clock
		long flex_ms = 1000*60*15; // 15 minutes
		map.put("forwardTime", new Time(time_ms));
		map.put("forwardFlexibility", new Time(flex_ms));
		
		ArrayList<Integer> ids = calc.filterByCoordinates(map, 20);
		for(Integer id : ids){
			System.out.println(id);
		}
	}
}
