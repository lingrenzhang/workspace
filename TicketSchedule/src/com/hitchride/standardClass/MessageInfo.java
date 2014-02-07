

package com.hitchride.standardClass;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import com.hitchride.access.CarpoolTbAccess;
import com.mysql.jdbc.ResultSet;
//This class is to represent the standard format of dataStructure for Message Box.
//Shield the front end from direct access to DB. Using runtime object for performance increase
//and de-couple physical persistent record with runtime server.  
//Later will put the MessageInfo as list in the runtime. 
//Any fetch will first check the MessageInfo from memory. Use regular commit to persistent storage.
//Has relation to both carpoolTb and messageTb in terms of persistent storage. Easier the table relation in mySQL.
//Not sure the detailed mySQL multi-table relationship supporting level.
//Think about more complicate use like trigger, cascade, etc later when necessary. 
public class MessageInfo {
	private int statusflag = -1; //-1 for not initialized, 0 for same with DB, 1 for has local change,
	public int recordID;
	public String origCity;
	public String origAddr;
	public String destCity;
	public String destAddr;
	public String owner;
	public Hashtable<String,Integer> userstatus; //User Defined type,saved as pure string in mySQL DB.
	                                        //Change when verify mySQL support array type
	public List<String> messages;           //Messages associated with this particular ride.
	//String for target User, integer for User status with the message. 
	// 0:participant check with recorder owner
	// 1:owner respond to participant 
	// 2:Commit the deal.   
	
	public MessageInfo(String rid) {
		//"select recordId,origCity,origAddr,destCity,destAddr from carpoolTb where recordId="+recordId
		ResultSet rs;
		try {
			
			rs = CarpoolTbAccess.getForMessageBox(rid);
		    if (rs.next())
		    {
		    	recordID = rs.getInt("recordID");
		    	origCity = rs.getString("origCity");
		    	origAddr = rs.getString("origAddr");
		    	destCity = rs.getString("destCity");
		    	destAddr = rs.getString("destAddr");
		    	owner = rs.getString("userName");

		    	String participants = rs.getString("participants");
			    if (participants!=null && !participants.equals(""))
			    {
		    		loadUsers(participants);
		    	}
		    }
    
		    else{
		    //TO DO: handle record not found exception. 
		    }
		    statusflag = 0;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	//Switch users in DB to users in MessageInfo class
	public void loadUsers(String users)
	{
		//Reference format: xiaoa,0;xiaob,1;xiaoc,0;...
		try
		{
			String[] _us = users.split(";");
			for (int i=0;i<_us.length;i++)
			{
				String[] ps = _us[i].split(",");
				String name = ps[0];
				Integer status = Integer.parseInt(ps[1]);
				userstatus.put(name, status);
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.out.println("Warning: Malformat of user field in CarpoolDB.");
		}
		
	}
	
	//TO DO: Update persistent storage if found status flag is 1 in finalizer.
	
}
