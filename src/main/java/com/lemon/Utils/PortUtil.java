package com.lemon.Utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.SplitClassLoader;

public class PortUtil {
	/**
	 * 创建端口号
	 * @param startPort
	 * @param deviceNum
	 */
	private static Logger logger = Logger.getLogger(PortUtil.class);
	public static List<String> createPort(int startPort,int deviceNum){
		List<String> portList = new ArrayList<String>();
		while (portList.size()!=deviceNum) {
			checkPort(startPort+"");
			portList.add(startPort+"");
			startPort+=2;
		}
		return portList;
	}

	
	/**
	 * 检查端口是否被占用，如果被占用就杀死对应进程
	 * @param port
	 */
	private static void checkPort(String port) {
		logger.info("检查端口【"+port+"】是否被占用");
			if (isPortUsed(port)) {
				killProcessByPid(getPidByPort(port));
			}
	}


	/**
	 * 判断端口是否被占用
	 * @param port
	 * @return
	 */
	public static boolean isPortUsed(String port){
		List<String> resultList = CmdUtil.execCmd("netstat -ano |findstr " + port);
		if (resultList==null) {
			return false;
		}else if (resultList.size()>0) {
			logger.info("端口【"+port+"】被占用");
			return true;
		}else {
			return false;
		}
		
	}
	
	
	/**
	 * 根据端口号查找进程号
	 * @param port
	 * @return
	 */
	public static String getPidByPort(String port){
		List<String> resultList = CmdUtil.execCmd("netstat -ano |findstr " + port);
		if (resultList != null) {
			String [] pid =resultList.get(0).split("\\s");
			return pid[pid.length-1];
		}
		return null;
 	}
	

	/**
	 * 杀死进程
	 * @param port
	 */
	public static void killProcessByPid(String pid ){
		logger.info("杀死端口对应的进程【"+pid+"】");
		CmdUtil.execCmd("taskkill -f -pid " + pid);
		
	}
	
	
}
