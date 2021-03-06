package com.hitchride;

import java.util.Date;

import com.hitchride.database.access.TransientTopicAccess;
import com.hitchride.environ.AllUsers;

public class TransientTopic implements IPersistentStorage{

	public int id;
	public int ownerId = 0; //This one is not saved in db schema and can not be changed by client. 
	public int nmiddlePoints=0;
	public GeoInfo[] middle;
	public int nParticipant=0;
	public int[] partiuid;
	public User[] parti;
	
	public int get_ownerId()
	{
		if (ownerId == 0)
		{
			System.out.println("ownerID not initalized.");
		}
		return this.ownerId;
	}
	
	
	public TransientTopic(int transientRideId)
	{
		this.id = transientRideId;
		GeoInfo middle0 = new GeoInfo(0,0);
		GeoInfo middle1 = new GeoInfo(0,0);
		GeoInfo middle2 = new GeoInfo(0,0);
		GeoInfo middle3 = new GeoInfo(0,0);
		GeoInfo middle4 = new GeoInfo(0,0);
		GeoInfo middle5 = new GeoInfo(0,0); //This one is just to reduce the side effect. 
		middle = new GeoInfo[]{middle0,middle1,middle2,middle3,middle4,middle5};
		partiuid = new int[]{0,0,0,0,0,0};
		parti = new User[]{null,null,null,null,null,null};
	}
	
	public TransientTopic(int transientRideId, int ownerId)
	{
		this.id = transientRideId;
		this.ownerId = ownerId;
		GeoInfo middle0 = new GeoInfo(0,0);
		GeoInfo middle1 = new GeoInfo(0,0);
		GeoInfo middle2 = new GeoInfo(0,0);
		GeoInfo middle3 = new GeoInfo(0,0);
		GeoInfo middle4 = new GeoInfo(0,0);
		GeoInfo middle5 = new GeoInfo(0,0); //This one is just to reduce the side effect. 
		middle = new GeoInfo[]{middle0,middle1,middle2,middle3,middle4,middle5};
		partiuid = new int[]{0,0,0,0,0,0};
		parti = new User[]{null,null,null,null,null,null};
	}
	
	public boolean addMiddlePoint(GeoInfo geo)
	{
		if (nmiddlePoints>=5)
		{
			System.out.print("Middle Point full for transientRideId: "+id);
			return false;
		}
		else
		{
			middle[nmiddlePoints]=geo;
			nmiddlePoints++;
		}
		return true;
	}
	
	public boolean deleteMiddlePoint(int nmiddle)
	{
		if (nmiddle>=nmiddlePoints)
		{
			System.out.print("Delete Number out of range.");
			return false;
		}
		else
		{
			for(int i=nmiddle;i<nmiddlePoints;i++)
			{
				if (i==4)
				{
					middle[i]=new GeoInfo(0,0);
				}
				else
				{
					middle[i]=middle[i+1];
				}
			}
			middle[nmiddlePoints]=new GeoInfo(0,0);
			nmiddlePoints--;
		}
		return true;
	}
	
	
	public boolean addParti(int uid)
	{
		if (nParticipant>=5)
		{
			System.out.print("Participants full for this ride: "+id);
			return false;
		}
		else
		{
			partiuid[nParticipant]=uid;
			parti[nParticipant] = (User) AllUsers.getUsers().getUser(uid);
			nParticipant++;
		}
		return true;
	}
	
	public boolean removeParti(int uid)
	{
		boolean found=false;
		for(int i=0;i<nParticipant;i++)
		{
			if (partiuid[i]==uid)
			{
				found=true;
				for (int j=i;j<nParticipant;j++)
				{
					if (j==4)
					{
						partiuid[j]=0;
						parti[j]=null;
					}
					else
					{
						partiuid[j]=partiuid[j+1];
						parti[j]=parti[j+1];
					}
				}
				nParticipant--;
			}
		}
		return found;
	}
	
	@Override
	public void insertToDB() {
		int rows = TransientTopicAccess.insertTransientTopic(this);
		if (rows==0)
		{
			System.out.println("Insert failed for transientTopic: "+ this.id + " attempting update.");
			rows = TransientTopicAccess.updateTransientTopic(this);
			if (rows==0)
			{
				System.out.println("Update also failed for transientTopic: "+ this.id + " Please check DB integrity.");
			}
		}
		
	}
	
	public void updateDB(){
		int rows = TransientTopicAccess.updateTransientTopic(this);
  		if (rows==0)
  		{
 			System.out.println("Update failed for transientTopic: "+ this.id + " attempting insert.");
  			rows = TransientTopicAccess.insertTransientTopic(this);
  			if (rows==0)
  			{
  	  			System.out.println("Insert also failed for rideinfo: "+ this.id + " Check DB integrity.");
  			}
  		}
	}
	
	
	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isSaved() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Date lastCheckpoint() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean storageMode() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
