package com.hitchride.dbBatchLoad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.hitchride.access.CarpoolTbAccess;
import com.hitchride.access.UserTbAccess;

//Generate sample userDB from Carpool DB for development purpose.
public class LoadUserFromRide {
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		Random rnd = new Random();
		UserTbAccess utb= new UserTbAccess();
		ResultSet rs = CarpoolTbAccess.rideInitialLoad();
		while (rs.next())
		{
			String name = rs.getString("username");
			String address = rs.getString("origAddr");
			utb.insertValue(name, "111111", name, "", address, rnd.nextInt(10), "default.jpg");
		}

	}
}
