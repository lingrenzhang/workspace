package com.shs.liangdiaosi.Calc;

import java.sql.*;
import com.shs.liangdiaosi.Access.*;

public class CalculatorTest {

	private static userDBAccess userDBObj;
	
	private static void createTestTable(){
		try
    	{
        	Connection con= userDBObj.getConnection(true);
			// check if table exists
        	DatabaseMetaData dbm = con.getMetaData();
        	ResultSet tables = dbm.getTables(null, null, "userInfo", null);
        	if(tables.next()){
        		Statement sql=con.createStatement();
        		String query = "drop table userInfo";
        		sql.executeUpdate(query);
        	}
        	Statement sql = con.createStatement();
			String query = "CREATE TABLE userInfo (";
			for(int i=0; i<userDBFormat.columns.length; i++){
				if(i>0) query += ", ";
				query += userDBFormat.columns[i] + " " + userDBFormat.columnTypes[i];
			}
			query += ")";
			sql.executeUpdate(query);
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
			String query = "INSERT INTO userInfo (commute, roundtrip, userType, dayOfWeek, tripDate, origState, origCity, origNbhd, origAddr, destState, destCity, destNbhd, destAddr, detourFactor, forwardTime, forwardFlexibility, backTime, backFlexibility) VALUES ";
			query += "(false, false, 'driver', NULL, '2013-08-01', 'CA', 'Stanford', NULL, '44 Olmsted Rd.', 'CA', 'Los Angeles', NULL, '405 Hilgard Ave', 0.1, '09:00:00', '00:30:00', NULL, NULL), ";
			query += "(false, false, 'rider', NULL, '2013-08-01', 'CA', 'Cupertino', NULL, '10983 N Wolfe Rd.', 'CA', 'Los Angeles', NULL, '405 Hilgard Ave', null, '09:45:00', '00:30:00', NULL, NULL)";
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
		//Todo: create a few test cases
	}
}
