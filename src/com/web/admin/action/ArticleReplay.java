package com.web.admin.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.CheckUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.User;
import com.web.admin.db.ArticleDB;

public class ArticleReplay {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		String articleId = req.getParameter("articleId");
		String content = req.getParameter("content");
		if(user == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(articleId)) {
			responseMessage = "error-articleId";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ArticleDB articleDB = new ArticleDB(sqlClient);
		
		if(responseMessage == "") {
			articleDB.ArticleReplayAdd(articleId, content, user.getUserId());
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
