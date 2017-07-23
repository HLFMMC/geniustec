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
	public LinkedList<HashMap<String, String>> findProblemList(String problemId, String search) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ProblemMain.ID AS problemId,  ");
		sql.append("   ProblemMain.Title AS title,  ");
		sql.append("   ProblemMain.Content AS content,  ");
		sql.append("   DATE_FORMAT(ProblemMain.CreateDate,'%Y-%m-%d %H:%i') AS createDate,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.ID AS managerId,  ");
		sql.append("   Manager.Pic AS pic  ");
		sql.append(" FROM  ");
		sql.append("   ProblemMain  ");
		sql.append(" JOIN Manager ON Manager.ID = ProblemMain.ManagerID  ");
		sql.append(" WHERE  ");
		sql.append("   ProblemMain.ID > 1  ");
		sql.append(" AND ProblemMain.Disabled = 0  ");
		if(CheckUtil.isNotEmpty(search)) {
			sql.append(" AND  ");
			sql.append("   ( ");
			sql.append("   ProblemMain.Title LIKE ?  ");
			sql.append("   OR ProblemMain.Content LIKE ? ");
			sql.append("   )  ");
			sqlClient.addParameter("%"+search+"%");
			sqlClient.addParameter("%"+search+"%");
		}
		if(CheckUtil.isInteger(problemId)) {
			sql.append(" AND ProblemMain.ID = ?  ");
			sqlClient.addParameter(problemId);
		}
		sql.append(" ORDER BY  ");
		sql.append("   ProblemMain.CreateDate DESC  ");
		
		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 查询问题图片信息
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findProblemPicList(String problemId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ProblemListPic.PicPath AS picPath  ");
		sql.append(" FROM  ");
		sql.append("   ProblemListPic  ");
		sql.append(" WHERE  ");
		sql.append("   ProblemListPic.ProblemMainID = ?  ");

		sqlClient.addParameter(problemId);
		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 查询问题回复列表
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findProblemReplayList(String problemId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ProblemListReplay.ID AS replayId, ");
		sql.append("   ProblemListReplay.Content AS content,  ");
		sql.append("   ProblemListReplay.PicPath AS picPath,  ");
		sql.append("   DATE_FORMAT(ProblemListReplay.CreateDate,'%Y-%m-%d %H:%i') AS createDate,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.Pic AS pic  ");
		sql.append(" FROM  ");
		sql.append("   ProblemListReplay  ");
		sql.append(" JOIN Manager  ");
		sql.append("   ON Manager.ID = ProblemListReplay.ManagerID  ");
		sql.append(" WHERE  ");
		sql.append("   ProblemListReplay.ProblemMainID = ?  ");

		
		sqlClient.addParameter(problemId);
		return sqlClient.execQuery(sql.toString());
	}
	
	public int addPromblemPic(String promblemId,String path) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   ProblemListPic  ");
		sql.append("   ( ");
		sql.append("   ProblemListPic.ProblemMainID,  ");
		sql.append("   ProblemListPic.PicPath  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?, ");
		sql.append("   ? ");
		sql.append("   ) ");
		sqlClient.addParameter(promblemId);
		sqlClient.addParameter(path);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	/**
	 * 我的问题列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> myProblemList(String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();

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
	public int ProblemAdd(String title,String count,String score,String managerId,String IpAddress) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   ProblemMain  ");
		sql.append("   ( ");
		sql.append("   ProblemMain.Title,  ");
		sql.append("   ProblemMain.Content,  ");
		sql.append("   ProblemMain.Score,  ");
		sql.append("   ProblemMain.ManagerID,  ");
		sql.append("   ProblemMain.CreateDate,  ");
		sql.append("   ProblemMain.IpAddress  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   NOW(),  ");
		sql.append("   ? ");
		sql.append("   ) ");

		sqlClient.addParameter(title);
		sqlClient.addParameter(count);
		sqlClient.addParameter(score);
		sqlClient.addParameter(managerId);
		sqlClient.addParameter(IpAddress);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	

	
	/**
	 * 发布问题扣除积分
	 * @param userId
	 * @param score
	 * @return
	 * @throws Exception
	 */
	public int updatePushUserScore(String managerId,String score) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   Manager  ");
		sql.append(" SET  ");
		sql.append("   Manager.Integral = Manager.Integral - ? ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID = ?  ");

		sqlClient.addParameter(score);
		sqlClient.addParameter(managerId);

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
		sql.append("   DATE_FORMAT(db_design.problemreply.CreateDate,'%Y-%m-%d %H:%i') AS createDate,  ");
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
	
	
	
	public int ProblemReplay(String problemId, String content, String path,String managerId,String IpAddress) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   ProblemListReplay  ");
		sql.append("   ( ");
		sql.append("   ProblemListReplay.ProblemMainID,  ");
		sql.append("   ProblemListReplay.Content,  ");
		sql.append("   ProblemListReplay.PicPath,  ");
		sql.append("   ProblemListReplay.ManagerID,  ");
		sql.append("   ProblemListReplay.IpAddress,  ");
		sql.append("   ProblemListReplay.CreateDate  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   ?, ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   NOW()  ");
		sql.append("   ) ");
		sqlClient.addParameter(problemId);
		sqlClient.addParameter(content);
		sqlClient.addParameter(path);
		sqlClient.addParameter(managerId);
		sqlClient.addParameter(IpAddress);
		
		return sqlClient.execUpdate(sql.toString());
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
