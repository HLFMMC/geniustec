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
import com.web.admin.db.BaseDB;
import com.web.admin.db.ManagerDB;
import com.web.admin.db.ProblemDB;

public class ProblemAdd {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		Manager manager = (Manager)session.getAttribute("manager");
		
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String score = req.getParameter("score");
		String picList = req.getParameter("picList");
		if(manager == null) {
			responseMessage = "error-login";
		} else if(CheckUtil.isEmpty(title)) {
			responseMessage = "error-title";
		} else if(CheckUtil.isEmpty(content)) {
			responseMessage = "error-content";
		} 
		if(!CheckUtil.isInteger(score)) {
			score = "0";
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
		ProblemDB problemDB = new ProblemDB(sqlClient);
		BaseDB baseDB = new BaseDB(sqlClient);
		ManagerDB managerDB = new ManagerDB(sqlClient);
		String removeScore = -(Integer.parseInt(score)+20)+"";
		if(responseMessage == "") {
			if(manager !=null){
				LinkedList<HashMap<String, String>> managerInfo = managerDB.findManagerInfo(manager.getManagerId());
				if(Integer.parseInt(managerInfo.get(0).get("integral"))<Integer.parseInt(removeScore)){
					responseMessage = "error-integral";
				}
			}
		}
		if(responseMessage == "") {
			try {
				sqlClient.setAutoCommit(false);
				problemDB.ProblemAdd(title, content, score,manager.getManagerId(),IPUtil.getIpAddress(req)); // 发表问题
				LinkedList<HashMap<String, String>> data = baseDB.lastAddId();
				String problemId  = data.get(0).get("lastId");
				while(base64List.size()>0) {
					String base64 = base64List.remove();
					String imageURL = "";
					if(CheckUtil.isNotEmpty(base64)) {
						String image = System.currentTimeMillis() + base64List.size()+ ".jpg";
						imageURL = "upload/problem/"+image;
						
						File imageFile =WebUtil.findWebPathFile(req, imageURL);
						Base64Util.base64ToFile(base64, imageFile);
					} 
					if(CheckUtil.isNotEmpty(imageURL)) 
						problemDB.addPromblemPic(problemId,imageURL);
				}
				problemDB.updatePushUserScore(manager.getManagerId(), removeScore);
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
