package com.hitchride;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;



/**
 * Servlet implementation class AvaterService
 */
public class AvaterService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AvaterService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/JPEG");
		response.setHeader("Pragma", "No-Cache");
	         //清除该页输出缓存，设置该页无缓存 
			/*
	            context.Response.Buffer = true;
	            context.Response.ExpiresAbsolute = System.DateTime.Now.AddMilliseconds(0);
	            context.Response.Expires = 0;
	            context.Response.CacheControl = "no-cache";
	            context.Response.AppendHeader("Pragma", "No-Cache");

	            using (Image viewimg = GetViewImage())
	            {
	                using (MemoryStream ms = new MemoryStream())
	                {
	                    viewimg.Save(ms, ImageFormat.Jpeg);
	                    context.Response.ClearContent();
	                    context.Response.BinaryWrite(ms.ToArray());
	                }
	            }
	            */
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");   
		response.setCharacterEncoding("UTF-8");
		String realDir = request.getSession().getServletContext().getRealPath("");
		System.out.print("Picdir is" + realDir);
		String contextpath = request.getContextPath();
		String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ contextpath + "/";
	
		try {
			String filePath = "UserProfile";
			String realPath = realDir+"/"+filePath;
			//Create path if not exist
			File dir = new File(realPath);
			if(!dir.isDirectory())
			    dir.mkdir();
	
			if(ServletFileUpload.isMultipartContent(request)){
			    DiskFileItemFactory dff = new DiskFileItemFactory();
			    dff.setRepository(dir);
			    dff.setSizeThreshold(1024000);
			    ServletFileUpload sfu = new ServletFileUpload(dff);
			    FileItemIterator fii = null;
			    fii = sfu.getItemIterator(request);
			    String title = "";   //Picture Title
			    String url = "";    //Picture Path
			    String fileName = "";
				String state="SUCCESS";
				String realFileName="";
	
	            String msg = "";
	            String result = "1";
	            String ww = "170";
	            String hh = "170";
	            String size = "1";//Zoom in
				/*
				 Process image with java.awt
				 Image image = ImageIO.read(source);
				 //2. draw original image to thumbnail image object and scale it to the new size on-the-fly
				 BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGBlack Eye;
				 Graphics2D graphics2D = thumbImage.createGraphics();
				    //...handle with graphics2D, please do this by yourself...      
				 //3. save thumbnail image to OUTFILE
				 BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
				 JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				 */
			    while(fii.hasNext()){
			        FileItemStream fis = fii.next();
			        try{
			            if(!fis.isFormField() && fis.getName().length()>0){
			                fileName = fis.getName();
			                fileName = fileName.toLowerCase();
							Pattern reg=Pattern.compile("[.]jpg|png|jpeg|gif$");
							Matcher matcher=reg.matcher(fileName);
							if(!matcher.find()) {
								state = "Illegal format！";
								response.getWriter().print("{\"msg\": \"Illegal file format.\"}");
								break;
							}
							realFileName = new Date().getTime()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
			                url = realPath+"/"+realFileName;
	
			                BufferedInputStream in = new BufferedInputStream(fis.openStream());//Get input stream
			                FileOutputStream a = new FileOutputStream(new File(url));
			                BufferedOutputStream output = new BufferedOutputStream(a);
			                Streams.copy(in, output, true);//Write to target upload folder
			            }else{
			                String fname = fis.getFieldName();
	
			                if(fname.indexOf("pictitle")!=-1){
			                    BufferedInputStream in = new BufferedInputStream(fis.openStream());
			                    byte c [] = new byte[10];
			                    int n = 0;
			                    while((n=in.read(c))!=-1){
			                        title = new String(c,0,n);
			                        break;
			                    }
			                }
			            }
			        }catch(Exception e){
			            e.printStackTrace();
			        }
			    }
			    response.setStatus(200);
			    String retxt="";
			    if (!realFileName.equalsIgnoreCase(""))
			    {
				    msg= basePath+filePath+"/"+realFileName;
				    //String retxt ="{src:'"+basePath+filePath+"/"+realFileName+"',status:success}";
				    retxt="{ \"result\":" + result + ",\"size\":" + size + 
				    ",\"msg\":\"" + msg + "\",\"avatarID\":\"" + realFileName +
				    "\",\"w\":" + ww + ",\"h\":" + hh + "}";
				    response.getWriter().print(retxt);
			    }
			    else
			    {
			    	//retxt="{\"result\":\"2\",\"msg\":\"Not proper filetype or file too large.\"}";
			    }
			   
			}
			else
			{
				response.setStatus(501);
				response.getWriter().print("{msg: \"Input file too large. Limit to 1Mb.\"}");
			}
		}catch(Exception ee) {
			ee.printStackTrace();
		}
	}
}
