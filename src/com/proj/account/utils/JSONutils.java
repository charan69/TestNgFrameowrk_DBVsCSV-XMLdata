package com.mdsol.ctms.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import io.restassured.path.json.JsonPath;

public class JSONutils {
	public static JsonPath jsonpath;

	public static String[] getJsonArrayValue(String jsonString, String jsonKey) {
		try {
			jsonpath = new JsonPath(jsonString);
			String jss = jsonpath.getString(jsonKey);
			return new String[] { jss };
		} catch (Exception e) {
			throw new UncheckedIOException((IOException) e);
		}
	}

	public static String getJsonvalue(String jsonString, String jsonKey) {
		try {
			jsonpath = new JsonPath(jsonString);
//			String jss = jsonpath.getString(jsonKey);
//			return jss;
			return jsonpath.getString(jsonKey);
		} catch (Exception e) {
			throw new UncheckedIOException((IOException) e);
		}
	}



	public static List<String> getlistofJsonvalue(List<String> jsonstringSED, String jsonKey) {
		List<String> jsonlistList = new ArrayList<String>();
		int count = jsonstringSED.size();
		for (int i = 4; i < count; i++) {
		 String jsonString = jsonstringSED.get(i);
		 jsonpath = new JsonPath(jsonString);
		 String jss = jsonpath.getString(jsonKey);
		 jsonlistList.add(jss);
		 if (i == 7 ) {
			 break;
		}
		
	   }
		return jsonlistList;
	 		
}
}
	 		
	 	
	

