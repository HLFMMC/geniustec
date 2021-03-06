package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CheckUtil;
import com.IPUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.db.ManagerDB;

public class ManagerRegister {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String email = req.getParameter("email");
		String userName = req.getParameter("userName");;
		String password = req.getParameter("password");
		if(CheckUtil.isEmpty(email)) {
			responseMessage = "error-email"; 
		} else if(CheckUtil.isEmpty(userName)){
			responseMessage = "error-userName";
		}else if(!CheckUtil.isPassword(password)) {
			responseMessage = "error-password";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = managerDB.findEmalRepeat(email);
			if(data.size()>0) {
				responseMessage = "error-repeat";
			} else {
				managerDB.ManagerRegister(email, userName, password, IPUtil.getIpAddress(req));
			}
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-email")) jsonFormat.setShowMsg("账号错误");
			if(responseMessage.equals("error-password")) jsonFormat.setShowMsg("密码错误");
			if(responseMessage.equals("error-repeat")) jsonFormat.setShowMsg("邮箱已被注册");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
