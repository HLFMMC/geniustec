package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.FormatDataUtil;
import com.web.admin.db.ProblemDB;

public class ProblemList {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String search = req.getParameter("search");
		String problemId = req.getParameter("problemId");
		
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = problemDB.findProblemList(problemId, search);
			while(data.size()>0) {
				HashMap<String, String> map = data.remove();
				String problemGetId = map.get("problemId");
				LinkedList<HashMap<String, String>> picList = problemDB.findProblemPicList(problemGetId);
				LinkedList<HashMap<String, String>> replayList = problemDB.findProblemReplayList(problemGetId);
				
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
