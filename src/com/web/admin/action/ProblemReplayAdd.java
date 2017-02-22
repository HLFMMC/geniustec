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
import com.web.admin.User;
import com.web.admin.db.ManagerDB;
import com.web.admin.db.ProblemDB;

public class ProblemReplayAdd {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		String problemId = req.getParameter("problemId");
		String content = req.getParameter("content");
		if(user == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(problemId)) {
			responseMessage = "error-problemId";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} 
	
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		ManagerDB managerDB = new ManagerDB(sqlClient);
		
		if(responseMessage == "") {
			try {
				sqlClient.setAutoCommit(false);
				LinkedList<HashMap<String, String>> data1 = problemDB.findProblemList(problemId, null);
				if(data1.size()>0) {
					if(data1.get(0).get("isClose").equals("0")){
						problemDB.ProblemReplayAdd(problemId, content, user.getUserId());
						if(!data1.get(0).get("userId").equals(user.getUserId())) {
							problemDB.updatePushUserScore(user.getUserId(), "5");
							LinkedList<HashMap<String, String>> data = managerDB.findUser(user.getUserId());
							user.setIntegral(data.get(0).get("integral"));
							session.setAttribute("user", user);
						}
						sqlClient.commit();
					} else if(data1.get(0).get("isClose").equals("1")) {
						
					} else {
						
					}
				} else {
					responseMessage = "error-problemId";
				}
			} catch (Exception e) {
				sqlClient.rollBack();
				throw e;
			}
			
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-login")) jsonFormat.setShowMsg("用户无登陆");
			if(responseMessage.equals("error-problemId")) jsonFormat.setShowMsg("错误的问题ID");
			if(responseMessage.equals("error-content")) jsonFormat.setShowMsg("内容不能为空");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
