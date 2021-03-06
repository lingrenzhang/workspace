package com.hitchride.database.dummydata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.hitchride.database.access.CarpoolTbAccess;
import com.hitchride.database.access.UserAccess;

//Generate sample userDB from Carpool DB for development purpose.
public class LoadUserFromRide {
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		Random rnd = new Random();
		UserAccess utb= new UserAccess();
		ResultSet rs = CarpoolTbAccess.rideInitialLoad();
		while (rs.next())
		{
			String name = rs.getString("username");
			String address = rs.getString("origAddr");
			try {
				utb.insertValue(name, 1,"111111", name, "", address, rnd.nextInt(10), "default.jpg","13112345678");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
