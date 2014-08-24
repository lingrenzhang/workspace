package com.hitchride;

import java.util.List;

public interface IMailList {
	public List<User> getAllRelevantUser();
	public List<User> getUsersExcept(int uid);
	public List<User> getUsersExcept(User euser);
}
