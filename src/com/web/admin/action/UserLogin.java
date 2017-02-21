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

public class UserLogin {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String userEmail = req.getParameter("email");
		String password = req.getParameter("password");
		System.out.println(userEmail);
		if(CheckUtil.isEmpty(userEmail)) {
			responseMessage = "error-email"; 
		} else if(!CheckUtil.isPassword(password)) {
			responseMessage = "error-password";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = managerDB.UserLogin(userEmail, password);
			if(data.size()>0) {
				HashMap<String, String> map = data.get(0);
				String userId = map.get("userId");
				String nikeName = map.get("nikeName");
				String email = map.get("email");
				String userSex = map.get("userSex");
				String signature = map.get("signature");
				String integral = map.get("integral");
				String managerId = map.get("manageId");
				
				User user = new User();
				user.setUserId(userId);
				user.setNikeName(nikeName);
				user.setEmail(email);
				user.setUserSex(userSex);
				user.setSignature(signature);
				user.setIntegral(integral);
				user.setManagerId(managerId);
				
			   HttpSession httpSession = req.getSession();
			   httpSession.setAttribute("user", user);
			} else {
				responseMessage = "error-account";
			}
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-email")) jsonFormat.setShowMsg("账号错误");
			if(responseMessage.equals("error-password"));jsonFormat.setShowMsg("密码错误");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
