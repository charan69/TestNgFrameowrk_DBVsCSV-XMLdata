package com.mdsol.ctms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HelperMethods {

	FileReader f;
	static String userDir = System.getProperty("user.dir");

	// The below method will read all the lines from the CSV and return as a string
	// array
	public List<String[]> readCSVAndReturnResults(String csvpath) throws IOException {
		// reading from CSV
		int k = 0;
		String line = "";
		List<String[]> myList = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(csvpath)));
			while ((line = br.readLine()) != null) {
				if (k > 0) {
					String[] tempArray = line.split(",");

					myList.add(tempArray);
				}
				k++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}

		// End of the code

		return myList;
	}

	// The below method will read the CSV and replace the values as per the mapping
	// config file
	public String[] getCSVMappingDetails(String[] csvDetails, String propPath) throws IOException {
		String[] newCSVValues = csvDetails;
		f = new FileReader(propPath);
		Properties p = new Properties();
		p.load(f);
		for (int i = 0; i < newCSVValues.length; i++) {
			try {
				if (p.getProperty(newCSVValues[i]).length() > 0) {

					newCSVValues[i] = p.getProperty(newCSVValues[i]);
				}

			} catch (Exception e) {
				System.out.println("no value found in mapping document for = " + newCSVValues[i]);
			}
		}

		return newCSVValues;
	}

	// the below method will read the XML as input and return as array List of
	// strings
	public static String readSQLQueryAndReturnResultAsList(String sql, String columnName)
			throws IOException, ClassNotFoundException, SQLException {
		FileReader fr;
		fr = new FileReader(userDir + "\\src\\config\\dbconfig.properties");
		Properties p = new Properties();
		p.load(fr);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = p.getProperty("url");
		String username = p.getProperty("username");
		String pwd = p.getProperty("password");
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String myStr = "{\"data\":[";
		try {
			con = DriverManager.getConnection(url, username, pwd);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				String tempString;
				tempString = rs.getString(columnName);
				myStr = myStr.concat(",");

				if (tempString.contains("null")) {
					tempString = tempString.replace("null", "\"null\"");
				}
				myStr = myStr.concat(tempString);

			}
			myStr = myStr.concat("]}").replace("[,", "[");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			con.close();

		}
		return myStr;
	}

	// The below method will read XML path and return required attributes as an
	// array
	public static String[] readXmlDataByXpathStudy(String xmlPath, String propFilePath)
			throws ParserConfigurationException, XPathExpressionException, SAXException, IOException {
		FileReader fReader = null;
		String[] tempArray = null;
		try {

			fReader = new FileReader(propFilePath);
			Properties prop = new Properties();
			prop.load(fReader);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(xmlPath));
			XPath xpath = XPathFactory.newInstance().newXPath();
			String[] elementPaths = prop.getProperty("requiredAttributes").split(",");
			tempArray = new String[elementPaths.length];
			for (int i = 0; i < elementPaths.length; i++) {
				XPathExpression exp = xpath.compile(prop.getProperty(elementPaths[i]));
				tempArray[i] = (String) exp.evaluate(doc, XPathConstants.STRING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fReader.close();
		}

		return tempArray;

	}

	// The below method will read XML path and return required attributes as an
	// array
	public static List<String[]> readXmlDataForMultipleObjects(String xmlPath, String propFilePath)
			throws ParserConfigurationException, XPathExpressionException, SAXException, IOException {
		FileReader fReader = null;
		String[] tempArray = null;
		List<String[]> myList = new ArrayList<String[]>();

		try {

			fReader = new FileReader(propFilePath);
			Properties prop = new Properties();
			prop.load(fReader);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(xmlPath));
			XPath xpath = XPathFactory.newInstance().newXPath();
			String[] nonRepetitiveElementsPaths = prop.getProperty("nonrepetitiveAttributes").split(",");
			String[] repetitiveElementPaths = prop.getProperty("repetitiveAttributes").split(",");
			XPathExpression exp = xpath.compile(prop.getProperty(repetitiveElementPaths[0]));
			NodeList nList = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			int nodeLength = nList.getLength();
			int totalAttributes = repetitiveElementPaths.length + nonRepetitiveElementsPaths.length;
			tempArray = new String[totalAttributes];

			for (int j = 0; j < nodeLength; j++) {
				String line = "";
				for (int k = 0; k < nonRepetitiveElementsPaths.length; k++) {
					exp = xpath.compile(prop.getProperty(nonRepetitiveElementsPaths[k]));
					line = line + (String) exp.evaluate(doc, XPathConstants.STRING) + ",";

				}
				for (int i = 0; i < repetitiveElementPaths.length; i++) {
					String attributePath = prop.getProperty("sampleXpath");
					attributePath = attributePath.replace("{xpath}", prop.getProperty(repetitiveElementPaths[i]));
					attributePath = attributePath.replace("{index}", String.valueOf(j + 1));
					exp = xpath.compile(attributePath);
					line = line + (String) exp.evaluate(doc, XPathConstants.STRING) + ",";

				}
				tempArray = line.trim().split(",");
				myList.add(tempArray);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fReader.close();
		}

		return myList;

	}
	
}
