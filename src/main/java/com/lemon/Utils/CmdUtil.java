package com.lemon.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**dos命令工具类
 * @author lym
 *
 */
public class CmdUtil {
	private static Logger logger = Logger.getLogger(CmdUtil.class);
	public static List<String> execCmd(String cmd){
		//获取当前运行时对象
		Runtime runtime = Runtime.getRuntime();
		BufferedReader bufferedReader = null;
		try {
			//执行cmd命令，/c--新打开一个cmd窗口
			Process process = runtime.exec("cmd /c "+cmd );
			//创建缓冲读取流对象，读取命令输出的信息
		    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			//循环读取每一行信息
			String line = "";
			//用集合保存命令输出的结果
			List<String> resultContent = new ArrayList<String>();
			while((line = bufferedReader.readLine())!= null){
				if (line.contains("[Appium] Appium REST http interface listener started")) {
					logger.info("***********appium成功开启**************");
				}
				resultContent.add(line);
			}
			return resultContent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("读取命令输出信息失败");
			e.printStackTrace();
		}finally {
			//关闭缓冲读取流
			if (bufferedReader!=null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.info("关闭缓冲读取流失败");
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(execCmd("adb devices"));
	}
}
