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
import com.web.admin.FormatDataUtil;
import com.web.admin.Manager;
import com.web.admin.db.ManagerDB;

public class ManagerInfo {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		HttpSession httpSession = req.getSession();
		Manager manager = (Manager)httpSession.getAttribute("manager");
		
		if(manager == null) {
			responseMessage = "error-login";
		}
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		if(responseMessage == "") {
			LinkedList<HashMap<String, String>> data = managerDB.findManagerInfo(manager.getManagerId());
			while (data.size()>0) {
				HashMap<String, String> map = data.remove();
				LinkedList<HashMap<String, String>> powerList = managerDB.findManagerPower(manager.getManagerId());
				HashMap<String, LinkedList<HashMap<String, String>>> maps = new HashMap<>();
				maps.put("powerList", powerList);
				jsonFormat = FormatDataUtil.groupFormat(map, maps, jsonFormat);
			}
			
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-login")) jsonFormat.setShowMsg("用户无登录");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
