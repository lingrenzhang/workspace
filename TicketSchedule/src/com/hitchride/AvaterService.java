package com.hitchride;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.Math;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.FileUploadIOException;
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
	 * return 200 if everything is ok
	 * return 400 if plain text is uploaded or file type doesn't belong to image
	 * return 501 if file size limitation exceeds
	 * return 500 if any other exception raised
	 * TODO: Now we rely on the client(browser) passed file extension name to check whether the uploaded file is a image
	 *       Should use more robust way(e.g. check upload file content head chars) to avoid client hacker
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String rootDir = request.getSession().getServletContext().getRealPath("");
		
		//This part only for development environment. Can fix code in runtime environment if required.
		Properties props=System.getProperties(); 
        String osname =  props.getProperty("os.name");
        int last_sec_slash;
        if (osname.toUpperCase().contains("WIN"))
        {
        	int last_slash = rootDir.lastIndexOf("\\");
    		last_sec_slash = rootDir.lastIndexOf("\\", last_slash - 1);
        }
        else
        {
        	int last_slash = rootDir.lastIndexOf("/"); // use "\\" for windows os
    		last_sec_slash = rootDir.lastIndexOf("/", last_slash - 1);  // use "\\" for windows os
        }
		String realDir = rootDir.substring(0, last_sec_slash); // Tomcat Root

		String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath();

		try {
			String filePath = "/pics/tmp"; // put the customer uploaded image into temp folder first.
			String realPath = realDir + filePath;

			//Create path if not exist
			File dir = new File(realPath);
			if(!dir.isDirectory())
			    dir.mkdir();

			if(ServletFileUpload.isMultipartContent(request)){ // otherwise it is plain text content
			    DiskFileItemFactory dff = new DiskFileItemFactory();
			    dff.setRepository(dir);
			    dff.setSizeThreshold(1024000);

			    ServletFileUpload sfu = new ServletFileUpload(dff);
			    sfu.setFileSizeMax(1024000); // upload file size limitation is 1MB
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
			                fileName = fis.getName().toLowerCase();
			                String fileType = this.getServletContext().getMimeType(fileName);
			                if(fileType == null || !fileType.startsWith("image")){
			                	response.setStatus(400);
			                	response.getWriter().print("{\"msg\": \"Illegal file format.\"}");
			                	return;
			                }
							realFileName = new Date().getTime() + "_" + Math.round(Math.random() * 100000000) + fileName.substring(fileName.lastIndexOf("."));
			                url = realPath + "/" + realFileName;
	
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
			        }catch(FileUploadIOException e){
			        	if(e.getCause().getClass().getSimpleName() == "FileSizeLimitExceededException"){
			        		response.setStatus(501);
			        		response.getWriter().print("{msg: \"upload file is too large. Limit to 1Mb.\"}");
			        	}else{
			        		System.err.println(e.getMessage());
			        		e.printStackTrace();
			        		response.setStatus(500);
			        		response.getWriter().print("{msg: \"Please upload a img file with size limitation 1MB, or leave it blank}");
			        	}
			        	return;
			        }catch(Exception e){
			        	System.err.println(e.getMessage());
			        	e.printStackTrace();
			        	response.setStatus(500);
			        	response.getWriter().print("{msg: \"Please upload a img file with size limitation 1MB, or leave it blank}");
			        	return;
			        }
			    }

			    response.setStatus(200);
			    String retxt="";
			    if (!realFileName.equalsIgnoreCase(""))
			    {
				    msg = basePath + filePath + "/" + realFileName;
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
				response.setStatus(400);
				response.getWriter().print("{msg: \"Please upload a image file.\"}");
				return;
			}
		}catch(Exception ee) {
			System.err.println(ee.getMessage());
			ee.printStackTrace();
			response.setStatus(500);
			response.getWriter().print("{msg: \"Please upload a img file with size limitation 1MB, or leave it blank}");
		}
	}
}
