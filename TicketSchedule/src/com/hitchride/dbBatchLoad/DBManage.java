package com.hitchride.dbBatchLoad;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.hitchride.access.DataColumnSchema;
import com.hitchride.access.TicketScheduleSchemaTb;
import com.hitchride.global.SQLServerConf;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;



public class DBManage {
	
	public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		objConn = (Connection) DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return objConn;
	}
	
	//This function only puts data to the table. Manually add the field definition in 
	//TicketScheduleSchemaTb first
	//Use Alter table tbname modify column columnname columndefinition.
	//Alter table tbname add column... 
	@Deprecated
	public void addField(String tableName,String fieldName,Object fieldDefaultValue)
	{
		//Load every content to memory, update the table, 
		//then insert value with fieldDefaultValue added.
		DataColumnSchema[] internalSchema = TicketScheduleSchemaTb.getSchemaByName(tableName);
		//TBGen.generateTable("TableBack", internalSchema);
		//Load content to the Ta
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=(Statement) objConn.createStatement();
				
			String query = "select * from " + tableName;
			//sql.setFetchSize(rows);
			ResultSet rs = (ResultSet) sql.executeQuery(query);
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			
			
			
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}


	public static void main(String args[])
	{
		
	}
}
