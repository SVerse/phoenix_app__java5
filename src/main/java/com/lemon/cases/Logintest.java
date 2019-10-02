package com.lemon.cases;

import org.testng.annotations.Test;

import com.lemon.Utils.ExcelUtil;
import com.lemon.Utils.XMLUtil;
import com.lemon.base.BasePage;
import com.lemon.listener.TestngRetry;
import com.lemon.pojo.Locator;
import com.lemon.pojo.Page;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

public class Logintest extends BasePage {
	
  //为测试方法添加重试机制第一种
  //@Test(priority = 1,retryAnalyzer=TestngRetry.class)
  @Test(priority = 1)
  public void testMainPage(){
	//找到我的柠檬元素
	click("主页页面", "我的柠檬");
	//找到头像元素并点击
	click("主页页面", "头像元素");
  }
  
  @Test(dataProvider="getDatas",priority = 2)
  public void testLogin(String mobilephone, String password,String toastTips){
		
	//找到手机号并输入
	sendData("登录页面", "手机号码输入框",mobilephone);
	//找到密码输入框并输入
	sendData("登录页面", "密码输入框",password);
	//点击登录按钮.
	click("登录页面", "登录按钮");
	//根据xpath获取toast
	//String toastTip=getElement(androidDriver, By.xpath("//*[contains(@text,'错误的账号')]")).getText();
	//System.out.println(getToast(toastTips));
	//Assert.assertEquals(getToast(toastTips), toastTips);
  }
  
//  @DataProvider
//  public Object[][] getDatas(){
//	  Object [][] datas = {
//			  			   {"18288220110",""},
//			  			   {"18588220110","123456"},
//			  			   {"13323234545","234545"}
//			  			 };
//	  return datas;
//  }
  
  
//通过解析Excel获得测试数据 
  @DataProvider
  public Object[][] getDatas(){
	  Object [][] datas =ExcelUtil.readExcel("src/main/resources/login_data.xlsx", "login", 2, 4, 1, 3);
	  return datas;
  }

}
