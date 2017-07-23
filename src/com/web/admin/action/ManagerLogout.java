package com.web.admin.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.JSONListFormat;
import com.web.WebUtil;

public class ManagerLogout {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		HttpSession session = req.getSession();
		
		if(responseMessage == "") {
			session.removeAttribute("manager");
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
