package com.web.admin.action;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Base64Util;
import com.CheckUtil;
import com.IPUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.db.ProblemDB;

public class ProblemReplay {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		Manager manager = (Manager)session.getAttribute("manager");
		
		String problemId = req.getParameter("problemId");
		String content = req.getParameter("content");
		String base64 = req.getParameter("base64");
		if(manager == null) {
			responseMessage = "error-login";
		} else if(!CheckUtil.isInteger(problemId)) {
			responseMessage = "error-problemId";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} 
	
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		
		if(responseMessage == "") {
			try {
				sqlClient.setAutoCommit(false);
				
				String imageURL = "";
				if(CheckUtil.isNotEmpty(base64)) {
					String image = System.currentTimeMillis() + ".jpg";
					imageURL = "upload/problem/"+image;
					
					File imageFile =WebUtil.findWebPathFile(req, imageURL);
					Base64Util.base64ToFile(base64, imageFile);
				} 
				problemDB.ProblemReplay(problemId, content, imageURL,manager.getManagerId(),IPUtil.getIpAddress(req));
				problemDB.updatePushUserScore(manager.getManagerId(), "5");
				sqlClient.commit();
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
