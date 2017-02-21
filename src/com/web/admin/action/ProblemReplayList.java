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
import com.web.admin.db.ProblemDB;

public class ProblemReplayList {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String replayId = req.getParameter("replayId");
		String problemId = req.getParameter("problemId");
		if(!CheckUtil.isInteger(problemId)) {
			responseMessage  = "error-problemId";
		}
		
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = problemDB.findProblemReplayList(problemId, replayId);
			while(data.size()>0) {
				HashMap<String, String> map = data.remove();
				jsonFormat.addMap(map);
			}
			
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-problemId")) jsonFormat.setShowMsg("错误的问题ID");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
