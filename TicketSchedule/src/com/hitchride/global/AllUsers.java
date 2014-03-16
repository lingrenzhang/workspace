package com.hitchride.global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.hitchride.access.UserTbAccess;
import com.hitchride.standardClass.User;
import com.hitchride.standardClass.UserInfo;

public class AllUsers {
	private static AllUsers allUsers;
	private AllUsers(){
			_users = new Hashtable<Integer,UserInfo>(100000);
			initialAllUser();
			_actUsers = new Hashtable<Integer,UserInfo>(10000);
			_nactuser=0;
	}
	
	public static AllUsers getUsers(){
		if (null == allUsers){
			{
				allUsers = new AllUsers();
			}
		}
		return allUsers;
		
	}

	//All user object reference can be directly accessed through UID
	public Hashtable<Integer,UserInfo> _users; //All Users. Represent by UID.
	//All Act user object reference can be directly accessed through UID
	public Hashtable<Integer,UserInfo> _actUsers; //Active users. Represent by UID.

	public int _nactuser;

	private void initialAllUser() {
		UserTbAccess utb = new UserTbAccess();
		System.out.println("_users initializing: Loaded from DB.");
		int i=0;
		try {
			ResultSet users = utb.showall();
			while(users.next())
			{
				User user = new User();
				user.set_uid(users.getInt("userID"));
				user.set_name(users.getString("givenname"));
				user.set_avatarID(users.getString("avatarID"));
				_users.put(user.get_uid(), user);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("_users DB load failed");
		}
		System.out.println("#"+i+" users loaded.");
	}

	
	

	
	public UserInfo getUser(int UID)
	{
		UserInfo user = _users.get(UID);
		return user;
	}
	public UserInfo getActUser(int UID)
	{
		UserInfo user = _actUsers.get(UID);
		return user;
	}
	


	public void addActiveUser(int uID) {
	     UserInfo user = _users.get(uID);
	     _actUsers.put(uID, user);
	     _nactuser++;
	}
	
	//Return the first user object when String name matches
	//Temper solution, think about DB structure later.
	public UserInfo getUser(String name)
	{
		Enumeration<UserInfo> enu = _users.elements();
		while (enu.hasMoreElements())
		{
			UserInfo result = enu.nextElement();
			if (result.get_name().equalsIgnoreCase(name))
			return result;
		}
		return null;
	}
}
