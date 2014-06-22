package com.hitchride.language;

public class Dictionary {
	enum Language{
		En,
		Zh
	}
	Language _language;
	public Dictionary(String language)
	{
		this._language = Language.valueOf(language);
		switch (_language){
			case En:
				MyTopic="Topic I Own";
				Participate ="Topic I Participate";
				FreeRide="Free Ride";
				From="From";
				To="To";
				MyMessages="My Messages";
				GivenName="GivenName";
				SureName="SureName ";
				Address="Address  ";
				MyProfile="My Profile";
				Update="Update";
				OldPassword="OldPassword";
				NewPassword="NewPassword";
				ConfirmPassword="ConfirmPassword";
				break;
			case Zh:
				MyTopic="我主持的行程组";
				Participate = "我加入的行程组";
				FreeRide = "未拼车行程";
				From="出发地";
				To="目的地";
				MyMessages="我的消息";
				GivenName="名  ";
				SureName="姓  ";
				Address="地址";
				MyProfile="个人信息";
				Update="更新";
				OldPassword="旧密码";
				NewPassword="新密码";
				ConfirmPassword="确认新密码";
				break;
			
		}
	}
	//phases
	public String MyTopic;
	public String Participate;
	public String FreeRide;
	public String From;
	public String To;
	public String MyMessages;
	public String GivenName;
	public String SureName;
	public String Address;
	public String MyProfile;
	public String Update;
	public String OldPassword;
	public String NewPassword;
	public String ConfirmPassword;

}
