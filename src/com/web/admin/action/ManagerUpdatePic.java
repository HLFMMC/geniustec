package com.web.admin.action;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.Base64Util;
import com.CheckUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.db.ManagerDB;

public class ManagerUpdatePic {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)throws Exception {
		
		String responseMessage = "";
				
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		
		String base64 = req.getParameter("base64");

		HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		if(manager == null){
			responseMessage="error-login";
		} else if(CheckUtil.isEmpty(base64)) {
			responseMessage = "error-base64";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		
		if(responseMessage==""){
			String imageURL = "";
			if(CheckUtil.isNotEmpty(base64)) {
				String image = System.currentTimeMillis() + ".jpg";
				imageURL = "upload/manager/"+image;
				
				File imageFile =WebUtil.findWebPathFile(req, imageURL);
				Base64Util.base64ToFile(base64, imageFile);
				String url = imageFile.getPath();
				System.out.println(url);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("path", url);
				jsonFormat.addJSONObject(jsonObject);
			} 
			managerDB.ManagerPicUpdate(imageURL, manager.getManagerId());
		} 
				
		
		if(responseMessage=="") responseMessage="success";
		jsonFormat.setServerMsg(responseMessage);
	    PrintWriter out = resp.getWriter();
        out.println(jsonFormat.toString());
        out.close();     
	}
	
}
