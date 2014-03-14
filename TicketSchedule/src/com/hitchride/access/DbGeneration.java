package com.hitchride.access;
import java.sql.*;

import com.hitchride.access.TicketScheduleDBFormat;


@Deprecated
public class DbGeneration {
		
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		//String url = "jdbc:mysql://rs.luzhuoer.info/ticketschedule";
		String url = "jdbc:mysql://localhost/ticketschedule";
		Class.forName("com.mysql.jdbc.Driver");
		String userName="root";
		String password="rideshare";
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
		for(int i=0; i<TicketScheduleDBFormat.UserTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += TicketScheduleDBFormat.UserTbcolumns[i] + " " + TicketScheduleDBFormat.UserTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
		sql.close();
		con.close();
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
		for(int i=0; i<TicketScheduleDBFormat.CarpoolTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += TicketScheduleDBFormat.CarpoolTbcolumns[i] + " " + TicketScheduleDBFormat.CarpoolTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
		sql.close();
		con.close();
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
		for(int i=0; i<TicketScheduleDBFormat.TravelTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += TicketScheduleDBFormat.TravelTbcolumns[i] + " " + TicketScheduleDBFormat.TravelTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
		sql.close();
		con.close();
		
	}
	
	
	public static void generateMessageTb() throws ClassNotFoundException, SQLException
	{
		Connection con= DbGeneration.getConnection();
		// check if table exists
    	DatabaseMetaData dbm = con.getMetaData();
    	ResultSet tables = dbm.getTables(null, null, "MessageTb", null);
    	if(tables.next()){
    		Statement sql=con.createStatement();
    		String query = "drop table MessageTb";
    		sql.executeUpdate(query);
    	}
    	Statement sql = con.createStatement();
		String query = "CREATE TABLE MessageTb (";
		for(int i=0; i<TicketScheduleDBFormat.MessageTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += TicketScheduleDBFormat.MessageTbcolumns[i] + " " + TicketScheduleDBFormat.MessageTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
		sql.close();
		con.close();
		
	}
	
	public static void generateUserHisTb() throws ClassNotFoundException, SQLException
	{
		Connection con= DbGeneration.getConnection();
		// check if table exists
    	DatabaseMetaData dbm = con.getMetaData();
    	ResultSet tables = dbm.getTables(null, null, "UserHisTb", null);
    	if(tables.next()){
    		Statement sql=con.createStatement();
    		String query = "drop table UserHisTb";
    		sql.executeUpdate(query);
    	}
    	Statement sql = con.createStatement();
		String query = "CREATE TABLE UserHisTb (";
		for(int i=0; i<TicketScheduleDBFormat.UserHisTbcolumns.length; i++){
			if(i>0) query += ", ";
			query += TicketScheduleDBFormat.UserHisTbcolumns[i] + " " + TicketScheduleDBFormat.UserHisTbcolumnTypes[i];
		}
		query += ")";
		sql.executeUpdate(query);
		sql.close();
		con.close();
		
	}
	public static void main(String args[]) throws ClassNotFoundException, SQLException
	{
		//Run this job to regenerate all the table definition.
		//DbGeneration.generateUserTable();
		//DbGeneration.generateCarpoolTable();
		//DbGeneration.generateTravelTb();
		//DbGeneration.generateMessageTb();
		//DbGeneration.generateUserTable();
		DbGeneration.generateUserHisTb();
	}
}
