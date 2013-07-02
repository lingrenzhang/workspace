package com.shs.liangdiaosi.Access;
import java.sql.*;

public class DbGeneration {
	public static final String[] UserTbcolumns={
		// login info
		"userName",
		"password",
		"emailAddress"
	};
	
	public static final String[] UserTbcolumnTypes = {
		"VARCHAR(30)", //"userName"
		"VARCHAR(30)", //"password"
		"VARCHAR(30)"  //"emailAddress"
	};
	
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
	};
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
		"TIME" //"backFlexibility"
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
		
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost/ticketschedule";
		Class.forName("com.mysql.jdbc.Driver");
		String userName="admin";
		String password="admin";
		Connection con = DriverManager.getConnection(url,userName,password);
		return con;
	}
	
	public static void generateUserTable() throws ClassNotFoundException, SQLException
	{
		Connection con= DbGeneration.getConnection();
		// check if table exists
    	DatabaseMetaData dbm = con.getMetaData();
    	ResultSet tables = dbm.getTables(null, null, "userTb", null);
    	if(tables.next()){
    		Statement sql=con.createStatement();
    		String query = "drop table userTb";
    		sql.executeUpdate(query);
    	}
    	Statement sql = con.createStatement();
		String query = "CREATE TABLE userTb (";
		for(int i=0; i<DbGeneration.UserTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += DbGeneration.UserTbcolumns[i] + " " + DbGeneration.UserTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
	}
	
	public static void generateCarpoolTable() throws ClassNotFoundException, SQLException
	{
		Connection con= DbGeneration.getConnection();
		// check if table exists
    	DatabaseMetaData dbm = con.getMetaData();
    	ResultSet tables = dbm.getTables(null, null, "CarpoolTb", null);
    	if(tables.next()){
    		Statement sql=con.createStatement();
    		String query = "drop table CarpoolTb";
    		sql.executeUpdate(query);
    	}
    	Statement sql = con.createStatement();
		String query = "CREATE TABLE CarpoolTb (";
		for(int i=0; i<DbGeneration.CarpoolTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += DbGeneration.CarpoolTbcolumns[i] + " " + DbGeneration.CarpoolTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
	}
	
	public static void generateTravelTb() throws ClassNotFoundException, SQLException
	{
		Connection con= DbGeneration.getConnection();
		// check if table exists
    	DatabaseMetaData dbm = con.getMetaData();
    	ResultSet tables = dbm.getTables(null, null, "TravelTb", null);
    	if(tables.next()){
    		Statement sql=con.createStatement();
    		String query = "drop table TravelTb";
    		sql.executeUpdate(query);
    	}
    	Statement sql = con.createStatement();
		String query = "CREATE TABLE TravelTb (";
		for(int i=0; i<DbGeneration.TravelTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += DbGeneration.TravelTbcolumns[i] + " " + DbGeneration.TravelTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
		
	}
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException
	{
		//Run this job to regenerate all the table definition.
		DbGeneration.generateUserTable();
		DbGeneration.generateCarpoolTable();
		DbGeneration.generateTravelTb();
	}
}
