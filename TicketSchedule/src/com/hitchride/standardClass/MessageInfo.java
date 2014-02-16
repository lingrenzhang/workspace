package com.hitchride.standardClass;

import java.util.Date;

public interface MessageInfo {
	public UserInfo getFrom();
	public UserInfo getTo();
	public String getMessageContent();
	public OwnerRideInfo getOwnerRide();
	public Date getMessageGenerateDate();
}
