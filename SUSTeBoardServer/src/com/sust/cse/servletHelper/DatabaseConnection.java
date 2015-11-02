package com.sust.cse.servletHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	String url = "jdbc:mysql://localhost/sust_notice_board_db";
	String user = "root";
	String password = "";
	Connection con = null;

	public Connection getDbConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver found");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found:" + e);
		}

		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Database Connection Successful");
		} catch (SQLException e) {
			System.out.println("Database Connection Failed");
		}

		return con;

	}

	public void CloseConnection() {
		try {
			if (con != null) {

				con.close();
				con = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
