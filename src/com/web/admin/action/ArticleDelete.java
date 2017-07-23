package com.web.admin.action;

import java.io.PrintWriter;

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

public class ArticleDelete {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		
		String articleId = req.getParameter("articleId");
		if(manager == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(articleId)) {
			responseMessage = "error-articleId";
		} 
		
		if(responseMessage == "") {
			ManagerPower managerPower = new ManagerPower("1001", manager.getManagerId());
			if(!managerPower.isPower()) {
				responseMessage = "error-power";
			}
		}
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		
		if(responseMessage == "") {
			managerDB.removeArticle(articleId);
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-login")) jsonFormat.setShowMsg("用户无登陆");
			if(responseMessage.equals("error-title")) jsonFormat.setShowMsg("标题不能为空");
			if(responseMessage.equals("error-count")) jsonFormat.setShowMsg("内容不能为空");
			if(responseMessage.equals("error-categoryId")) jsonFormat.setShowMsg("分类Id错误");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
