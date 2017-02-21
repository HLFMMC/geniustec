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

public class ArticleAdd {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		String source = req.getParameter("source");
		String title = req.getParameter("title");
		String count = req.getParameter("count");
		String categoryId = req.getParameter("categoryId");
		if(user == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(title)) {
			responseMessage = "error-title";
		} else if(CheckUtil.isEmpty(count)) {
			responseMessage = "error-count";
		} else if(!CheckUtil.isInteger(categoryId)) {
			responseMessage = "error-categoryId";
		}
		
		SQLClient sqlClient = new SQLClient();
		ArticleDB articleDB = new ArticleDB(sqlClient);
		
		if(responseMessage == "") {
			articleDB.ArticleAdd(source, title, count, user.getUserId(), categoryId);			
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
