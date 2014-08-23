package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;

import com.hitchride.Message;
import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteTopic;
import com.hitchride.User;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllUsers;
import com.hitchride.environ.DummyData;
import com.hitchride.environ.RecentMessages;
import com.hitchride.environ.SQLServerConf;

public class CommuteTopicAccess {
	public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		if (objConn==null)
		{
			objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		}  
		else
		{
			if (objConn.isClosed())
			{
				objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
			}
		}
		return objConn;
	}
	
	
	public static int insertTopic(CommuteTopic topic)
	{
		int rows=0;
		try
		{
			Statement sql;
			getConnection();
			 
			sql=objConn.createStatement();
			StringBuilder parRideIds= new StringBuilder();
			boolean first=true;
			for(Iterator<CommutePartiRide> pari= topic.parRides.iterator();pari.hasNext();){
				if (first)
				{
					first=false;
				}
				else
				{
					parRideIds.append(",");
				}
				if (parRideIds.length()>100)
				{
					System.out.println("Warning: too much participant ride");
					break;
				}
				parRideIds.append(pari.next()._pid);
			}
			
			StringBuilder reqparRideIds = new StringBuilder();
			first=true;
			for(Iterator<CommutePartiRide> reqpari= topic._requestPride.iterator();reqpari.hasNext();){
				if (first)
				{
					first=false;
				}
				else
				{
					reqparRideIds.append(",");
				}
				if (reqparRideIds.length()>100)
				{
					System.out.println("Warning: too much requst ride");
					break;
				}
				reqparRideIds.append(reqpari.next()._pid);
			}
			
			StringBuilder messages = new StringBuilder();
			first=true;
			for(Iterator<Message> messi= topic.messages.iterator();messi.hasNext();){
				if (first)
				{
					first=false;
				}
				else
				{
					messages.append(",");
				}
				if (messages.length()>100)
				{
					System.out.println("Warning: too much participant ride");
					break;
				}
				reqparRideIds.append(messi.next()._messageId);
			}
			
			rows=sql.executeUpdate("insert into commutetopic (TopicId,OwnerId,ParRideIds,ReqRideIds,MessageIds) values(\"" + 
					topic.get_topicId() + "\",\""
					+ topic.owner.get_uid() +"\",\""
					+ parRideIds.toString() +"\",\""
					+ reqparRideIds.toString() +"\",\""
					+ messages.toString() +"\")");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return rows;
	}
	
	
	public static int updateTopic(CommuteTopic topic)
	{
		int rows=0;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			StringBuilder parRideIds= new StringBuilder();
			boolean first=true;
			for(Iterator<CommutePartiRide> pari= topic.parRides.iterator();pari.hasNext();){
				if (first)
				{
					first=false;
				}
				else
				{
					parRideIds.append(",");
				}
				if (parRideIds.length()>100)
				{
					System.out.println("Warning: too much participant ride");
					break;
				}
				parRideIds.append(pari.next()._pid);
			}
			
			StringBuilder reqparRideIds = new StringBuilder();
			first=true;
			for(Iterator<CommutePartiRide> reqpari= topic._requestPride.iterator();reqpari.hasNext();){
				if (first)
				{
					first=false;
				}
				else
				{
					reqparRideIds.append(",");
				}
				if (reqparRideIds.length()>100)
				{
					System.out.println("Warning: too much requst ride");
					break;
				}
				reqparRideIds.append(reqpari.next()._pid);
			}
			
			StringBuilder messages = new StringBuilder();
			first=true;
			for(Iterator<Message> messi= topic.messages.iterator();messi.hasNext();){
				if (first)
				{
					first=false;
				}
				else
				{
					messages.append(",");
				}
				if (messages.length()>100)
				{
					System.out.println("Warning: too much participant ride");
					break;
				}
				messages.append(messi.next()._messageId);
			}
			
			rows= sql.executeUpdate("update commutetopic set "
					+ "OwnerId=\""+ topic.owner.get_uid() +"\","
					+ "ParRIdeIds=\""+parRideIds.toString() +"\","
					+ "ReqRideIds=\""+reqparRideIds.toString() +"\","
					+ "MessageIds=\""+messages.toString() +"\" "
					+ "where TopicId=\""+topic.get_topicId()+"\"");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return rows;
	}
	
	public static Hashtable<Integer,CommuteTopic> LoadAllTopic(){
		Hashtable<Integer,CommuteTopic> allTopics = new Hashtable<Integer,CommuteTopic>(5000);
		try {
			Statement sql;
			getConnection();
				
			sql=objConn.createStatement();
			ResultSet topicrs = sql.executeQuery("select * from commutetopic");
			while (topicrs.next())
			{
				CommuteTopic topic = new CommuteTopic();
				try {
					topic.set_topicId(topicrs.getInt("TopicId"));
					topic.owner = (User) AllUsers.getUsers().getUser(topicrs.getInt("OwnerId"));
					topic.ownerRide =  AllTopicRides.getTopicRides().getRide(topic.get_topicId());
		
					int[] pRides = parseIds(topicrs.getString("ParRideIds"));
					if (pRides!=null)
					{
						for(int i=0; i<pRides.length;i++)
						{
							CommutePartiRide pRide = AllPartRides.getPartRides().get_participantRide(pRides[i]);
							switch (pRide.get_status())
							{
							    
								case 1:
									topic._requestPride.add(pRide);
									break;
								case 2:
								case 3:
								case 4:
									topic.parRides.add(pRide);
									break;
								default:
									System.out.println("Invalid status code for pride: "+ pRide._pid);
							}
						}
					}
					
					int[] reqRides = parseIds(topicrs.getString("ReqRideIds"));
					if (reqRides!=null)
					{
						for(int i=0; i<reqRides.length;i++)
						{
							CommutePartiRide reqRide = AllPartRides.getPartRides().get_participantRide(reqRides[i]);
							topic._requestPride.add(reqRide);
						}
					}
					
					int[] messages = parseIds(topicrs.getString("messageIds"));
					if (messages!=null)
					{
						for(int i=0; i<messages.length;i++)
						{
							Message message = DummyData.getDummyEnv()._dummyMessage.get(messages[i]);
							//Message message = RecentMessages.getRecMessage()._recMessages.get(messages[i]);
							topic.messages.add(message);
						}
					}
				} catch (SQLException e) {
					System.out.println("Not able to load Topic: " + topic.get_topicId());
					e.printStackTrace();
				}
				allTopics.put(topic.get_topicId(), topic);
				System.out.println("Topic: " + topic.get_topicId()+" loaded.");
			}
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allTopics;
	}
	
	public static void CloseConn() throws SQLException
	{
		if (objConn!=null)
		{
			objConn.close();
			objConn=null;
		}
	};
	
	public static int[] parseIds(String input)
	{
		String[] values = input.split(",");
		if (values[0].equals(""))
			return null;
		
		int[] result = new int[values.length];  //50 for the most
	    for (int i=0; i<values.length;i++)
	    {
	    	result[i] = Integer.parseInt(values[i]);
	    }
		return result;
	}
}
