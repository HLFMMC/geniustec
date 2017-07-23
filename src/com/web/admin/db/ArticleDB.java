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
	public LinkedList<HashMap<String, String>> findArticleList(String articleId, String search) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ArticleMain.ID AS articleId,  ");
		sql.append("   ArticleMain.Title AS title,  ");
		sql.append("   ArticleMain.Content AS content,  ");
		sql.append("   ArticleMain.BrownTimes AS brownTimes,  ");
		sql.append("   Category.`Name` AS categoryName,  ");
		sql.append("   Discipline.`Name`  AS disciplineName,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.ID AS managerId,  ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   DATE_FORMAT(ArticleMain.CreateDate,'%Y-%m-%d %H:%i') AS createDate  ");
		sql.append(" FROM  ");
		sql.append("   ArticleMain  ");
		sql.append(" JOIN Manager ON Manager.ID = ArticleMain.ManagerID  ");
		sql.append(" JOIN Category ON Category.ID = ArticleMain.CategroyID  ");
		sql.append(" JOIN Discipline ON Discipline.ID = Category.DisciplineID  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleMain.ID > 1 ");
		sql.append(" AND ArticleMain.Disabled = 0  ");
		if(CheckUtil.isNotEmpty(search)) {
			sql.append(" AND  ");
			sql.append("   ( ");
			sql.append("   ArticleMain.Title LIKE ? ");
			sql.append("   OR ArticleMain.Content LIKE ?  ");
			sql.append("   OR Category.`Name` LIKE ?  ");
			sql.append("   OR Discipline.`Name` LIKE ?  ");
			sql.append("   )  ");
			sqlClient.addParameter("%"+search+"%");
			sqlClient.addParameter("%"+search+"%");
			sqlClient.addParameter("%"+search+"%");
			sqlClient.addParameter("%"+search+"%");
		}
		if(CheckUtil.isInteger(articleId)) {
			sql.append(" AND ArticleMain.ID = ?  ");
			sqlClient.addParameter(articleId);
		}
		
		sql.append(" ORDER BY  ");
		sql.append("   ArticleMain.CreateDate DESC  ");

		
		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 文章图片列表
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findArticlePicList(String articleId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ArticleListPic.PicPath AS picPath  ");
		sql.append(" FROM  ");
		sql.append("   ArticleListPic  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleListPic.Disabled = 0  ");
		sql.append(" AND ArticleListPic.ArticleMainID = ? ");
		sqlClient.addParameter(articleId);
		return sqlClient.execQuery(sql.toString());
	}
	
	
	/**
	 * 文章回复列表
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findArticleReplayList(String articleId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ArticleListReplay.ID AS replayId,  ");
		sql.append("   ArticleListReplay.Content AS content,  ");
		sql.append("   DATE_FORMAT(ArticleListReplay.CreateDate,'%Y-%m-%d %H:%i') AS createDate,  ");
		sql.append("   ArticleListReplay.PicPath AS picPath,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   Manager.ID AS managerId,  ");
		sql.append("   Manager.Personality AS personality  ");
		sql.append(" FROM  ");
		sql.append("   ArticleListReplay  ");
		sql.append(" JOIN Manager ON Manager.ID = ArticleListReplay.ManagerID  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleListReplay.ID > 1  ");
		sql.append(" AND ArticleListReplay.ArticleMainID = ?  ");
		sqlClient.addParameter(articleId);
		sql.append(" ORDER BY  ");
		sql.append("   ArticleListReplay.CreateDate ASC  ");
		
		return sqlClient.execQuery(sql.toString());
	}
	
	
	/**
	 * 新增文章内容
	 * @param title
	 * @param content
	 * @param categoryId
	 * @param managerId
	 * @return
	 * @throws Exception
	 */
	public int ArticleAdd(String title,String content,String categoryId ,String IpAddress,String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   ArticleMain  ");
		sql.append("   ( ");
		sql.append("   ArticleMain.Title,  ");
		sql.append("   ArticleMain.Content,   ");
		sql.append("   ArticleMain.CategroyID,  ");
		sql.append("   ArticleMain.IpAddress,  ");
		sql.append("   ArticleMain.ManagerID,  ");
		sql.append("   ArticleMain.CreateDate  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   NOW()  ");
		sql.append("   ) ");
		sqlClient.addParameter(title);
		sqlClient.addParameter(content);
		sqlClient.addParameter(categoryId);
		sqlClient.addParameter(IpAddress);
		sqlClient.addParameter(managerId);

		return sqlClient.execUpdate(sql.toString());
	}
	/**
	 * 新增文章图片
	 * @param path
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public int ArticlePicAdd (String path,String articleId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   ArticleListPic  ");
		sql.append("   ( ");
		sql.append("   ArticleListPic.PicPath,  ");
		sql.append("   ArticleListPic.ArticleMainID  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?, ");
		sql.append("   ? ");
		sql.append("   ) ");
		sqlClient.addParameter(path);
		sqlClient.addParameter(articleId);
		
		return sqlClient.execUpdate(sql.toString());
		
	}

	/**
	 * 修改帖子浏览数
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public int ArticleUpdate(String articleId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   ArticleMain  ");
		sql.append(" SET  ");
		sql.append("   ArticleMain.BrownTimes = BrownTimes + 1  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleMain.ID = ?  ");
		sqlClient.addParameter(articleId);
		
		return sqlClient.execUpdate(sql.toString());
	}

	/**
	 * 我的文章列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> myArticleList(String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   ArticleMain.ID AS articleId,  ");
		sql.append("   ArticleMain.Title AS title,  ");
		sql.append("   ArticleMain.Content AS content,  ");
		sql.append("   ArticleMain.BrownTimes AS brownTimes,  ");
		sql.append("   Category.`Name` AS categoryName,  ");
		sql.append("   Discipline.`Name`  AS disciplineName,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.ID AS managerId,  ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   ArticleMain.CreateDate AS createDate  ");
		sql.append(" FROM  ");
		sql.append("   ArticleMain  ");
		sql.append(" JOIN Manager ON Manager.ID = ArticleMain.ManagerID  ");
		sql.append(" JOIN Category ON Category.ID = ArticleMain.CategroyID  ");
		sql.append(" JOIN Discipline ON Discipline.ID = Category.DisciplineID  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleMain.ID > 1 ");
		sql.append(" AND ArticleMain.Disabled = 0  ");
		sql.append(" AND Manager.ID = ?  ");
		sqlClient.addParameter(managerId);

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
	public int ArticleReplayAdd(String articleId, String content, String managerId,String picPath,String IpAddress) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   ArticleListReplay  ");
		sql.append("   ( ");
		sql.append("   ArticleListReplay.ArticleMainID,  ");
		sql.append("   ArticleListReplay.ManagerID,  ");
		sql.append("   ArticleListReplay.Content,  ");
		sql.append("   ArticleListReplay.PicPath,  ");
		sql.append("   ArticleListReplay.IpAddress,  ");
		sql.append("   ArticleListReplay.CreateDate  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   (  ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   ?, ");
		sql.append("   NOW()  ");
		sql.append("   ) ");
		sqlClient.addParameter(articleId);
		sqlClient.addParameter(managerId);
		sqlClient.addParameter(content);
		sqlClient.addParameter(picPath);
		sqlClient.addParameter(IpAddress);

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
		sql.append(" UPDATE  ");
		sql.append("   ArticleMain  ");
		sql.append(" SET  ");
		sql.append("   ArticleMain.Disabled = 1  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleMain.ID = ?  ");
		sqlClient.addParameter(articleId);
		
		return sqlClient.execUpdate(sql.toString());
	}
}
