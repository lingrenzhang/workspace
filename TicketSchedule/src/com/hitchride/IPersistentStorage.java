package com.hitchride;

import java.util.Date;

public interface IPersistentStorage {

	public void insertToDB();
	public boolean isChanged();
	public boolean isSaved();
	public Date lastCheckpoint();
	public boolean storageMode(); //True for user Side, false for server batch.
}
