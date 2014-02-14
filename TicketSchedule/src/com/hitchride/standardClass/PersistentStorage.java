package com.hitchride.standardClass;

public interface PersistentStorage {

	public void insertToDB();
	public boolean isChanged();
	public boolean isSaved();
	public boolean lastCheckpoint();
}
