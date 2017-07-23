package com.web.admin.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Base64Util;
import com.CheckUtil;
import com.IPUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.db.ArticleDB;
import com.web.admin.db.BaseDB;

public class ArticleAdd {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String categoryId = req.getParameter("categoryId");
		String picList = req.getParameter("picList");
		if(manager == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(title)) {
			responseMessage = "error-title";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} else if(!CheckUtil.isInteger(categoryId)) {
			responseMessage = "error-categoryId";
		}
		
		LinkedList<String> base64List = new LinkedList<>();
		if(CheckUtil.isNotEmpty(picList)) {
			JSONArray jsonArray = new JSONArray(picList);
			for(int i = 0;i<jsonArray.length();i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String base64 = jsonObject.optString("base64");
				if(CheckUtil.isNotEmpty(base64)) {
					base64List.add(base64);
				}
			}
			
		}
		
		SQLClient sqlClient = new SQLClient();
		ArticleDB articleDB = new ArticleDB(sqlClient);
		BaseDB baseDB = new BaseDB(sqlClient);
		
		if(responseMessage == "") {
			try {
				sqlClient.setAutoCommit(false);
				
				articleDB.ArticleAdd(title, content, categoryId, IPUtil.getIpAddress(req),manager.getManagerId());
				LinkedList<HashMap<String, String>> data = baseDB.lastAddId();
				
				String articleId  = data.get(0).get("lastId");;
				
				while(base64List.size()>0) {
					String base64 = base64List.remove();
					String imageURL = "";
					if(CheckUtil.isNotEmpty(base64)) {
						String image = System.currentTimeMillis() + base64List.size()+ ".jpg";
						imageURL = "upload/article/"+image;
						
						File imageFile =WebUtil.findWebPathFile(req, imageURL);
						Base64Util.base64ToFile(base64, imageFile);
					} 
					if(CheckUtil.isNotEmpty(imageURL)) 
						articleDB.ArticlePicAdd(imageURL, articleId);
				}
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
			if(responseMessage.equals("error-count")) jsonFormat.setShowMsg("内容不能为空");
			if(responseMessage.equals("error-categoryId")) jsonFormat.setShowMsg("分类Id错误");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
