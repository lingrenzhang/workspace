package com.hitchride.access;

import java.util.Hashtable;

//This class defines the table format inside schedule DB. Use DBGeneration to generate tables.
@Deprecated
public class TicketScheduleDBFormat {

	
	@Deprecated
	public static final String[] CarpoolTbcolumns={
		"userName",
		// trip info
		"recordId",
		"roundtrip", 
		"userType", 
		"dayOfWeek", 
		//"tripDate",
		"origState",
		"origCity",
		"origNbhd",
		"origAddr",
		"destState",
		"destCity",
		"destNbhd",
		"destAddr",
		"detourFactor",
		"forwardTime",
		"forwardFlexibility",
		"backTime",
		"backFlexibility",
		"origLat",
		"origLon",
		"destLat",
		"destLon",
		"tripTime",
		"dist",
		"dura",
		"participants"
	};
	
	@Deprecated
	public static final String[] CarpoolTbcolumnTypes = {
		"VARCHAR(30)", //"userName"
		// trip info
		"INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE", //"recordId"
		"BOOL", //"roundtrip"
		"BOOL", //"userType"
		"INT(7)", //"dayOfWeek"  (1234567)
		//"DATE",
		"VARCHAR(20)",//"origState"
		"VARCHAR(20)",//"origCity"
		"VARCHAR(20)",//"origNbhd"
		"VARCHAR(50)",//"origAddr"
		"VARCHAR(20)",//"destState"
		"VARCHAR(20)",//"destCity"
		"VARCHAR(20)",//"destNbhd"
		"VARCHAR(50)",//"destAddr"
		"DECIMAL(3,2)",//"detourFactor"
		"TIME",//"forwardTime"
		"TIME",//"forwardFlexibility"
		"TIME",//"backTime"
		"TIME", //"backFlexibility"
		"DECIMAL(10,6)",//"origLat"
		"DECIMAL(10,6)",//"origLon"
		"DECIMAL(10,6)",//"destLat"
		"DECIMAL(10,6)",//"destLon"
		"TIME",//"tripTime"
		"INT(10)",//"dist"
		"INT(10)",//"dura"
		"VARCHAR(120)" //"participants"
	};

	
	public static final String[] TravelTbcolumns={
		// trip info
		"recordId",
		"roundtrip", 
		"userType", 
		//"dayOfWeek", 
		"tripDate",
		"origState",
		"origCity",
		"origNbhd",
		"origAddr",
		"destState",
		"destCity",
		"destNbhd",
		"destAddr",
		"detourFactor",
		"forwardTime",
		"forwardFlexibility",
		"backTime",
		"backFlexibility",
	};
	
	public static final String[] TravelTbcolumnTypes = {
		// trip info
		"INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE", //"recordId"
		"BOOL", //"roundtrip"
		"BOOL", //"userType"
		//"INT(7)", 
		"DATE", //"tripDate"
		"VARCHAR(20)",//"origState"
		"VARCHAR(20)",//"origCity"
		"VARCHAR(20)",//"origNbhd"
		"VARCHAR(50)",//"origAddr"
		"VARCHAR(20)",//"destState"
		"VARCHAR(20)",//"destCity"
		"VARCHAR(20)",//"destNbhd"
		"VARCHAR(50)",//"destAddr"
		"DECIMAL(3,2)",//"detourFactor"
		"TIME",//"forwardTime"
		"TIME",//"forwardFlexibility"
		"TIME",//"backTime"
		"TIME" //"backFlexibility"
	};
	
	
	public static final String[] UserTbcolumns={
		// login info
		"userId",
		"emailAddress",
		"password",
		"givenname",
		"surname",
		"address",
		"userLevel",
		"avatarID"
	};
	
	public static final String[] UserTbcolumnTypes = {
		"INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE", //"userId"
		"VARCHAR(50)", //"emailAddress"
		"VARCHAR(30)", //"password"
		"VARCHAR(40)", //"givenname"
		"VARCHAR(40)",  //"surname"
		"VARCHAR(500)",  //"address"
		"INT", //"userLevel"
		"VARCHAR(30)" //"avatarID"
		
	};
	
	public static final String[] MessageTbcolumns={
		// login info
		"messageID",
		"senderName",
		"receiverName",
		"content",
		"messageSenderStatus",
		"messageReceiverStatus",
		"Type",  //Commute of travel
		"recordId"
	};
	public static final String[] MessageTbcolumnTypes = {
		"INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE",
		"VARCHAR(50)",
		"VARCHAR(50)",
		"VARCHAR(3000)",
		"INT(2)",
		"INT(2)",
		"INT(1)",  //0 for commute 1 for travel
		"INT"      //Associated message
	};
	
	
	public static final String[] UserHisTbcolumns={
		// login info
		"userid",
		"reputation",
		"totalmiles",
		"dealcount",
		"judgecount"
	};
	public static final String[] UserHisTbcolumnTypes = {
		"INT UNSIGNED NOT NULL UNIQUE",
		"INT(10)",
		"INT(10)",
		"INT(5)",
		"INT(5)",
	};
}
