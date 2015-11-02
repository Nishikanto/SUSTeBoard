package com.sust.cse.servletHelper;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * Servlet implementation class UploadServlet
 */
//@WebServlet("/UploadServlet")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String databaseFilePath;
	private PrintWriter out;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Upload() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @param uploadFolder 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request,
			HttpServletResponse response, String uploadFolder) throws ServletException, IOException {
		out = response.getWriter();
		
		String fileName = request.getParameter("fileName");
		System.out.println(fileName);
		
		String pictureName = request.getParameter("picturePath");
		System.out.println(pictureName);
		
		String department = request.getParameter("department");
		System.out.println(department);
		
		String postDate = request.getParameter("postDate");
		System.out.println(postDate);
		
		String notice = request.getParameter("notice");
		System.out.println(notice);
		
		String details = request.getParameter("details");
		System.out.println(details);
		
		
	
		//databaseFilePath = "http://192.168.43.147:8080/SUSTeBoardServer/DownloadServlet?fileName=" + attachFileName;
	    //System.out.println(databaseFilePath)
		
		
		//String databaseImagePath = "http://192.168.43.147:8080/SUSTeBoardServer/ImageServlet?title=" + imageName.split("\\.")[0];
        //System.out.println(databaseImagePath);
        
      
        /*DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection con = databaseConnection.getDbConnection();
		
		String sql = "insert into notice_table (_notice, _details, _created_at, _image_link, _pdf_link, _department) values ('" + notice + "','" + details + "','" + postDate + "','" + databaseImagePath + "','" + databaseFilePath + "','" + department + "')";
		Statement st = null;
		try {
			st = con.createStatement();
			int i = st.executeUpdate(sql);

			if (i != 0) {
				System.out.println("data inserted !!!");
				out.println("1");
			} else {
				System.out.println("data failed to insert");
				out.println("0");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
		
		
		
		
		
	}
}
