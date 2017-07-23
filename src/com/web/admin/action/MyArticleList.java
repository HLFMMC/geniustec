package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.db.ArticleDB;

public class MyArticleList {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession httpSession = req.getSession();
		Manager manager =(Manager)httpSession.getAttribute("manager");
		if(manager == null) {
			responseMessage = "error-login";
		}
		
		SQLClient sqlClient = new SQLClient();
		ArticleDB articleDB = new ArticleDB(sqlClient);
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = articleDB.myArticleList(manager.getManagerId());
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
