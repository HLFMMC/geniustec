package com.web.admin.db;

import java.util.HashMap;
import java.util.LinkedList;

import com.CheckUtil;
import com.db.SQLClient;

public class ManagerDB {

	private SQLClient sqlClient;

	public ManagerDB(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
		sqlClient.setDebug(true);
	}
	
	
	/**
	 * 登录
	 * @param userEmail
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> ManagerLogin(String userEmail, String password) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   Manager.ID AS managerId,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.Email AS email,  ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   Manager.Integral AS integral,  ");
		sql.append("   Manager.Personality AS personality,  ");
		sql.append("   SexBase.`Name` AS sex,  ");
		sql.append("   Manager.RoleOptsID AS roleId,  ");
		sql.append("   Manager.CreateDate AS createDate  ");
		sql.append(" FROM  ");
		sql.append("   Manager  ");
		sql.append(" JOIN SexBase ON SexBase.ID = Manager.SexBaseID  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.Disabled = 0  ");
		sql.append(" AND Manager.Email = ?  ");
		sql.append(" AND Manager.`Password` = ? ");
		sqlClient.addParameter(userEmail);
		sqlClient.addParameter(password);
		
		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 用户注册
	 * @param email
	 * @param userName
	 * @param password
	 * @param IpAddress
	 * @return
	 * @throws Exception
	 */
	public int ManagerRegister(String email,String userName,String password,String IpAddress) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   Manager  ");
		sql.append("   ( ");
		sql.append("   Manager.UserName,  ");
		sql.append("   Manager.Email,  ");
		sql.append("   Manager.`Password`,  ");
		sql.append("   Manager.IPAddress,  ");
		sql.append("   Manager.CreateDate  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   ?,  ");
		sql.append("   NOW()  ");
		sql.append("   ) ");

		sqlClient.addParameter(userName);
		sqlClient.addParameter(email);
		sqlClient.addParameter(password);
		sqlClient.addParameter(IpAddress);
		
		return sqlClient.execUpdate(sql.toString());
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
		sql.append("   Manager.ID  ");
		sql.append(" FROM  ");
		sql.append("   Manager  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.Email = ?  ");
		sqlClient.addParameter(email);

		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 修改用户头像
	 * @param path
	 * @param managerId
	 * @return
	 * @throws Exception
	 */
	public int ManagerPicUpdate(String path,String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   Manager  ");
		sql.append(" SET  ");
		sql.append("   Manager.Pic = ?  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID = ?  ");
		sqlClient.addParameter(path);
		sqlClient.addParameter(managerId);

		return sqlClient.execUpdate(sql.toString());
	}
	
	/**
	 * 用户信息修改
	 * @param userName
	 * @param sex
	 * @param personality
	 * @param managerId
	 * @return
	 * @throws Exception
	 */
	public int ManagerInfoUpdate(String userName,String sex,String personality,String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   Manager  ");
		sql.append(" SET  ");
		sql.append("   Manager.UserName = ?,  ");
		sql.append("   Manager.Personality = ?,  ");
		sql.append("   Manager.SexBaseID = ?  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID = ?  ");
		sqlClient.addParameter(userName);
		sqlClient.addParameter(personality);
		sqlClient.addParameter(sex);
		sqlClient.addParameter(managerId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	/**
	 * 删除文章
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public int removeArticle(String articleId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   ArticleMain  ");
		sql.append(" SET ");
		sql.append("   ArticleMain.Disabled = 1  ");
		sql.append(" WHERE  ");
		sql.append("   ArticleMain.ID = ?  ");
		sqlClient.addParameter(articleId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	public int removeProblem(String problemId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   ProblemMain  ");
		sql.append(" SET  ");
		sql.append("   ProblemMain.Disabled = 1  ");
		sql.append(" WHERE  ");
		sql.append("   ProblemMain.ID = ?  ");
		sqlClient.addParameter(problemId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	
	/**
	 * 查找用户信息
	 * @param managerId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findManagerInfo(String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   Manager.ID AS managerId,  ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.Email AS email,  ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   Manager.Integral AS integral,  ");
		sql.append("   Manager.Personality AS personality,  ");
		sql.append("   SexBase.`Name` AS sex,  ");
		sql.append("   SexBase.ID AS sexId,  ");
		sql.append("   Manager.RoleOptsID AS roleId,  ");
		sql.append("   DATE_FORMAT(Manager.CreateDate,'%Y-%m-%d %H:%i') AS createDate  ");
		sql.append(" FROM  ");
		sql.append("   Manager  ");
		sql.append(" JOIN SexBase ON SexBase.ID = Manager.SexBaseID  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.Disabled = 0  ");
		sql.append(" AND Manager.ID = ?  ");

		sqlClient.addParameter(managerId);
		
		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 *查看用户权限
	 */
	public LinkedList<HashMap<String, String>> findManagerPower(String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();	
		sql.append(" SELECT  ");
		sql.append("   PowerOpts.ID AS powerId, ");
		sql.append("   PowerCode AS powerCode,  ");
		sql.append("   PowerName AS powerName  ");
		sql.append(" FROM  ");
		sql.append("   Manager  ");
		sql.append(" JOIN RoleOpts  ");
		sql.append("   ON RoleOpts.ID = Manager.RoleOptsID  ");
		sql.append(" JOIN RoleListPower  ");
		sql.append("   ON RoleListPower.RoleOptsID = RoleOpts.ID  ");
		sql.append(" JOIN PowerOpts  ");
		sql.append("   ON PowerOpts.ID = RoleListPower.PowerOptsID  ");
		sql.append(" WHERE  ");
		sql.append("   PowerOpts.ID > 1 ");
		sql.append(" AND Manager.ID = ?  ");
		sqlClient.addParameter(managerId);

		return sqlClient.execQuery(sql.toString());
	}
	

	//查询用户
	public LinkedList<HashMap<String, String>> findManagerList(String managerId,String findManagerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   Manager.ID AS managerId, ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.Email AS email, ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   Manager.Integral AS integral,  ");
		sql.append("   Manager.Disabled AS status  ");
		sql.append(" FROM  ");
		sql.append("   Manager  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID >1  ");
		sql.append(" AND Manager.ID != ?  ");
		sqlClient.addParameter(managerId);
		sql.append(" AND Manager.ID NOT IN  ");
		sql.append("   ( ");
		sql.append("   SELECT  ");
		sql.append(" 		M.ID  ");
		sql.append("   FROM  ");
		sql.append("     Manager AS M ");
		sql.append("   JOIN RoleListPower  ");
		sql.append("     ON RoleListPower.RoleOptsID = M.RoleOptsID  ");
		sql.append("   WHERE  ");
		sql.append("     RoleListPower.PowerOptsID = 4  ");
		sql.append("   OR RoleListPower.PowerOptsID = 5  ");
		sql.append("   GROUP BY M.ID ");
		sql.append("   ) ");
		if(CheckUtil.isInteger(findManagerId)) {
			sql.append(" AND Manager.ID = 4  ");
			sqlClient.addParameter(findManagerId);
		}
		
		return sqlClient.execQuery(sql.toString());
	}
	
	//禁用用户
	public int disabledManager(String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   Manager  ");
		sql.append(" SET  ");
		sql.append("   Manager.Disabled = 1  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID = ?  ");
		sqlClient.addParameter(managerId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	/**
	 * 查询权限列表
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findRolePowerList(String roleId,String powerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   PowerOpts.ID AS powerId,  ");
		sql.append("   PowerOpts.PowerName AS powerName,  ");
		sql.append("   PowerOpts.PowerCode AS powerCode  ");
		sql.append(" FROM  ");
		sql.append("   PowerOpts  ");
		sql.append(" JOIN RoleListPower  ");
		sql.append("   ON RoleListPower.PowerOptsID = PowerOpts.ID  ");
		sql.append(" WHERE  ");
		sql.append("   PowerOpts.Disabled = 0  ");
		sql.append(" AND PowerOpts.ID >1  ");
		sql.append(" AND RoleListPower.RoleOptsID = ?  "); 
		sqlClient.addParameter(roleId);
		if(CheckUtil.isInteger(powerId)) {
			sql.append(" AND RoleListPower.PowerOptsID = ?  ");
			sqlClient.addParameter(powerId);
		}
		
		return sqlClient.execQuery(sql.toString());
	}
	/**
	 * 查询角色列表
	 * @return
	 * @throws Exception
	 */
	public LinkedList<HashMap<String, String>> findRoleList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   RoleOpts.ID AS roleId, ");
		sql.append("   RoleOpts.RoleName AS roleName,  ");
		sql.append("   RoleOpts.RoleCode AS roleCode  ");
		sql.append(" FROM  ");
		sql.append("   RoleOpts  ");
//		sql.append(" WHERE  ");
//		sql.append("   RoleOpts.ID >1  ");
		sql.append(" ORDER BY  ");
		sql.append("   RoleOpts.ID DESC  ");

		return sqlClient.execQuery(sql.toString());
	}
	
	/**
	 * 新增角色
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	public int addRole(String roleName) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   RoleOpts  ");
		sql.append("   ( ");
		sql.append("   RoleOpts.RoleName  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?  ");
		sql.append("   ) ");
		sqlClient.addParameter(roleName);
		
		return sqlClient.execUpdate(sql.toString());
	}
	
	
	/**
	 * 给角色新增权限
	 * @param roleId
	 * @param powerId
	 * @return
	 * @throws Exception
	 */
	public int addRolePower (String roleId,String powerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO  ");
		sql.append("   RoleListPower  ");
		sql.append("   ( ");
		sql.append("   RoleListPower.RoleOptsID,  ");
		sql.append("   RoleListPower.PowerOptsID  ");
		sql.append("   )  ");
		sql.append(" VALUES  ");
		sql.append("   ( ");
		sql.append("   ?, ");
		sql.append("   ?  ");
		sql.append("   ) ");
		sqlClient.addParameter(roleId);
		sqlClient.addParameter(powerId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	/**
	 * 移除角色权限
	 * @param roleId
	 * @param powerId
	 * @return
	 * @throws Exception
	 */
	public int removeRolePower (String roleId,String powerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   RoleListPower  ");
		sql.append(" SET  ");
		sql.append("   RoleListPower.Disabled = 1  ");
		sql.append(" WHERE  ");
		sql.append("   RoleListPower.RoleOptsID = ?  ");
		sql.append(" AND RoleListPower.PowerOptsID = ?  ");
		sqlClient.addParameter(roleId);
		sqlClient.addParameter(powerId);
		
		return sqlClient.execUpdate(sql.toString());
	}
	public int updateManagerRole(String roleId,String managerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("   Manager  ");
		sql.append(" SET  ");
		sql.append("   Manager.RoleOptsID = ?  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID = ?  ");
		sqlClient.addParameter(roleId);
		sqlClient.addParameter(managerId);
		return sqlClient.execUpdate(sql.toString());
	}
	
	public LinkedList<HashMap<String, String>> findManagerRole(String managerId)  throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append("   Manager.ID AS managerId, ");
		sql.append("   Manager.UserName AS userName,  ");
		sql.append("   Manager.Email AS email, ");
		sql.append("   Manager.Pic AS pic,  ");
		sql.append("   Manager.RoleOptsID AS roleId,  ");
		sql.append("   Manager.Integral AS integral,  ");
		sql.append("   Manager.Disabled AS status  ");
		sql.append(" FROM  ");
		sql.append("   Manager  ");
		sql.append(" WHERE  ");
		sql.append("   Manager.ID >1  ");
		sql.append(" AND Manager.ID != ?  ");
		sqlClient.addParameter(managerId);
		sql.append(" AND Manager.ID NOT IN  ");
		sql.append("   ( ");
		sql.append("   SELECT  ");
		sql.append(" 		M.ID  ");
		sql.append("   FROM  ");
		sql.append("     Manager AS M ");
		sql.append("   JOIN RoleListPower  ");
		sql.append("     ON RoleListPower.RoleOptsID = M.RoleOptsID  ");
		sql.append("   WHERE  ");
		sql.append("     RoleListPower.PowerOptsID = 5  ");
		sql.append("   GROUP BY M.ID ");
		sql.append("   ) ");
		
		return sqlClient.execQuery(sql.toString());
	}
	
}
