package com.web.admin.db;

import java.util.HashMap;
import java.util.LinkedList;

import com.db.SQLClient;

public class ManagerDB {

	private SQLClient sqlClient;

	public ManagerDB(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
		sqlClient.setDebug(true);
	}

	public LinkedList<HashMap<String, String>> UserLogin(String userEmail, String password) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("   db_design.userbase.ID AS userId, ");
		sql.append("   db_design.userbase.NikeName AS nikeName, ");
		sql.append("   db_design.userbase.Email AS email, ");
		sql.append("   db_design.userbase.Sex AS userSex, ");
		sql.append("   db_design.userbase.Integral AS integral, ");
		sql.append("   db_design.userbase.Signature AS signature, ");
		sql.append("   db_design.userbase.ManageID AS manageId  ");
		sql.append(" FROM  ");
		sql.append("   db_design.userbase  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.userbase.Email = ?  ");
		sql.append(" AND db_design.userbase.`PassWord` = ?");
		sqlClient.addParameter(userEmail);
		sqlClient.addParameter(password);
		
		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 查询email 重复
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findEmalRepeat(String email) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.userbase.ID  ");
		sql.append(" FROM  ");
		sql.append("   db_design.userbase  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.userbase.Email = ? ");
		sqlClient.addParameter(email);

		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 用户注册
	 * @param email
	 * @param nikeName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public int UserRegister(String email,String nikeName,String password) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   db_design.userbase  ");
		sql.append("   ( ");
		sql.append("   db_design.userbase.NikeName,  ");
		sql.append("   db_design.userbase.Email, ");
		sql.append("   db_design.userbase.`PassWord`  ");
		sql.append("   )  ");
		sql.append(" VALUES ");
		sql.append(" ( ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ? ");
		sql.append(" ) ");
		sqlClient.addParameter(nikeName);
		sqlClient.addParameter(email);
		sqlClient.addParameter(password);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	public LinkedList<HashMap<String, String>> findUser(String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("   db_design.userbase.ID AS userId, ");
		sql.append("   db_design.userbase.NikeName AS nikeName, ");
		sql.append("   db_design.userbase.Email AS email, ");
		sql.append("   db_design.userbase.Sex AS userSex, ");
		sql.append("   db_design.userbase.Integral AS integral, ");
		sql.append("   db_design.userbase.Signature AS signature, ");
		sql.append("   db_design.userbase.ManageID AS manageId  ");
		sql.append(" FROM  ");
		sql.append("   db_design.userbase  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.userbase.ID = ?   ");
		sqlClient.addParameter(userId);
		
		return sqlClient.execQuery(sql.toString());
	}
}
