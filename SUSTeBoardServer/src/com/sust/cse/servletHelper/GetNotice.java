package com.sust.cse.servletHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetNoticeServlet
 */
//@WebServlet("/GetNoticeServlet")
public class GetNotice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String department;
	private Connection con;
	private PreparedStatement statement;
	private ResultSet resultSet;
	private NoticeInfo noticeInfo;

	private String joinDepartment;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetNotice() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String sql;
		department = request.getParameter("department");
		System.out.println(department);

		List<String> separateDepartments = Arrays.asList(department.split(","));

		if (separateDepartments.size() == 1) {
			joinDepartment = "('" + separateDepartments.get(0) + "')";
		} else {
			for (int i = 0; i < separateDepartments.size(); i++) {
				if (i == 0) {
					joinDepartment = "('" + separateDepartments.get(i) + "',";
				} else if (i == separateDepartments.size() - 1) {
					joinDepartment +=  "'" + separateDepartments.get(i) + "')";
				} else
					joinDepartment += "'" + separateDepartments.get(i) + "',";
			}

		}

		System.out.println(joinDepartment);

		if (!department.equals("all")) {
			sql = "SELECT * FROM `notice_table` WHERE `_department` IN "
					+ joinDepartment + "";
		} else
			sql = "SELECT * FROM `notice_table`";

		DatabaseConnection databaseConnection = new DatabaseConnection();
		con = databaseConnection.getDbConnection();

		try {
			statement = con.prepareStatement(sql);
			resultSet = statement.executeQuery();

			PrintWriter out = response.getWriter();
			List<NoticeInfo> list = new ArrayList<NoticeInfo>();

			while (resultSet.next()) {
				noticeInfo = new NoticeInfo();
				noticeInfo.setID(resultSet.getInt("_id"));
				noticeInfo.setNotice(resultSet.getString("_notice").trim());
				noticeInfo.setDetails(resultSet.getString("_details").trim());
				noticeInfo.setCreatedAt(resultSet.getString("_created_at")
						.trim());
				noticeInfo.setImage(resultSet.getString("_image_link").trim());
				noticeInfo.setPdf(resultSet.getString("_pdf_link").trim());
				noticeInfo.setDepartment(resultSet.getString("_department")
						.trim());
				list.add(noticeInfo);
			}

			JsonArray jarray = new JsonArray();
			for (int i = 0; i < list.size(); i++) {
				NoticeInfo noticeInfo = list.get(i);
				Gson gson = new Gson();
				JsonObject myObj = (JsonObject) gson.toJsonTree(noticeInfo);
				jarray.add(myObj);
			}
			out.print(jarray);

			resultSet.close();
			statement.close();
			out.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
