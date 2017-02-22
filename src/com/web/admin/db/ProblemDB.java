package com.web.admin.db;

import java.util.HashMap;
import java.util.LinkedList;

import com.CheckUtil;
import com.db.SQLClient;

public class ProblemDB {

	private SQLClient sqlClient;

	public ProblemDB(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
		sqlClient.setDebug(true);
	}

	/**
	 * 问题列表
	 * @param articleId
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findProblemList(String problemId, String text) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.problembase.ID AS problemId,  ");
		sql.append("   db_design.problembase.Title AS title, ");
		sql.append("   db_design.problembase.Detailed AS detailed,  ");
		sql.append("   db_design.problembase.CreateDate AS createDate, ");
		sql.append("   db_design.problembase.IsClose AS isClose, ");
		sql.append("   db_design.userbase.ID AS userId, ");
		sql.append("   db_design.userbase.NikeName AS userNikeName, ");
		sql.append("   db_design.userbase.Signature AS signature, ");
		sql.append("   db_design.problemreply.Count AS bestReplyCount, ");
		sql.append("   db_design.problemreply.ID AS bestReplyId  ");
		sql.append(" FROM  ");
		sql.append("   db_design.problembase  ");
		sql.append(" JOIN db_design.userbase  ");
		sql.append("   ON db_design.userbase.ID = db_design.problembase.UserBaseID  ");
		sql.append(" JOIN db_design.problemreply  ");
		sql.append("   ON db_design.problemreply.ID = db_design.problembase.BestReplyID  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.problembase.ID >1  ");
		if(CheckUtil.isInteger(problemId)) {
			sql.append(" AND db_design.problembase.ID = ?  ");
			sqlClient.addParameter(problemId);
		}
		if(CheckUtil.isNotEmpty(text)) {
			sql.append(" AND  ");
			sql.append(" ( ");
			sql.append("   db_design.problembase.Title LIKE ?  ");
			sql.append("   OR  db_design.problembase.Detailed LIKE ? ");
			sql.append(" ) ");
			sqlClient.addParameter("%"+text+"%");
			sqlClient.addParameter("%"+text+"%");
		}

		sql.append(" ORDER BY db_design.problembase.CreateDate DESC  ");
		
		return sqlClient.execQuery(sql.toString());
	}
	
	
	/**
	 * 问题新增
	 * @param email
	 * @param nikeName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public int ProblemAdd(String title,String count ,String userId,String score) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   db_design.problembase  ");
		sql.append("   ( ");
		sql.append("   db_design.problembase.Title,  ");
		sql.append("   db_design.problembase.Detailed, ");
		sql.append("   db_design.problembase.UserBaseID,  ");
		sql.append("   db_design.problembase.Score,  ");
		sql.append("   db_design.problembase.CreateDate  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?,  ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?,  ");
		sql.append("   NOW() ");
		sql.append("   ) ");
		sqlClient.addParameter(title);
		sqlClient.addParameter(count);
		sqlClient.addParameter(userId);
		sqlClient.addParameter(score);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	
	/**
	 * 发布问题扣除积分
	 * @param userId
	 * @param score
	 * @return
	 * @throws Exception
	 */
	public int updatePushUserScore(String userId,String score) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   db_design.userbase  ");
		sql.append(" SET  ");
		sql.append("   db_design.userbase.Integral = Integral  + ?  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.userbase.ID = ?  ");
		sqlClient.addParameter(score);
		sqlClient.addParameter(userId);

		return sqlClient.execUpdate(sql.toString());
	}
	/**
	 * 采纳问题
	 * @param articleId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int ProblemAdoption(String replayId,String problemId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   db_design.problembase  ");
		sql.append(" SET  ");
		sql.append("   db_design.problembase.IsClose = 1, ");
		sql.append("   db_design.problembase.BestReplyID = ?  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.problembase.ID = ?  ");


		sqlClient.addParameter(replayId);
		sqlClient.addParameter(problemId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	// 采纳问题 最佳用户添加积分
	public int UserAddSorce(String replayId,String problemId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   db_design.userbase  ");
		sql.append(" SET  ");
		sql.append("   db_design.userbase.Integral = Integral+  ");
		sql.append("   ( ");
		sql.append("   SELECT  ");
		sql.append("     db_design.problembase.Score  ");
		sql.append("   FROM  ");
		sql.append("     db_design.problembase  ");
		sql.append("   WHERE  ");
		sql.append("     db_design.problembase.ID = ?  ");
		sql.append("   )  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.userbase.ID =  ");
		sql.append("   ( ");
		sql.append("   SELECT  ");
		sql.append("     db_design.problemreply.UserBaseID  ");
		sql.append("   FROM  ");
		sql.append("     db_design.problemreply  ");
		sql.append("   WHERE  ");
		sql.append("     db_design.problemreply.ID = ?  ");
		sql.append("   ) ");
		sqlClient.addParameter(problemId);
		sqlClient.addParameter(replayId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	/**
	 * 关闭问题
	 * @param replayId
	 * @param problemId
	 * @return
	 * @throws Exception
	 */
	public int ProblemClose(String problemId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   db_design.problembase  ");
		sql.append(" SET  ");
		sql.append("   db_design.problembase.IsClose = 2 ");
		sql.append(" WHERE  ");
		sql.append("   db_design.problembase.ID = ?  ");
		sqlClient.addParameter(problemId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	/**
	 * 回复列表
	 * @param articleId
	 * @param replayId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findProblemReplayList(String problemId,String replayId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   db_design.problemreply.ID AS problemReplyId,  ");
		sql.append("   db_design.problemreply.Count AS count,  ");
		sql.append("   db_design.problembase.ID AS problemId,  ");
		sql.append("   db_design.problemreply.CreateDate AS createDate,  ");
		sql.append("   db_design.userbase.ID AS userId,  ");
		sql.append("   db_design.userbase.NikeName AS nikeName,  ");
		sql.append("   db_design.problembase.IsClose AS isClose  ");
		sql.append(" FROM  ");
		sql.append("   db_design.problemreply  ");
		sql.append(" JOIN db_design.userbase  ");
		sql.append("   ON db_design.userbase.ID = db_design.problemreply.UserBaseID  ");
		sql.append(" JOIN db_design.problembase  ");
		sql.append("   ON db_design.problembase.ID = db_design.problemreply.ProblemBaseID  ");
		sql.append(" WHERE  ");
		sql.append("   db_design.problembase.ID = ? ");
		sqlClient.addParameter(problemId);
		if(CheckUtil.isInteger(replayId)) {
			sql.append(" AND db_design.problemreply.ID = ?");
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
	public int ProblemReplayAdd(String problemId, String content, String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   db_design.problemreply  ");
		sql.append("   ( ");
		sql.append("   db_design.problemreply.Count, ");
		sql.append("   db_design.problemreply.ProblemBaseID, ");
		sql.append("   db_design.problemreply.UserBaseID, ");
		sql.append("   db_design.problemreply.CreateDate  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   NOW()  ");
		sql.append("   ) ");

		sqlClient.addParameter(content);
		sqlClient.addParameter(problemId);
		sqlClient.addParameter(userId);
		
		return sqlClient.execUpdate(sql.toString());
		
	}
}
