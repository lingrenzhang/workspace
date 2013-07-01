package com.shs.liangdiaosi.generateSampleDB;
import com.shs.liangdiaosi.Access.userDBAccess;

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
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

public class LoadFromEride {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection conn = userDBAccess.getConnection(true);
		
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
		                  TableRow tr = (TableRow) rows[j];
		                  TableColumn[] td = tr.getColumns();
		                  
		                  /*for (int k = 0; k < td.length; k++) {		                   		                   */
		                     //Parsing operation, DB operation
		                  
	                }
	                i++;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
