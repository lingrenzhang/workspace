package com.shs.liangdiaosi.generateSampleDB;
import com.shs.liangdiaosi.Access.userDBAccess;
import java.sql.*;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
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
	            String testText = extractText(sTotalString);
	            System.out.println(testText);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
    public static String extractText(String inputHtml) throws Exception {
        StringBuffer text = new StringBuffer();

        Parser parser = Parser.createParser(new String(inputHtml.getBytes(),
                "8859_1"), "8859-1");

        NodeList nodes = parser.extractAllNodesThatMatch(new NodeFilter() {
            public boolean accept(Node node) {
                return true;
            }
        });
        
        Node node = nodes.elementAt(0);
        text.append(new String(node.toPlainTextString().getBytes("8859_1")));
        return text.toString();
    }
}
