package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.db.BaseDB;

public class CategoryList {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String disciplineId = req.getParameter("disciplineId");
		
		
		SQLClient sqlClient = new SQLClient();
		BaseDB baseDB = new BaseDB(sqlClient);
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = baseDB.CategoryList(disciplineId);
			while(data.size()>0) {
				HashMap<String, String> map = data.remove();
				jsonFormat.addMap(map);
			}
			
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} 
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
