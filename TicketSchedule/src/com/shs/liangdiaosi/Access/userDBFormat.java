package com.shs.liangdiaosi.Access;

public class userDBFormat {
	public static final String[] columns={
		// login info
		"userName",
		"password",
		"emailAddress",
		// trip info
		"recordId",
		"commute", 
		"roundtrip", 
		"userType", // true means driver, false means passenger
		"dayOfWeek", 
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
	public static final String[] columnTypes = {
		"VARCHAR(30)",
		"VARCHAR(30)",
		"VARCHAR(30)",
		// trip info
		"INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE",
		"BOOL",
		"BOOL",
		"VARCHAR(8)",
		"VARCHAR(8)",
		"DATE",
		"VARCHAR(20)",
		"VARCHAR(20)",
		"VARCHAR(20)",
		"VARCHAR(50)",
		"VARCHAR(20)",
		"VARCHAR(20)",
		"VARCHAR(20)",
		"VARCHAR(50)",
		"DECIMAL(3,2)",
		"TIME",
		"TIME",
		"TIME",
		"TIME",
	};

}
