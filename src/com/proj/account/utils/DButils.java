package com.mdsol.ctms.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DButils {

	static Connection connection = null;
	static Statement statement;
	static ResultSet resultSet;
	static String columndata = null;
	static List<String> listofColumn = new ArrayList<String>();
	static String[] multipleArray = null;
	public static java.sql.PreparedStatement preparedStatement;

	public static Connection dbConnect(String dbURL, String dbusername, String dbpassword)
			throws SQLException, ClassNotFoundException {

		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(dbURL, dbusername, dbpassword);
		return connection;
	}

	public static Connection dbDisconnect() throws SQLException {

		connection.close();
		return connection;
	}

	public static ResultSet selectRecord(String selectquery)
			throws SQLException{

		statement = connection.createStatement();
		resultSet = statement.executeQuery(selectquery);
		return resultSet;
	}
	
	public static Statement deleteRecord(String deletequery)
			throws SQLException{

		statement = connection.createStatement();
		statement.executeUpdate(deletequery);
		return statement;
	}
	
	public static PreparedStatement insertColumnstoRecord(String insertintoquery)
			throws SQLException{

		preparedStatement = connection.prepareStatement(insertintoquery);
		
		return preparedStatement;
	}
	
	public static PreparedStatement insertValuestoColumnsofRecord(int column, String value)
			throws SQLException {

		preparedStatement.setString(column, value);
		
		return preparedStatement;
	}
	
	public static PreparedStatement insertJsondatatoColumnsofRecord(int column, String jsonstring)
			throws SQLException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject jo = (JSONObject) parser.parse(jsonstring);
		ObjectMapper objMap = new ObjectMapper();
		preparedStatement.setObject(column, objMap.writerWithDefaultPrettyPrinter().writeValueAsString(jo));
		
		return preparedStatement;
	}
	
	public static PreparedStatement afterInsertupdatetoCreateRecord()
			throws SQLException {

		preparedStatement.executeUpdate();
		
		return preparedStatement;
	}

	public static String getDataOfColumnName(String selectquery, String columnname)
			throws SQLException{

		statement = connection.createStatement();
		resultSet = statement.executeQuery(selectquery);
		while (resultSet.next()) {
			columndata = resultSet.getString(columnname);
		}
		return columndata;
	}

	public static List<String> getListOfDataOfColumnName(String selectquery, String columnname)
			throws SQLException{
		statement = connection.createStatement();
		resultSet = statement.executeQuery(selectquery);
		while (resultSet.next()) {
			columndata = resultSet.getString(columnname);
			listofColumn.add(columndata);
		}
		return listofColumn;

	}
	
}
