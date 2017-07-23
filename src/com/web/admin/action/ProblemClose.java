package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.CheckUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.db.ProblemDB;

public class ProblemClose {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String problemId = req.getParameter("problemId");HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		
		if(manager == null) {
			responseMessage  = "error-login";
		} else if(!CheckUtil.isInteger(problemId)) {
			responseMessage  = "error-problemId";
		}
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = problemDB.findProblemList(problemId, null);
			if(data.size()>0) {
				if(data.get(0).get("userId").equals(manager.getManagerId())) {
					problemDB.ProblemClose(problemId);
				} else {
					responseMessage  = "error-authority";
				}
			} else {
				responseMessage  = "error-problemId";
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
