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
import com.web.admin.db.ProblemDB;
/**
 * 采纳问题
 * @author HM
 *
 */
public class AdoptionReplay {
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws Exception {
		String responseMessage = "";
		
		JSONListFormat  jsonFormat = WebUtil.createJSONListFormat(req, false);
		
		String replayId = req.getParameter("replayId");
		String problemId = req.getParameter("problemId");
		HttpSession session = req.getSession();
		Manager manager = (Manager) session.getAttribute("manager");
		
		if(manager == null) {
			responseMessage  = "error-login";
		} else if(!CheckUtil.isInteger(problemId)) {
			responseMessage  = "error-problemId";
		} else if(!CheckUtil.isInteger(replayId)) {
			responseMessage  = "error-replayId";
		} 
		
		
		SQLClient sqlClient = new SQLClient();
		ProblemDB problemDB = new ProblemDB(sqlClient);
		
		if(responseMessage == "") {

			LinkedList<HashMap<String, String>> data = problemDB.findProblemList(problemId,null);
			LinkedList<HashMap<String, String>> data2 = problemDB.findProblemReplayList(problemId,replayId);

			if(data.size()>0 && data2.size()>0) {
				if(data.get(0).get("userId").equals(manager.getManagerId())) {
					if(data.get(0).get("isClose").equals("0")) {
						if(data2.get(0).get("userId").equals(manager.getManagerId())){
							responseMessage = "error-oneself";
						} else {
							try {
								sqlClient.setAutoCommit(false);
								problemDB.ProblemAdoption(replayId, problemId);
								problemDB.UserAddSorce(replayId, problemId);
								sqlClient.commit();
							} catch (Exception e) {
								sqlClient.rollBack();
								throw e;
							}
						}
					} else {
						responseMessage  = "error-close";
					}
				} else {
					responseMessage  = "error-authority";
				}
			} else {
				responseMessage  = "error-problemId";
			}
			
		}
		
		if(responseMessage == "") {
			responseMessage = "success";
		} else {
			if(responseMessage.equals("error-login")) jsonFormat.setShowMsg("用户无登录");
			if(responseMessage.equals("error-problemId")) jsonFormat.setShowMsg("错误的问题ID");
			if(responseMessage.equals("error-replayId")) jsonFormat.setShowMsg("错误的回复ID");
			if(responseMessage.equals("error-authority")) jsonFormat.setShowMsg("无权限");
			if(responseMessage.equals("error-close")) jsonFormat.setShowMsg("问题已关闭");
			if(responseMessage.equals("error-oneself")) jsonFormat.setShowMsg("不能采纳自己的回答");
		}
		
		jsonFormat.setServerMsg(responseMessage);
		PrintWriter out = resp.getWriter();
		out.println(jsonFormat.toString());
		out.close();
	}

}
