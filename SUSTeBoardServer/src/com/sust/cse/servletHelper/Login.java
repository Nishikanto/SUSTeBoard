package com.sust.cse.servletHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



/**
 * Servlet implementation class LoginServlet
 */
//@WebServlet("/LoginServlet")
public class Login {
	private static final long serialVersionUID = 1L;
	
	private String email=null;
	private String pass=null;
	private String sql;
	private PreparedStatement statement = null;
	private ResultSet resultSet;
	private DatabaseConnection databaseConnection;
	private Connection con;
	private PrintWriter out;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
    	//super();
    	databaseConnection = new DatabaseConnection();
		con = databaseConnection.getDbConnection();
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		out = response.getWriter();
		email = request.getParameter("email");
		pass = request.getParameter("pass");
		
		sql = "SELECT * FROM `users_table` WHERE `_email` = '"+ email 
				+"' AND `_pasword` = '"+ pass +"'";
		
		 try {
			statement = con.prepareStatement(sql);
			resultSet = statement.executeQuery();
			
			JsonObject user_auth = new JsonObject();
			
			if(resultSet.next()){
				String position = resultSet.getString("_position");
				user_auth.addProperty("user", "valid");
				user_auth.addProperty("position", position);
				System.out.println("Found");
			} else {
				user_auth.addProperty("user", "invalid");
				System.out.println("Not Found");
			}
				
			
			Gson gson = new Gson();
			String auth = gson.toJson(user_auth);
			out.println(auth);
			
			resultSet.close();
			statement.close();
			out.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println(email + " " + pass);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
