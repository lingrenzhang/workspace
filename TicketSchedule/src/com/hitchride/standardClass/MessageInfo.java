package com.hitchride.standardClass;

import java.util.Date;

public interface MessageInfo {
	public User getFrom();
	public User getTo();
	public String getMessageContent();
	public OwnerRideInfo getOwnerRide();
	public Date getMessageGenerateDate();
}
