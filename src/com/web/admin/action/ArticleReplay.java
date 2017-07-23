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
import com.web.admin.db.ArticleDB;

public class ArticleReplay {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		
		String articleId = req.getParameter("articleId");
		String content = req.getParameter("content");
		String base64 = req.getParameter("base64");
		if(manager == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(articleId)) {
			responseMessage = "error-articleId";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ArticleDB articleDB = new ArticleDB(sqlClient);
		
		if(responseMessage == "") {
			
			String imageURL = "";
			if(CheckUtil.isNotEmpty(base64)) {
				String image = System.currentTimeMillis() + ".jpg";
				imageURL = "upload/article/"+image;
				
				File imageFile =WebUtil.findWebPathFile(req, imageURL);
				Base64Util.base64ToFile(base64, imageFile);
			} 
			articleDB.ArticleReplayAdd(articleId, content, manager.getManagerId(),imageURL,IPUtil.getIpAddress(req));
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-login")) jsonFormat.setShowMsg("用户无登陆");
			if(responseMessage.equals("error-title")) jsonFormat.setShowMsg("标题不能为空");
			if(responseMessage.equals("error-content")) jsonFormat.setShowMsg("内容不能为空");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
