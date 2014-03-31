package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;

import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllUsers;
import com.hitchride.global.DummyData;
import com.hitchride.global.SQLServerConf;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.User;

public class TopicTbAccess {
	public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return objConn;
	}
	
	public static void insertTopic(Topic topic)
	{
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			StringBuilder parRideIds= new StringBuilder();
			boolean first=true;
			for(Iterator<ParticipantRide> pari= topic.parRides.iterator();pari.hasNext();){
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
			for(Iterator<ParticipantRide> reqpari= topic._requestPride.iterator();reqpari.hasNext();){
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
			
			sql.execute("insert into Topic (TopicId,OwnerId,ParRideIds,ReqRideIds,MessageIds) values(\"" + 
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
	}
	
	
	public static void updateTopic(Topic topic)
	{
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			StringBuilder parRideIds= new StringBuilder();
			boolean first=true;
			for(Iterator<ParticipantRide> pari= topic.parRides.iterator();pari.hasNext();){
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
			for(Iterator<ParticipantRide> reqpari= topic._requestPride.iterator();reqpari.hasNext();){
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
			
			sql.execute("update Topic set "
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
	}
	
	public static Hashtable<Integer,Topic> LoadAllTopic(){
		Hashtable<Integer,Topic> allTopics = new Hashtable<Integer,Topic>(5000);
		try {
			Statement sql;
			if (objConn==null)
			{
				
					objConn = getConnection();
				
			} 
			sql=objConn.createStatement();
			ResultSet topicrs = sql.executeQuery("select * from Topic");
			while (topicrs.next())
			{
				Topic topic = new Topic();
				try {
					topic.set_topicId(topicrs.getInt("TopicId"));
					topic.owner = (User) AllUsers.getUsers().getUser(topicrs.getInt("OwnerId"));
					topic.ownerRide =  AllTopicRides.getTopicRides().getRide(topic.get_topicId());
		
					int[] pRides = parseIds(topicrs.getString("ParRideIds"));
					if (pRides!=null)
					{
						for(int i=0; i<pRides.length;i++)
						{
							ParticipantRide pRide = AllPartRides.getPartRides().get_participantRide(pRides[i]);
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
							ParticipantRide reqRide = AllPartRides.getPartRides().get_participantRide(reqRides[i]);
							topic._requestPride.add(reqRide);
						}
					}
					
					int[] messages = parseIds(topicrs.getString("messageIds"));
					if (messages!=null)
					{
						for(int i=0; i<messages.length;i++)
						{
							Message message = DummyData.getDummyEnv()._dummyMessage.get(messages[i]);
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