package com.shs.liangdiaosi.DbBatchLoad;
import com.shs.liangdiaosi.Access.CarpoolTbAccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

public class LoadFromEride {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection conn = CarpoolTbAccess.getConnection();
    	 try {
	            String sCurrentLine;
	            String sTotalString;
	            sCurrentLine = "";
	            sTotalString = "";
	            java.io.InputStream l_urlStream;
	            java.net.URL l_url = new java.net.URL(
	                    "https://www.erideshare.com/carpool.php?dstate=CA");
	            java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url
	                    .openConnection();
	            l_connection.connect();
	            l_urlStream = l_connection.getInputStream();
	            java.io.BufferedReader l_reader = new java.io.BufferedReader(
	                    new java.io.InputStreamReader(l_urlStream));
	            while ((sCurrentLine = l_reader.readLine()) != null) {
	                sTotalString += sCurrentLine;
	            }
	            System.out.println(sTotalString);

	            System.out.println("====================");
	            
	            Parser parser = Parser.createParser(new String(sTotalString.getBytes(),
                "8859_1"), "8859-1");
	            NodeClassFilter filter = new NodeClassFilter(TableTag.class);
                NodeList nodeList = parser.parse(filter);
                int i=4;
                while (i<=nodeList.size())
                {
	                TableTag tableTag = (TableTag) nodeList.elementAt(i);
	                TableRow[] rows = tableTag.getRows();
	                for (int j = 0; j < rows.length; j++) {
	                	try
	                	{
		                  TableRow tr = (TableRow) rows[j];
		                  TableColumn[] td = tr.getColumns();
		                  TextNode OrigCity=(TextNode)td[0].getFirstChild().getNextSibling();
		                  TextNode OrigStreet=(TextNode)OrigCity.getNextSibling().getNextSibling();
		                  TextNode OrigState=(TextNode)td[1].getFirstChild().getNextSibling();
		                  TextNode DesCity=(TextNode)td[2].getFirstChild();
		                  TextNode DesStreet=(TextNode)DesCity.getNextSibling().getNextSibling();
		                  TextNode DesState=(TextNode)td[3].getFirstChild();
		                  TextNode CommuteDate=(TextNode) td[4].getFirstChild();
		                  LinkTag lt = (LinkTag) td[5].getFirstChild();
		                  TextNode ltid = (TextNode) lt.getFirstChild();
		                  TextNode time = (TextNode) td[6].getFirstChild();
		                  TextNode commuteType = (TextNode) time.getNextSibling().getNextSibling().getNextSibling();
		                  TextNode StartDate = (TextNode) commuteType.getNextSibling().getNextSibling().getNextSibling();
		                  System.out.println("Importing the " + ((i-4)*100+j) + " message to DB...");
		                  
		                  String username=ltid.getText();
		                  boolean roundtrip=false;
		                  boolean userType= (commuteType.getText().contains("offer")||commuteType.getText().contains("any"))? true:false;
		                  int dayOfWeek=1234; //TODO: LoadFromEride.getDay(CommuteDate.getText(););
		                  String origState=OrigState.getText();
		                  String origCity=OrigCity.getText();
		                  if (origCity!=null)
		                  {
		                	  origCity=origCity.substring(0, origCity.length()-2);
              	  
		                  }
		                  String origNbhd = " ";
		                  String origAddr;
		                  if (OrigStreet!=null)
		                  {
		                	  origAddr = OrigStreet.getText();
		                  }
		                  else
		                  {
		                	  origAddr = " ";
		                  }
		                  String destState=DesState.getText();
		                  String destCity=DesCity.getText();
		                  if (destCity!=null)
		                  {
		                	  destCity=destCity.substring(0,destCity.length()-2);
		                  }
		                  
		                  String destNbhd =" ";
		                  String destAddr;
		                  if (DesStreet!=null)
		                  {
		                	  destAddr = DesStreet.getText();
		                  }
		                  else
		                  {
		                	  destAddr = " ";
		                  }
		                  String detourFactor= "0.20"; //Default value for Score matching
		                  String forwardTime = "08:00:00";//TODO
		                  String forwardFlexibility="00:15:00";
		                  String backTime = "00:00:00";
		                  String backFlexibility ="00:15:00";
		                  CarpoolTbAccess.insertValue(username, roundtrip, userType, dayOfWeek, origState, origCity, origNbhd, origAddr, destState, destCity, destNbhd, destAddr, 
		                		  detourFactor, forwardTime, forwardFlexibility, backTime, backFlexibility, true);
	                	}
	                	catch(Exception e)
	                	{
	                		System.out.println(((i-4)*100+j) + " is not properly formated");
	                		e.printStackTrace();
	                	}
	                }
	                i++;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	
	public static int getComDate(String commuteDate)
	{
		int comDate=0;
		return comDate;
	}
}
