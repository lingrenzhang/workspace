package com.hitchride.database.access;

//Used to increase database integrity
public class TriggersScript {
	public static final String deleteUser="create trigger deleteUser on usertb for delete as "
			+"declare @userid"
			+"select @userid=userid,";
}
