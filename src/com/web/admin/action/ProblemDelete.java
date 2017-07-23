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
import com.web.admin.ManagerPower;
import com.web.admin.db.ManagerDB;
import com.web.admin.db.ProblemDB;

public class ProblemDelete {
	
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
		
		if(responseMessage == "") {
			ManagerPower managerPower = new ManagerPower("1002", manager.getManagerId());
			if(!managerPower.isPower()) {
				responseMessage = "error-power";
			}
		}
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		ProblemDB problemDB = new ProblemDB(sqlClient);
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = problemDB.findProblemList(problemId, null);
			if(data.size()>0) {
				managerDB.removeProblem(problemId);
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
