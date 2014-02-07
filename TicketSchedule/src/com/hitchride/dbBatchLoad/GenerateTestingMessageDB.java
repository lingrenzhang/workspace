package com.hitchride.dbBatchLoad;

import java.sql.Connection;
import java.sql.SQLException;

import com.hitchride.access.MessageTbAccess;



public class GenerateTestingMessageDB {
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		MessageTbAccess messageTbAccess = new MessageTbAccess();
		messageTbAccess.insertMessage("coordinate6467@hotmail.com", "abc", "This is a test", 1);
		messageTbAccess.insertMessage("abc", "coordinate6467@hotmail.com,123", "This is a test", 1);
		messageTbAccess.insertMessage("coordinate6467@hotmail.com", "123", "This is a test", 1);
		messageTbAccess.insertMessage("coordinate6467@hotmail.com", "abc", "This is a test", 1);
		
	}
}
