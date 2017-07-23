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
import com.web.admin.Manager;
import com.web.admin.db.ManagerDB;

public class ManagerLogin {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String userEmail = req.getParameter("email");
		String password = req.getParameter("password");

		if(CheckUtil.isEmpty(userEmail)) {
			responseMessage = "error-email"; 
		} else if(!CheckUtil.isPassword(password)) {
			responseMessage = "error-password";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = managerDB.ManagerLogin(userEmail, password);
			if(data.size()>0) {
				HashMap<String, String> map = data.get(0);
				String managerId = map.get("managerId");
				String userName = map.get("userName");
				String email = map.get("email");
				String pic = map.get("pic");
				String integral = map.get("integral");
				String personality = map.get("personality");
				String sex = map.get("sex");
				String roleId = map.get("roleId");
				String createDate = map.get("createDate");
				
				Manager manager = new Manager();
				manager.setManagerId(managerId);
				manager.setUserName(userName);
				manager.setEmail(email);
				manager.setPic(pic);
				manager.setIntegral(integral);
				manager.setPersonality(personality);
				manager.setSex(sex);
				manager.setRoleId(roleId);
				manager.setCreateDate(createDate);
				
			   HttpSession httpSession = req.getSession();
			   httpSession.setAttribute("manager", manager);
			} else {
				responseMessage = "error-account";
			}
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-email")) jsonFormat.setShowMsg("账号错误");
			if(responseMessage.equals("error-password"));jsonFormat.setShowMsg("密码错误");
			if(responseMessage.equals("error-account"));jsonFormat.setShowMsg("账号或密码错误");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
