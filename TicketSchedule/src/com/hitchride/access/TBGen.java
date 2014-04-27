package com.hitchride.access;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.global.SQLServerConf;

public class TBGen {
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		Connection conn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return conn;
	}
	
	public static void generateTable(String tableName,DataColumnSchema[] schema) throws ClassNotFoundException, SQLException
	{
		Connection con= TBGen.getConnection();
		// check if table exists
    	DatabaseMetaData dbm = con.getMetaData();
    	ResultSet tables = dbm.getTables(null, null, tableName, null);
    	if(tables.next()){
    		Statement sql=con.createStatement();
    		String query = "drop table "+tableName;
    		sql.executeUpdate(query);
    	}
    	Statement sql = con.createStatement();
		String query = "CREATE TABLE " + tableName +" (";
		for(int i=0; i<schema.length; i++){
			if(i>0) query += ", ";
			query += schema[i]._columnName + " " + schema[i]._fieldDefine;
		}
		query += ")";
		sql.executeUpdate(query);
		sql.close();
		con.close();
	}
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException
	{
		//Run this job to regenerate all the table definition.
		//TBGen.generateTable("Message", TicketScheduleSchemaTb.Message);
		//TBGen.generateTable("Topic", TicketScheduleSchemaTb.Topic);
		///TBGen.generateTable("RideInfo", TicketScheduleSchemaTb.RideInfo);
		//TBGen.generateTable("TopicRide", TicketScheduleSchemaTb.TopicRide);
		TBGen.generateTable("UserGroup", TicketScheduleSchemaTb.UserGroup);
	}
}
