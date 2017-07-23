package com.web.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.IPUtil;
import com.JSONListFormat;
import com.web.admin.action.*;


/**
 * 
 * @author HM
 *
 */
@WebServlet(name = "webAction", urlPatterns = {"/webAction"}, loadOnStartup = 1)
public class WebAction extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	static Logger log = Logger.getLogger(WebAction.class.getName());
	
	@Override
	public void init() throws ServletException {
		try{
			System.out.println("===========================");
			System.out.println("web启动 ----成功");
			System.out.println("===========================");
		}catch(Exception e){
			System.out.println("===========================");
			System.out.println("web启动 ----失败");
			System.out.println("===========================");
			throw new ServletException();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		req.setCharacterEncoding("utf-8");
		
		String serverCode = req.getParameter("serverCode");
		if(serverCode == null)serverCode="";
	
  
    	System.out.println();
    	String charset =req.getCharacterEncoding();
    	Enumeration<String> enumeration = req.getParameterNames();

    	while (enumeration.hasMoreElements()) {
			String str = enumeration.nextElement();
			String parameterStr = req.getParameter(str);
			if(charset==null){
				parameterStr = new String(parameterStr.getBytes("ISO-8859-1"), "UTF-8");
			}
			if(parameterStr.length()<50){
				System.out.println("[" + str + "]" + parameterStr);
			}else{
				System.out.println("[" + str + "][数据过长，只截取前50]" + parameterStr.substring(0, 50));
			}
			log.debug("[" + str + "]" + parameterStr);
		}
    	System.out.println("[IP]" + IPUtil.getIpAddress(req));
    	

		
		try{

			/*用户*/
			if(        "managerLogin".equals(serverCode)){new ManagerLogin().doPost(req, resp);}
			else if(   "managerInfo".equals(serverCode)){new ManagerInfo().doPost(req, resp);}
			else if(   "managerLogout".equals(serverCode)){new ManagerLogout().doPost(req, resp);}
			else if(   "managerRegister".equals(serverCode)){new ManagerRegister().doPost(req, resp);}
			else if(   "managerUpdatePic".equals(serverCode)){new ManagerUpdatePic().doPost(req, resp);}
			else if(   "managerUpdateInfo".equals(serverCode)){new ManagerUpdateInfo().doPost(req, resp);}
			

			else if(   "myArticleList".equals(serverCode)){new MyArticleList().doPost(req, resp);}
			else if(   "myProblemList".equals(serverCode)){new MyProblemList().doPost(req, resp);}
			
			
			//文章相关
			else if(   "articleList".equals(serverCode)){new ArticleList().doPost(req, resp);}
			else if(   "articleAdd".equals(serverCode)){new ArticleAdd().doPost(req, resp);}
			else if(   "articleReplay".equals(serverCode)){new ArticleReplay().doPost(req, resp);}
			
			// 问题相关
			else if(   "problemList".equals(serverCode)){new ProblemList().doPost(req, resp);}
			else if(   "problemAdd".equals(serverCode)){new ProblemAdd().doPost(req, resp);}
			else if(   "problemReplay".equals(serverCode)){new ProblemReplay().doPost(req, resp);}
			
			

			else if(   "adoptionReplay".equals(serverCode)){new AdoptionReplay().doPost(req, resp);}
			else if(   "closeProblem".equals(serverCode)){new ProblemClose().doPost(req, resp);}
			
			
			//管理员
			else if(   "articleDelete".equals(serverCode)){new ArticleDelete().doPost(req, resp);}
			else if(   "problemDelete".equals(serverCode)){new ProblemDelete().doPost(req, resp);}
	 		else if(   "managerList".equals(serverCode)){new ManagerList().doPost(req, resp);}
	 		else if(   "managerList2".equals(serverCode)){new ManagerList2().doPost(req, resp);}
			else if(   "managerDisabled".equals(serverCode)){new ManagerDisabled().doPost(req, resp);}
			else if(   "managerToRole".equals(serverCode)){new ManagerToRole().doPost(req, resp);}
			else if(   "roleList".equals(serverCode)){new RoleList().doPost(req, resp);}
			else if(   "roleAdd".equals(serverCode)){new RoleAdd().doPost(req, resp);}
			else if(   "rolePowerList".equals(serverCode)){new RolePowerList().doPost(req, resp);}
			else if(   "roleToPower".equals(serverCode)){new RoleToPower().doPost(req, resp);}
			
			//分类
			else if(   "categoryList".equals(serverCode)) {new CategoryList().doPost(req, resp);}
			else if(   "disciplineList".equals(serverCode)) {new DisciplineList().doPost(req, resp);}
			else{
				JSONListFormat jFormat = new JSONListFormat();
				jFormat.setServerMsg("error - no serverCode");
				jFormat.setShowMsg("不存在该serverCode");
				PrintWriter out = resp.getWriter();
				out.println(jFormat.toString());
				out.close();
			}
			

			
		}catch(Exception e){
			logger.error(e.getMessage());
			JSONListFormat jFormat = new JSONListFormat();
			jFormat.setServerCode(serverCode);
			jFormat.setServerMsg("error--"+e.getMessage());
			jFormat.setShowMsg("系统出现异常，请联系系统管理员");
			PrintWriter out = resp.getWriter();
			out.println(jFormat.toString());
			out.close();
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
