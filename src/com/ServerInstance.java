package com;


import java.io.File;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerInstance {
	
	static ServerInstance instance ;
	
	
	
	static {
		instance = new ServerInstance() ;
		
	}
	private String    fileURL    = "";
	private String    serverURL  = "";


	private File      realPathFile    = null;

	private File      filePath   = null;


	private  ServerInstance() {
		
		ApplicationContext context =  new ClassPathXmlApplicationContext("beans-config-system.xml");
		
		Properties dataProperties  = (Properties)context.getBean("serverConfig");
			
	  	   fileURL = dataProperties.getProperty("fileURL");
		 serverURL = dataProperties.getProperty("serverURL");
		 
		System.out.println("  fileURL  "+fileURL);
		System.out.println("serverURL  "+serverURL);
	
		
	}
	

	
	
	
	public static  ServerInstance getInstance(){
		
		return instance;
		
	}





	public String getFileURL() {
		return fileURL;
	}





	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}





	public String getServerURL() {
		return serverURL;
	}





	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}





	public File getRealPathFile() {
		return realPathFile;
	}





	public void setRealPathFile(File webPathFile) {
		this.realPathFile = webPathFile;
	}





	public File getFilePath() {
		return filePath;
	}





	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}



}
