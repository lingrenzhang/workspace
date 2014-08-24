package com.hitchride.authorization;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class AuthorizationHelper {
	private Hashtable<String,Integer> methodTable = new Hashtable<String,Integer>();
	
	public AuthorizationHelper()
	{
		//Environment issue here when working with JavaEE server
		LoadMethodTable("./src/com/hitchride/authorization/MethodLevelDefine.xml");
	}
	
	public AuthorizationHelper(String methodTableSrc){
		LoadMethodTable(methodTableSrc);
	}
	
	private void LoadMethodTable(String methodTableSrc) {
		//Load methodDefineTable
		DocumentBuilderFactory docbldFactory = DocumentBuilderFactory.newInstance();
		try {
				DocumentBuilder docbld = docbldFactory.newDocumentBuilder();
				Document doc = docbld.parse(methodTableSrc);
				Element docRoot= doc.getDocumentElement();
				Element methodAuth = (Element) docRoot.getElementsByTagName("MethodAuth").item(0);
				NodeList methods = methodAuth.getChildNodes();
				
				for (int i=0; i<methods.getLength();i++)
				{
					Node method = methods.item(i);
					if (method.getNodeType()==1) //For element Type
					{
						methodTable.put(method.getNodeName(), Integer.parseInt(method.getTextContent()));
					}
				}
				
		} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	
	public boolean CheckAuthority(String methodName, int UserGroupLevel)
	{
		Integer minLevel = methodTable.get(methodName);
		if (minLevel == null)
		{
			System.out.println("Authority of method: "+ methodName +" not defined. Treat as valid");
			return true;
		}
		
		if (UserGroupLevel>=minLevel)
		{
			return true;
		}
		return false;
	}
	
	//URL is for a third page
	public void CheckAuthority(String methodName, int UserGroupLevel, String URL)
	{
		boolean valid = CheckAuthority(methodName,UserGroupLevel);
		if (!valid)
		{
			//TO DO: page switch
		}
	}
	public static void main(String[] args) //For unit test.
	{
		AuthorizationHelper authhelper = new AuthorizationHelper();
		System.out.println(authhelper.CheckAuthority("ab", 8));
		System.out.println(authhelper.CheckAuthority("SearchCommuteTopic", 8));
	}
}
