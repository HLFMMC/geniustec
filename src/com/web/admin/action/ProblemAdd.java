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

public class ProblemAdd {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String score = req.getParameter("score");
		if(user == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(title)) {
			responseMessage = "error-title";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} 
		if(!CheckUtil.isInteger(score)) {
			score = "0";
		}
		String removeScore = -(Integer.parseInt(score)+20)+"";
		if(user !=null){
			if(Integer.parseInt(user.getIntegral())<Integer.parseInt(removeScore)){
				responseMessage = "error-integral";
			}
		}
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		ManagerDB managerDB = new ManagerDB(sqlClient);
		
		if(responseMessage == "") {
			try {
				sqlClient.setAutoCommit(false);
				problemDB.ProblemAdd(title, content, user.getUserId(), score);
				problemDB.updatePushUserScore(user.getUserId(), removeScore);
				LinkedList<HashMap<String, String>> data = managerDB.findUser(user.getUserId());
				user.setIntegral(data.get(0).get("integral"));
				session.setAttribute("user", user);
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
			if(responseMessage.equals("error-title")) jsonFormat.setShowMsg("标题不能为空");
			if(responseMessage.equals("error-content")) jsonFormat.setShowMsg("内容不能为空");
			if(responseMessage.equals("error-integral")) jsonFormat.setShowMsg("积分不足");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
