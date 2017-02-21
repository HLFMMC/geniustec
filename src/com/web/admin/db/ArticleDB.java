package com.web.admin.db;

import java.util.HashMap;
import java.util.LinkedList;

import com.CheckUtil;
import com.db.SQLClient;

public class ArticleDB {

	private SQLClient sqlClient;

	public ArticleDB(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
		sqlClient.setDebug(true);
	}

	/**
	 * 文章列表
	 * @param articleId
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findArticleList(String articleId, String text) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.articlebase.ID AS articleId,  ");
		sql.append("   db_design.articlebase.Title AS title, ");
		sql.append("   db_design.articlebase.Count AS count, ");
		sql.append("   db_design.articlebase.CreateDate AS createDate, ");
		sql.append("   db_design.articlebase.Source AS source, ");
		sql.append("   db_design.userbase.ID AS userId, ");
		sql.append("   db_design.userbase.NikeName AS userNikeName, ");
		sql.append("   db_design.userbase.Signature AS signature, ");
		sql.append("   db_design.userbase.Pic AS pic, ");
		sql.append("   db_design.discipline.`Name` AS disciplineName, ");
		sql.append("   db_design.category.`Name` AS categoryName  ");
		sql.append(" FROM  ");
		sql.append("   db_design.articlebase  ");
		sql.append(" JOIN db_design.userbase  ");
		sql.append("   ON db_design.userbase.ID = db_design.articlebase.UserBaseID  ");
		sql.append(" JOIN db_design.category  ");
		sql.append("   ON db_design.category.ID = db_design.articlebase.CategoryID  ");
		sql.append(" JOIN db_design.discipline  ");
		sql.append("   ON db_design.discipline.ID = db_design.category.DisciplineID  ");
		sql.append(" WHERE  1 = 1 ");
		if(CheckUtil.isInteger(articleId)) {
			sql.append(" AND db_design.articlebase.ID = ?  ");
			sqlClient.addParameter(articleId);
		}
		if(CheckUtil.isNotEmpty(text)) {
			sql.append(" AND  ");
			sql.append(" ( ");
			sql.append("   db_design.articlebase.Title Like ?  ");
			sql.append("   OR db_design.articlebase.Count LIKE ?  ");
			sql.append("   OR db_design.category.`Name` LIKE ?  ");
			sql.append("   OR db_design.discipline.`Name` LIKE ?  ");
			sql.append(" ) ");
			sqlClient.addParameter("%"+text+"%");
			sqlClient.addParameter("%"+text+"%");
			sqlClient.addParameter("%"+text+"%");
			sqlClient.addParameter("%"+text+"%");
		}
		sql.append(" ORDER BY db_design.articlebase.CreateDate DESC  ");
		
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
	public int ArticleAdd(String source,String title,String count ,String userId,String categoryId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   db_design.articlebase  ");
		sql.append(" ( ");
		sql.append("   db_design.articlebase.Source, ");
		sql.append("   db_design.articlebase.Title, ");
		sql.append("   db_design.articlebase.Count, ");
		sql.append("   db_design.articlebase.UserBaseID, ");
		sql.append("   db_design.articlebase.CategoryID, ");
		sql.append("   db_design.articlebase.CreateDate ");
		sql.append(" )VALUES ");
		sql.append(" ( ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?,  ");
		sql.append("   NOW()  ");
		sql.append(" ) ");
		sqlClient.addParameter(source);
		sqlClient.addParameter(title);
		sqlClient.addParameter(count);
		sqlClient.addParameter(userId);
		sqlClient.addParameter(categoryId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	/**
	 * 删除文章
	 * @param articleId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int ArticleDelete(String articleId,String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM  ");
		sql.append("   db_design.articlebase  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.articlebase.ID = ?  ");
		sql.append(" AND db_design.articlebase.UserBaseID = ? ");

		sqlClient.addParameter(articleId);
		sqlClient.addParameter(userId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	public LinkedList<HashMap<String, String>> findArticleReplayList(String articleId,String replayId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.articlereply.ID  AS replyId, ");
		sql.append("   db_design.userbase.ID AS userId,  ");
		sql.append("   db_design.userbase.NikeName AS nikeName, ");
		sql.append("   db_design.userbase.Signature AS signature,  ");
		sql.append("   db_design.articlereply.Count AS count,  ");
		sql.append("   db_design.articlereply.CreateDate AS createDate  ");
		sql.append(" FROM  ");
		sql.append("   db_design.articlereply  ");
		sql.append(" JOIN db_design.userbase  ");
		sql.append("   ON db_design.userbase.ID = db_design.articlereply.UserBaseID  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.articlereply.ArticleBaseID = ?  ");
		sqlClient.addParameter(articleId);
		if(CheckUtil.isInteger(replayId)) {
			sql.append(" AND db_design.articlereply.ID = ?  ");
			sqlClient.addParameter(replayId);
		}
		return sqlClient.execQuery(sql.toString());
	}
	/**
	 * 新增回复
	 * @param articleId
	 * @param content
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int ArticleReplayAdd(String articleId, String content, String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   db_design.articlereply  ");
		sql.append("   ( ");
		sql.append("   db_design.articlereply.ArticleBaseID,  ");
		sql.append("   db_design.articlereply.UserBaseID, ");
		sql.append("   db_design.articlereply.Count, ");
		sql.append("   db_design.articlereply.CreateDate  ");
		sql.append("   ) ");
		sql.append(" VALUES  ");
		sql.append(" ( ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   NOW()  ");
		sql.append(" ) ");
		sqlClient.addParameter(articleId);
		sqlClient.addParameter(userId);
		sqlClient.addParameter(content);
		
		return sqlClient.execUpdate(sql.toString());
		
	}
}
