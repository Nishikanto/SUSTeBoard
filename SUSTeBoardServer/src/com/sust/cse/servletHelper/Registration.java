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

/**
 * Servlet implementation class RegistrationServlet
 */
//@WebServlet("/RegistrationServlet")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrintWriter out;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Registration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("call");
		DatabaseConnection dbConnection = new DatabaseConnection();
		System.out.println(request.getParameter("name"));
		out = response.getWriter();
		
		if (request.getParameterNames().hasMoreElements()) {
			insertRecord(request, dbConnection.getDbConnection());
		} else {
			System.out.println("Servlet Started First Time");
		}
	}

	private void insertRecord(HttpServletRequest request, Connection connection) {

		System.out.println("===== Received Record =====");
		
		PreparedStatement stmt;
		try {
			
			stmt = connection.prepareStatement("SELECT * FROM `users_table` WHERE `_email` = '"+request.getParameter("email")+"'");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				System.out.println("All ready registered");
				out.println("0");
			} else{
				stmt = connection
						.prepareStatement("insert into users_table(_name,_email,_pasword,_position) values(?,?,?,?)");
				stmt.setString(1, request.getParameter("name"));
				stmt.setString(2, request.getParameter("email"));
				stmt.setString(3, request.getParameter("password"));
				stmt.setString(4, "student");
				int i = stmt.executeUpdate();
				if (i != 0) {
					System.out.println("data inserted !!!");
					out.println("2");
				} else {
					System.out.println("data failed to insert");
					out.println("1");
				}
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}
}
