package com.hitchride.standardClass;

import java.util.Date;

public interface PersistentStorage {

	public void insertToDB();
	public boolean isChanged();
	public boolean isSaved();
	public Date lastCheckpoint();
	public boolean storageMode(); //True for user Side, false for server batch.
}
