package com.web.admin.db;

import java.util.HashMap;
import java.util.LinkedList;

import com.CheckUtil;
import com.db.SQLClient;

public class BaseDB {

	private SQLClient sqlClient;

	public BaseDB(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
		sqlClient.setDebug(true);
	}

	/**
	 * 类别列表
	 * @param articleId
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> CategoryList(String disciplineId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.category.ID AS categoryId, ");
		sql.append("   db_design.category.`Name` AS categoryName, ");
		sql.append("   db_design.discipline.ID AS disciplineId,  ");
		sql.append("   db_design.discipline.`Name` AS disciplineName  ");
		sql.append(" FROM  ");
		sql.append("   db_design.category  ");
		sql.append(" JOIN db_design.discipline  ");
		sql.append("   ON db_design.discipline.ID = db_design.category.DisciplineID  ");
		sql.append(" WHERE  1 = 1 ");
		if(CheckUtil.isInteger(disciplineId)) {
			sql.append(" AND db_design.discipline.ID = ?  ");
			sqlClient.addParameter(disciplineId);
		}

		return sqlClient.execQuery(sql.toString());
	}

	public LinkedList<HashMap<String, String>> DisciplineList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.discipline.ID AS disciplineId,  ");
		sql.append("   db_design.discipline.`Name` AS disciplineName  ");
		sql.append(" FROM  ");
		sql.append("   db_design.discipline  ");

		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 查询最后一笔新增的Id
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> lastAddId() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT LAST_INSERT_ID() AS lastId ");
		return sqlClient.execQuery(sql.toString());
		
	}
	
	
}
