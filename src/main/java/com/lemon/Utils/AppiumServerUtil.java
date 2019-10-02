package com.lemon.Utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;

public class AppiumServerUtil {
	private static Logger logger = Logger.getLogger(AppiumServerUtil.class);
	public static List<String> appiumPortList = new ArrayList<String>();
	public static List<String> deviceList = new ArrayList<String>();
	public static List<String> automator2PortList = new ArrayList<String>();
	private static String appiumJsPath = "C:\\Users\\lym\\AppData\\Local\\appium-desktop\\app-1.5.0\\resources\\app\\node_modules\\appium\\build\\lib\\main.js";
	public static List<String> getDeviceUdid(String cmd){
		//得到命令输出的结果
		List<String> deviceInfoList = CmdUtil.execCmd(cmd);
		List<String> udidList = new ArrayList<String>();
		//对输出结果进行解析
		for (int i = 0; i < deviceInfoList.size(); i++) {
			//排除第一行：List of devices attached
			if (i!=0) {
				try {
					String [] deviceInfo = deviceInfoList.get(i).split("\t");
					if (deviceInfo[1].equals("device")) {
						//判断数组中第二个值有device说明存在设备信息，将数组的一个值添加到列表
						udidList.add(deviceInfo[0]);
					};
					
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO: handle exception
					//当有数组越界异常时i的值加2
					i= i+2;
				}
			}
		}
		if (udidList.size()>0) {
			logger.info("共获取到【"+udidList.size()+"】台设备");
		}else if (udidList.size()==0) {
			logger.info("获取设备失败，终止脚本运行");
			System.exit(0);
		}
		return udidList;
	}
	

	public static void main(String[] args) {
		deviceList = getDeviceUdid("adb devices");
		//1、根据设备数量得到appium端口号列表
		appiumPortList = PortUtil.createPort(4723,deviceList.size());
		//2、根据设备数量得到bootstrap端口号
		List<String> bootstrapList = PortUtil.createPort(4724, deviceList.size());
		//3、根据已有信息拼接启动appium命令参数，因为可能有多条命令所以将命令保存在一个集合中
		List<String> commandList = new ArrayList<String>();
		//4、根据设备数量得到uiautomator2端口号列表
		automator2PortList = PortUtil.createPort(8200, deviceList.size());
		for (int i = 0; i < appiumPortList.size(); i++) {
			
			String appiumCommand = "node " + appiumJsPath + " -a " + "127.0.0.1 " + "-p " 
						+ appiumPortList.get(i) + " -bp " + bootstrapList.get(i) + " --session-override";
			commandList.add(appiumCommand);
		}
		for (final String command : commandList) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//开启appium server
					CmdUtil.execCmd(command);
					
				}
			}).start();
			//System.out.println(command);
	}
		XMLUtil.createTestngXml();
		
		//通过代码方式运行testng.xml文件
		//1、创建testng对象
		TestNG testNG = new TestNG();
		//2、将需要执行的testng配置文件的路径加入到集合中
		List<String> testngFileList = new ArrayList<String>();
		testngFileList.add(System.getProperty("user.dir")+"\\testng_app.xml");
		//3、使用setTestSuites添加list集合
		testNG.setTestSuites(testngFileList);
		//4、执行testng
		testNG.run();
		//用mvn test执行时，去掉pom.xml文件中依赖中带<scope>test</scope>的标签
 }
}
