package com.web.admin.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.CheckUtil;
import com.JSONListFormat;
import com.db.SQLClient;
import com.web.WebUtil;
import com.web.admin.Manager;
import com.web.admin.db.ManagerDB;

/**
 * 修改用户信息
 * @author HM
 *
 */
public class ManagerUpdateInfo {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		Manager manager = (Manager)session.getAttribute("manager");

		String userName = req.getParameter("userName");;
		String sex = req.getParameter("sex");
		String personality = req.getParameter("personality");
		if(manager == null) {
			responseMessage = "error-login"; 
		} else if(CheckUtil.isEmpty(userName)) {
			responseMessage = "error-userName"; 
		} else if(CheckUtil.isEmpty(sex)){
			responseMessage = "error-sex";
		}
		if(CheckUtil.isEmpty(personality)) {
			personality = "";
		} 
		
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		if(responseMessage == "") {
			managerDB.ManagerInfoUpdate(userName, sex, personality, manager.getManagerId());
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-login")) jsonFormat.setShowMsg("用户无登录");
			if(responseMessage.equals("error-userName")) jsonFormat.setShowMsg("用户名不能为空");
			if(responseMessage.equals("error-sex")) jsonFormat.setShowMsg("性别不能为空");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
