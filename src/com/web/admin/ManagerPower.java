package com.web.admin;

import java.util.HashMap;
import java.util.LinkedList;

import com.db.SQLClient;
import com.web.admin.db.ManagerDB;

public class ManagerPower {
	private String powerCode;
	private String managerId = null;
	public ManagerPower(String powerCode,String managerId) {
		this.powerCode = powerCode;
		this.managerId = managerId;
	}
	public boolean isPower() throws Exception {
		SQLClient sqlClient = new SQLClient();
		ManagerDB managerDB = new ManagerDB(sqlClient);
		LinkedList<HashMap<String, String>> powerList = managerDB.findManagerPower(managerId);
		while(powerList.size()>0) {
			HashMap<String, String> power = powerList.remove();
			if(powerCode.equals(power.get("powerCode"))) return true;
		}
		return false;
	}

}
