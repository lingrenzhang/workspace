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
				
				break;
			case Zh:
				MyTopic="�����ֵ��г���";
				Participate = "�Ҽ�����г���";
				FreeRide = "δƴ���г�";
				From="������";
				To="Ŀ�ĵ�";
				MyMessages="�ҵ���Ϣ";
				GivenName="��  ";
				SureName="��  ";
				Address="��ַ";
				MyProfile="������Ϣ";
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
}
