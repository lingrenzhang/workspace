package com.hitchride.access;


//Note: The schemaTb is mainly used to persistent store DDL. Actual schemaTb has process the 
//      "fieldType" to smaller control unit.
public class TicketScheduleSchemaTb {
	public enum Table
	{
		UserTb,
		UserGroup,
		RideInfo,
		PartiRide,
		TopicRide,
		Message,
		Topic
	}
	
	public static final DataColumnSchema[] UserTb={
		new DataColumnSchema("userId","INT(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY"),
		new DataColumnSchema("emailAddress","VARCHAR(50)"),
		new DataColumnSchema("password","VARCHAR(30)"),
		new DataColumnSchema("givenname","VARCHAR(40)"),
		new DataColumnSchema("surname","VARCHAR(40)"),
		new DataColumnSchema("address","VARCHAR(200)"),
		new DataColumnSchema("userLevel","INT"),
		new DataColumnSchema("avatarID","VARCHAR(20)"),
		new DataColumnSchema("groupId","INT(10) UNSIGNED"),
		//Define constrains as Data Column Schema since currently there is no function difference
		new DataColumnSchema("FOREIGN KEY (groupId)","References UserGroup(GroupId)")
	};
	
	
	public static final DataColumnSchema[] RideInfo={
        //Key Info
		new DataColumnSchema("userId","INT(10) UNSIGNED"), //Foreign Key constrain? 
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
		new DataColumnSchema("PayperSeat","DECIMAL(6,2)"),
		
		//Constrain
		new DataColumnSchema("FOREIGN KEY (userId)","References UserTb(userId)")
	};
	
	public static final DataColumnSchema[] PartiRide={
		new DataColumnSchema("RideInfoID","INT(10) UNSIGNED"), //Foreign Key constrain? 
        new DataColumnSchema("TopicId","INT(10)"),
        new DataColumnSchema("Status","INT(2)"),
		new DataColumnSchema("GeoMatch","INT(3) DEFAULT 0"),
		new DataColumnSchema("ScheduleMatch","INT(3) DEFAULT 0"),
		new DataColumnSchema("BarginMatch","INT(3) DEFAULT 0"),
		
		//Foreign keyconstrain
		new DataColumnSchema("Foreign key (RideInfoID)","REFERENCE RideInfo(recordid)"),
		
	};
	
	public static final DataColumnSchema[] TopicRide={
        //Key Info
		new DataColumnSchema("userId","INT(10) UNSIGNED"), //Foreign Key constrain? 
		                                          //Fixed value not visible to client.
		new DataColumnSchema("RideInfoId","INT(10) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
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
		
		//Foreign key constrain
		new DataColumnSchema("Foreign Key (userId)","Reference UserTb(userId)")
	};

	public static final DataColumnSchema[] Message={
		new DataColumnSchema("MessageId","INT(10) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
		new DataColumnSchema("fromUser","INT(10)"),
		new DataColumnSchema("toUser","INT(10)"),
        new DataColumnSchema("topicID","INT(10)"),
        new DataColumnSchema("messageContent","VARCHAR(500)"),
        new DataColumnSchema("timestamp","timestamp"),
        new DataColumnSchema("isSystemMessage","BOOL")
	};
	
	public static final DataColumnSchema[] Topic={
		new DataColumnSchema("TopicId","INT(10) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"), //Same as TopicRide ID
		new DataColumnSchema("OwnerId","INT(10) UNSIGNED"),
        new DataColumnSchema("ParRideIds","VARCHAR(100)"), //10 pride at most, ID split by ","
		new DataColumnSchema("ReqRideIds","VARCHAR(100)"), //10 pride at most, ID split by ","
        new DataColumnSchema("messageIds","VARCHAR(400)"), //about 50 message for a single ride now. Ignore early message.
	
		new DataColumnSchema("Foreign key (OwnerId)","References UserTb(userId)")
	};
	
	public static final DataColumnSchema[] UserGroup={
		new DataColumnSchema("GroupID","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY"),
		new DataColumnSchema("GroupName","VARCHAR(50)"),
		new DataColumnSchema("GroupAuthLevel","INT"),
		new DataColumnSchema("AuthnicationCode","VARCHAR(20)")
	};
	
	public static DataColumnSchema[] getSchemaByName(String name)
	{
		Table tb = Table.valueOf(name);
		switch (tb) 
		{
			case UserTb:
				return TicketScheduleSchemaTb.UserTb;
			case UserGroup:
				return TicketScheduleSchemaTb.UserGroup;
			case RideInfo:
				return TicketScheduleSchemaTb.RideInfo;
			case PartiRide:
				return TicketScheduleSchemaTb.PartiRide;
			case TopicRide:
				return TicketScheduleSchemaTb.TopicRide;
			case Topic:
				return TicketScheduleSchemaTb.Topic;
			case Message:
				return TicketScheduleSchemaTb.Message;
		}
		System.out.println("Input table name not valid");
		return null;
	}
}

