package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CheckUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.FormatDataUtil;
import com.web.admin.db.ArticleDB;

public class ArticleList {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String search = req.getParameter("search");
		String articleId = req.getParameter("articleId");
		
		
		SQLClient sqlClient = new SQLClient();
		ArticleDB articleDB = new ArticleDB(sqlClient);
		
		if(responseMessage == "") {
			if(CheckUtil.isInteger(articleId)) {
				articleDB.ArticleUpdate(articleId);
			}
			LinkedList<HashMap<String, String>> data = articleDB.findArticleList(articleId, search);
			while(data.size()>0) {
				HashMap<String, String> map = data.remove();
				String articleGetId = map.get("articleId");
				LinkedList<HashMap<String, String>> picList = articleDB.findArticlePicList(articleGetId);
				LinkedList<HashMap<String, String>> replayList = articleDB.findArticleReplayList(articleGetId);
				
				HashMap<String, LinkedList<HashMap<String, String>>> maps = new HashMap<>();
				maps.put("picList", picList);
				maps.put("replayList", replayList);
				jsonFormat = FormatDataUtil.groupFormat(map, maps, jsonFormat);
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
