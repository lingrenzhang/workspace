package com.shs.liangdiaosi.Calc;

import java.sql.*;

import com.shs.liangdiaosi.Access.userDBAccess;

public class CalculatorTest {

	private static userDBAccess userDBObj;
	private static final String[] columns={
		// login info
		"userName",
		"password",
		"emailAddress",
		// trip info
		"recordId",
		"commute", 
		"roundtrip", 
		"userType", 
		"dayOfWeek", 
		"tripDate",
		"origin",
		"destination",
		"detourFactor",
		"forwardTime",
		"forwardFlexibility",
		"backTime",
		"backFlexibility",
	};
	private static final String[] columnTypes = {
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
		"VARCHAR(50)",
		"VARCHAR(50)",
		"DECIMAL(3,2)",
		"TIME",
		"TIME",
		"TIME",
		"TIME",
	};
	private static void createTestTable(){
		try
    	{
        	Connection con= userDBObj.getConnection(true);
			// check if table exists
        	DatabaseMetaData dbm = con.getMetaData();
        	ResultSet tables = dbm.getTables(null, null, "userInfo", null);
        	if(tables.next()){
        		Statement sql=con.createStatement();
        		String query = "delete from userInfo";
        		sql.executeUpdate(query);
        	}
        	else{
	        	Statement sql = con.createStatement();
				String query = "CREATE TABLE userInfo (";
				for(int i=0; i<columns.length; i++){
					if(i>0) query += ", ";
					query += columns[i] + " " + columnTypes[i];
				}
				query += ")";
				sql.executeUpdate(query);
        	};
    	}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	private static void insertTestEntries(){
		try {
			Connection con = userDBObj.getConnection(true);
			Statement sql = con.createStatement();
			String query = "INSERT INTO userInfo (commute, roundtrip, userType, dayOfWeek, tripDate, origin, destination, detourFactor, forwardTime, forwardFlexibility, backTime, backFlexibility) VALUES ";
			query += "(false, false, 'driver', NULL, '2013-08-01', '44 Olmsted Rd.,Apt 133, Stanford, CA', '405 Hilgard Ave, Los Angeles, CA', 0.1, '09:00:00', '00:30:00', NULL, NULL), ";
			query += "(false, false, 'rider', NULL, '2013-08-01', '10983 N Wolfe Rd  Cupertino, CA', '405 Hilgard Ave, Los Angeles, CA', 0.1, '09:45:00', '00:30:00', NULL, NULL)";
			sql.executeUpdate(query);
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:"+e.getMessage());
		} catch (SQLException e) {
			System.err.println("SQLException:"+e.getMessage());
		}
		
	}
	
	public static void main(String[] args) {
		userDBObj = new userDBAccess();
		userDBObj.setTest(true);
		createTestTable();
		insertTestEntries();
		//create a few test cases
	}
}
