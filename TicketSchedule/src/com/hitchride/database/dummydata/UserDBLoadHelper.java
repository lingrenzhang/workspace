package com.hitchride.database.dummydata;

//Note: Use jxl.jar
//Supported version is Excel 2003
//Currently use hard coded structure
//1st line is UserID
//2nd line is Name in Chinese, first character for Sure Name, then for Given Name
//3rd line is Password
//4th line is CellPhone

import com.hitchride.User;
import com.hitchride.database.access.UserAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class UserDBLoadHelper {
	String inputname;
	String inputtype; //Only supports Excel now.
	
	List<User> user= new ArrayList<User>();
	
	public UserDBLoadHelper(String inputname)
	{
		this.inputname=inputname;
		this.inputtype="XLS";
	}

	public void loadXLStoUser()
	{
	
		Workbook wb = null;
		try{
			wb = Workbook.getWorkbook(new File(this.inputname));
		}catch(BiffException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if (wb==null)
			return;
		
		Sheet[] sheet = wb.getSheets();
		if (sheet!=null&&sheet.length>0){
			for(int i=0;i<sheet.length;i++)
			{
				int rowNum = sheet[i].getRows();
				for (int j=4;j<rowNum;j++) //First three line are header.
				{
					Cell[] userRow = sheet[i].getRow(j);
					if (rowCheck(userRow))
					{
						String username= userRow[1].getContents().trim();
						String name = userRow[2].getContents().trim();
						//String givenname = name.substring(1);
						String givenname = name;
						String surename = name.substring(0,1);
						String password = userRow[3].getContents().trim();
						String cellphone = userRow[4].getContents().trim();
						System.out.print("User "+(j-3) +" inserted: ");
						UserAccess userTb = new UserAccess();
						System.out.println(givenname);
						try
						{
							userTb.insertValue(username,1, password,givenname, surename,"",4,"default.jpg",cellphone);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						System.out.println("Issue with line:"+(j-3));
					}
				}
			}
		}
	}
	
	//Pre Check whether an input row is valid. 
	//Use sanity check later.
	public boolean rowCheck(Cell[] row)
	{
		boolean result = true;
		if (row.length!=5)
		{
			System.out.println("Missing parameter");
			return false;
		}
		if (!(null!=row[1].getContents() && !"".equals(row[1].getContents())))
		{
			System.out.println("Id not valid");
			return false;
		}
		if (!(null!=row[2].getContents() && !"".equals(row[2].getContents())))
		{
			System.out.println("Name not valid");
			return false;
		}
		if (!(null!=row[3].getContents() && !"".equals(row[3].getContents())))
		{
			System.out.println("Password not valid");
			return false;
		}
		if (!(null!=row[4].getContents() && !"".equals(row[4].getContents())))
		{
			System.out.println("Warning: Cell phone not valid for "+row[2].getContents());
			return true;
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		UserDBLoadHelper DBloaderHelper = new UserDBLoadHelper("C:/UserDB/Taicang.xls");
		DBloaderHelper.loadXLStoUser();
	}
}
