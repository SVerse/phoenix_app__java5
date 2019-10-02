package com.lemon.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.lemon.pojo.ConfigInfo;
import com.lemon.pojo.Locator;
import com.lemon.pojo.Page;

public class XMLUtil {
	
	private static Logger logger = Logger.getLogger(XMLUtil.class);
	public static List<Page> pageLists = new ArrayList<Page>();
	public static ConfigInfo configInfo;
	static {
		parseUiLibrary();
		readConfigInfo();
	}
	
	/**
	 * 解析UI库
	 */
	public static void parseUiLibrary(){
		try {
			//创建解析器
			SAXReader reader = new SAXReader();
			//创建文档对象模型
			Document document = reader.read(XMLUtil.class.getResourceAsStream("/UiLibrary.xml"));
			//获取根节点
			Element rootElement = document.getRootElement();
			//获取根节点下的page元素
			List<Element> pageList = rootElement.elements("page");
			for (Element page : pageList) {
				String activityName = page.attributeValue("activityName");
				String pageDesc = page.attributeValue("pageDesc");
				List<Element> locatorList = page.elements("locator");
				List<Locator> locatorLists = new ArrayList<Locator>();
				for (Element locator : locatorList) {
					String locatorBy = locator.attributeValue("by");
					String locatorValue = locator.attributeValue("value");
					String locatorDesc = locator.attributeValue("desc");
					Locator loca = new Locator(locatorBy, locatorValue, locatorDesc);
					locatorLists.add(loca);
				}
				Page pg = new Page(locatorLists, pageDesc, activityName);
				pageLists.add(pg);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 自动生成testng.xml文件
	 */
	public static void createTestngXml(){
		//初始化document对象
		Document document = DocumentHelper.createDocument();
		document.addDocType("suite",null,"http://testng.org/testng-1.0.dtd");
		//添加根元素suite
		Element rootElement = DocumentHelper.createElement("suite");
		document.setRootElement(rootElement);
		//设置根元素属性---suite
		rootElement.addAttribute("name", "suite");
		rootElement.addAttribute("parallel", "tests");
		rootElement.addAttribute("thread-count", AppiumServerUtil.deviceList.size()+"");
		
		//添加listener元素及属性
		Element listenersElement = rootElement.addElement("listeners");
		Element htmlListener = listenersElement.addElement("listener");
		htmlListener.addAttribute("class-name", "org.uncommons.reportng.HTMLReporter");
		
		//添加RepostListener元素及属性
		Element  RepostListener = listenersElement.addElement("listener");
		RepostListener.addAttribute("class-name", "com.lemon.listener.RepostListener");
		
		Element  RetryListener = listenersElement.addElement("listener");
		RetryListener.addAttribute("class-name", "com.lemon.listener.RetryListener");
		
		//循环创建test标签及属性
		for (int i = 0;i<AppiumServerUtil.deviceList.size();i++) {
			//添加test标签及属性
			Element testElement = rootElement.addElement("test");
			testElement.addAttribute("name", "test_"+configInfo.getTestName()+(i+1+""));
			//添加parameter标签
			Element deviceName = testElement.addElement("parameter");
			deviceName.addAttribute("name", "deviceName");
			deviceName.addAttribute("value", AppiumServerUtil.deviceList.get(i));
			
			Element appPackage = testElement.addElement("parameter");
			appPackage.addAttribute("name", "appPackage");
			appPackage.addAttribute("value", configInfo.getAppPackage());
			
			Element platformName = testElement.addElement("parameter");
			platformName.addAttribute("name", "platformName");
			platformName.addAttribute("value", configInfo.getPlatformName());
			
			Element automator2_port = testElement.addElement("parameter");
			automator2_port.addAttribute("name", "automator2_port");
			automator2_port.addAttribute("value", AppiumServerUtil.automator2PortList.get(i));
			
			Element appActivity = testElement.addElement("parameter");
			appActivity.addAttribute("name", "appActivity");
			appActivity.addAttribute("value", configInfo.getAppActivity());
			
			Element appiumUrl = testElement.addElement("parameter");
			appiumUrl.addAttribute("name", "appiumUrl");
			appiumUrl.addAttribute("value","http://127.0.0.1:"+AppiumServerUtil.appiumPortList.get(i)+"/wd/hub");
			
			//添加class元素
			Element classElement = testElement.addElement("classes");
			Element classess = classElement.addElement("class");
			for (String csName : configInfo.getCsList()) {
				
				classess.addAttribute("name", csName);
			}
		}
		
		//创建输出格式 createPrettyPrint:漂亮格式的xml   createCompactFormat:兼容格式xml
		OutputFormat outputFormat =OutputFormat.createPrettyPrint();
		//输出流
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir")+"\\testng_app.xml");
			XMLWriter xmlWriter = new XMLWriter(fileOutputStream,outputFormat);
			xmlWriter.write(document);
			logger.info("创建testng_app.xml成功");
		} catch (Exception e) {
			logger.info("创建testng_app.xml失败");
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 读取ConfigInfo配置文件
	 */
	public static void readConfigInfo(){
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(XMLUtil.class.getResourceAsStream("/ConfigInfo.xml"));
			Element rootElement = document.getRootElement();
			Element platformElement = rootElement.element("platformName");
			String  platformName = platformElement.attributeValue("value");
			Element appPackageElement = rootElement.element("appPackage");
			String appPackage = appPackageElement.attributeValue("value");
			Element appActivityElement = rootElement.element("appActivity");
			String appActivity = appActivityElement.attributeValue("value");
			Element testNameElement = rootElement.element("testName");
			String testName = testNameElement.attributeValue("value");
			Element classElement = rootElement.element("classes");
			List<Element> clsElement = classElement.elements("class");
			List<String> csList = new ArrayList<String>();
 			for (Element element : clsElement) {
				String name = element.attributeValue("name");
				csList.add(name);
			}
			configInfo = new ConfigInfo(platformName, appPackage, appActivity, testName, csList);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
