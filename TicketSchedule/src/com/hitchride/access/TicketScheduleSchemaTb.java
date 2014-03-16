package com.hitchride.access;


//Note: The schemaTb is mainly used to persistent store DDL. Actual schemaTb has process the 
//      "fieldType" to smaller control unit.
public class TicketScheduleSchemaTb {
	
	public static final DataColumnSchema[] User={
		new DataColumnSchema("userId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
		new DataColumnSchema("emailAddress","VARCHAR(50)"),
		new DataColumnSchema("password","VARCHAR(30)"),
		new DataColumnSchema("givenname","VARCHAR(40)"),
		new DataColumnSchema("surname","VARCHAR(40)"),
		new DataColumnSchema("address","VARCHAR(200)"),
		new DataColumnSchema("userLevel","INT"),
		new DataColumnSchema("avatarID","VARCHAR(20)"),
	};
	
	
	public static final DataColumnSchema[] RideInfo={
        //Key Info
		new DataColumnSchema("userId","INT(10)"), //Foreign Key constrain? 
		                                          //Fixed value not visible to client.
		new DataColumnSchema("recordId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
		//Geo Info
		new DataColumnSchema("origFAddr","VARCHAR(100)"),
		new DataColumnSchema("origAddr","VARCHAR(50)"),
		new DataColumnSchema("origState","VARCHAR(20)"),
		new DataColumnSchema("origCity","VARCHAR(30)"),
		new DataColumnSchema("origLat","DECIMAL(10,6)"),
		new DataColumnSchema("origLon","DECIMAL(10,6)"),

		
		new DataColumnSchema("destFAddr","VARCHAR(100)"),
		new DataColumnSchema("destAddr","VARCHAR(50)"),
		new DataColumnSchema("destState","VARCHAR(20)"),
		new DataColumnSchema("destCity","VARCHAR(30)"),
		new DataColumnSchema("destLat","DECIMAL(10,6)"),
		new DataColumnSchema("destLon","DECIMAL(10,6)"),
		
		new DataColumnSchema("distance","INT(8)"),
		new DataColumnSchema("duration","INT(8)"),
		
		//Schedule Info
		new DataColumnSchema("commute","BOOL"),
		new DataColumnSchema("roundtrip","BOOL"),
		new DataColumnSchema("forwardFlex","TIME"),
		new DataColumnSchema("backFlex","TIME"),
		new DataColumnSchema("tripDate","DATE"),
		new DataColumnSchema("tripTime","TIME"),
		
		new DataColumnSchema("dayOfWeek","INT(7)"),
        new DataColumnSchema("f1","TIME"),
        new DataColumnSchema("f2","TIME"),
        new DataColumnSchema("f3","TIME"),
        new DataColumnSchema("f4","TIME"),
        new DataColumnSchema("f5","TIME"),
        new DataColumnSchema("f6","TIME"),
        new DataColumnSchema("f7","TIME"),
        new DataColumnSchema("b1","TIME"),
        new DataColumnSchema("b2","TIME"),
        new DataColumnSchema("b3","TIME"),
        new DataColumnSchema("b4","TIME"),
        new DataColumnSchema("b5","TIME"),
        new DataColumnSchema("b6","TIME"),
        new DataColumnSchema("b7","TIME"),
        
        //Bargin Info
		new DataColumnSchema("UserType","BOOL"),
		new DataColumnSchema("TotalSeats","INT(2)"),
		new DataColumnSchema("AvailSeats","INT(2)"),
		new DataColumnSchema("PayperSeat","DECIMAL(6,2)")
	};
	
	public static final DataColumnSchema[] PartiRide={
		new DataColumnSchema("RideInfoID","INT(10)"), //Foreign Key constrain? 
        new DataColumnSchema("TopicId","INT(10)"),
        new DataColumnSchema("Status","INT(2)"),
		new DataColumnSchema("GeoMatch","INT(3) DEFAULT 0"),
		new DataColumnSchema("ScheduleMatch","INT(3) DEFAULT 0"),
		new DataColumnSchema("BarginMatch","INT(3) DEFAULT 0"),
	};
	
	public static final DataColumnSchema[] TopicRide={
        //Key Info
		new DataColumnSchema("userId","INT(10)"), //Foreign Key constrain? 
		                                          //Fixed value not visible to client.
		new DataColumnSchema("RideInfoId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
		//Ready to extend to middle point info
		new DataColumnSchema("MiddlePointCount","INT(2) DEFAULT 0"),
		new DataColumnSchema("M1Addr","VARCHAR(50)"),
		new DataColumnSchema("M1Lat","DECIMAL(10,6)"),
		new DataColumnSchema("M1Lon","DECIMAL(10,6)"),
		new DataColumnSchema("M2Addr","VARCHAR(50)"),
		new DataColumnSchema("M2Lat","DECIMAL(10,6)"),
		new DataColumnSchema("M2Lon","DECIMAL(10,6)"),
		new DataColumnSchema("M3Addr","VARCHAR(50)"),
		new DataColumnSchema("M3Lat","DECIMAL(10,6)"),
		new DataColumnSchema("M3Lon","DECIMAL(10,6)"),
		new DataColumnSchema("M4Addr","VARCHAR(50)"),
		new DataColumnSchema("M4Lat","DECIMAL(10,6)"),
		new DataColumnSchema("M4Lon","DECIMAL(10,6)"),
		new DataColumnSchema("M5Addr","VARCHAR(50)"),
		new DataColumnSchema("M5Lat","DECIMAL(10,6)"),
		new DataColumnSchema("M5Lon","DECIMAL(10,6)"),
	};

	public static final DataColumnSchema[] Message={
		new DataColumnSchema("MessageId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
		new DataColumnSchema("fromUser","INT(10)"),
		new DataColumnSchema("toUser","INT(10)"),
        new DataColumnSchema("topicID","INT(10)"),
        new DataColumnSchema("messageContent","VARCHAR(500)"),
        new DataColumnSchema("timestamp","timestamp"),
        new DataColumnSchema("isSystemMessage","BOOL")
	};
	
	public static final DataColumnSchema[] Topic={
		new DataColumnSchema("TopicId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"), //Same as TopicRide ID
		new DataColumnSchema("OwnerId","INT(10)"),
        new DataColumnSchema("ParRideIds","VARCHAR(100)"), //10 pride at most, ID split by ","
		new DataColumnSchema("ReqRideIds","VARCHAR(100)"), //10 pride at most, ID split by ","
        new DataColumnSchema("messageIds","VARCHAR(400)") //about 50 message for a single ride now. Ignore early message.
	};
}

