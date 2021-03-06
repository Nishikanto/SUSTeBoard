package com.sust.cse.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;

	   private String filePath="c:\\temp";
	   private int maxFileSize = 1000000 * 1024;
	   private int maxMemSize = 1000000 * 1024;
	   private File file ;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(isMultipart = ServletFileUpload.isMultipartContent(request));
		
		
		
		String uploadFolder = getServletContext().getRealPath("");
		DiskFileItemFactory factory = new DiskFileItemFactory();
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(maxMemSize);
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File("c:\\temp"));

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // maximum file size to be uploaded.
	      upload.setSizeMax( maxFileSize );

	      try{ 
	      // Parse the request to get file items.
	      List fileItems = upload.parseRequest(request);
	      
	      // Process the uploaded file items
	      Iterator i = fileItems.iterator();
	      while ( i.hasNext () ) 
	      {
	         FileItem fi = (FileItem)i.next();    
	         System.out.println(fi.isFormField());
	         if(fi.isFormField()){
	        	 System.out.println("Got a form field: " + fi.getFieldName()  + " " +fi);
	         }
	         else	
	         {
	            // Get the uploaded file parameters
	        	//System.out.println(fi.getString("Command"));
	        	 
	            String fieldName = fi.getFieldName();
	            String fileName = fi.getName();
	            String contentType = fi.getContentType();
	            boolean isInMemory = fi.isInMemory();
	            
	            System.out.println(fieldName);
	            System.out.println(fileName);
	            System.out.println(contentType);
	            
	            long sizeInBytes = fi.getSize();
	            // Write the file
	            
	            if( fileName.lastIndexOf("\\") >= 0 ){
	               file = new File( uploadFolder + 
	               fileName.substring( fileName.lastIndexOf("\\"))) ;
	            }else{
	               file = new File( uploadFolder + 
	               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	            }
	            fi.write( file );
	            System.out.println(uploadFolder);
	         } 
	      }
	   }catch(Exception ex) {
	       System.out.println(ex);
	   }
	}

}
