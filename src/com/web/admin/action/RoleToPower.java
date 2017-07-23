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
import com.web.admin.ManagerPower;
import com.web.admin.db.ManagerDB;

public class RoleToPower {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String roleId = req.getParameter("roleId");
		String powerId = req.getParameter("powerId");
		
		HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		
		if(manager == null) {
			responseMessage  = "error-login";
		} else if(CheckUtil.isEmpty(roleId)) {
			responseMessage  = "error-roleId";
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
			LinkedList<HashMap<String, String>> data = managerDB.findRolePowerList(roleId,powerId);
			if(data.size()>0) {
				managerDB.removeRolePower(roleId, powerId);
			} else {
				managerDB.addRolePower(roleId, powerId);
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
