package com.hitchride;

import java.util.Date;

public interface IMessageInfo {
	public IUserInfo getFrom();
	public IUserInfo getTo();
	public String getMessageContent();
	public CommuteOwnerRide getOwnerRide();
	public Date getMessageGenerateDate();
}
