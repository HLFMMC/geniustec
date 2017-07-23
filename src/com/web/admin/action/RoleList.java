package com.web.admin.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.ManagerPower;
import com.web.admin.db.ManagerDB;

public class RoleList {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession httpSession = req.getSession();
		Manager manager = (Manager)httpSession.getAttribute("manager");
		if(manager == null) {
			responseMessage = "error-login";
		}
		
		if(responseMessage == "") {
			ManagerPower managerPower = new ManagerPower("1004", manager.getManagerId());
			if(!managerPower.isPower()) {
				responseMessage = "error-power";
			}
		}
		
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		
		
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = managerDB.findRoleList();
			while (data.size()>0) {
				HashMap<String, String> map = data.remove();
				jsonFormat.addMap(map);
				
			}
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} 
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
