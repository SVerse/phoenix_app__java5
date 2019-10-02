package com.lemon.base;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import javax.swing.LookAndFeel;

import org.apache.log4j.Logger;
import org.apache.velocity.test.misc.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lemon.Utils.XMLUtil;
import com.lemon.listener.TestngRetry;
import com.lemon.pojo.Locator;
import com.lemon.pojo.Page;

import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;


public class BasePage {
	  public static AndroidDriver<WebElement> androidDriver;
	  private Logger logger = Logger.getLogger(BasePage.class);
	  
	  //初始化threadLocal对象来保证程序运行的线程安全问题，以免testng进行多线程并发的的安全问题
	  public static  ThreadLocal<AndroidDriver> threadLocal = new ThreadLocal<AndroidDriver>();
	  
	  @BeforeSuite
	  public void startServer() throws InterruptedException{
		  //因为appium开启需要一定的时间，需要加载等待，确保用例执行前appium已经运行
		  Thread.sleep(5000);
	  }
	  
	  @Parameters({"deviceName","platformName","appPackage","appActivity","appiumUrl","automator2_port"})
	  @BeforeTest
	  public void setUp(String deviceName,String platformName,String appPackage,
			  String appActivity,String appiumUrl,String automator2_port) throws MalformedURLException {
	  //配置设备信息
	  logger.info("***********初始化Android driver对象**************");
	  DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
	  desiredCapabilities.setCapability("deviceName", deviceName);
	  desiredCapabilities.setCapability("platformName", platformName);
	  desiredCapabilities.setCapability("appPackage", appPackage);
	  desiredCapabilities.setCapability("appActivity", appActivity);
	  //指定noRest不清除应用数据
	  //desiredCapabilities.setCapability("noRest", true);
	  //指定automationName为uiautomator2,为了获取toast
	  desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,AutomationName.ANDROID_UIAUTOMATOR2);
	  //设置uiautomator2端口，防止多线程并发的时候appium通讯的时候端口占用的问题
	  desiredCapabilities.setCapability("systemPort", automator2_port);
	  desiredCapabilities.setCapability("udid", deviceName);

	  //创建驱动对象
	  //取得threadLocal存放的androidDriver，如果不为null说明没有初始化过驱动
	  androidDriver = threadLocal.get();
	  if (androidDriver ==null) {
		 androidDriver = new AndroidDriver<WebElement>(new URL(appiumUrl), desiredCapabilities);
		 threadLocal.set(androidDriver);
	}
	  logger.info(getdriver().getCapabilities());
	  //在测试开始前将retryCount置为1
	  TestngRetry.setRetryCout(1);
	  }

	  @AfterTest
	  public void tearDown() {
		  logger.info("*****************测试结束，关闭driver对象*****************");
		  getdriver().quit();
	  }
  
	  
	  public static AndroidDriver getdriver(){
		  return threadLocal.get();
	  }

	/**
	 * 显示等待
	 * @param androidDriver
	 * @param locator
	 * @return
	 */
	public  WebElement getElement(String pageDesc,String locatorDesc){
		logger.info("在【"+pageDesc+"】"+"去查找元素【"+locatorDesc+"】");
		String locatorBy = "";
		String locatorValue = "";
		try{
			//得到对应的locatorby，locatorValue
			for (Page page  : XMLUtil.pageLists) {
				String pageKwd = page.getPageDesc();
				List<Locator> listLocator = page.getListLocator();
				if (pageKwd.equals(pageDesc)) {
					for (Locator loca : listLocator) {
						String locatorKwd = loca.getDesc();
						if (locatorKwd.equals(locatorDesc)) {
							locatorBy =loca.getBy();
							locatorValue = loca.getValue();
						}
					}
				}
			}
			
			//通过by和value找到By对象---反射
			//1、找到类的字节码
			Class clazz = By.class;
			//2、根据字节码得到对应的方法
			Method method = clazz.getMethod(locatorBy, String.class);
			//3、执行对应的方法
			By by = (By)method.invoke(null,locatorValue);
			WebDriverWait  webDriverWait = new WebDriverWait(getdriver(), 30);
			WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
			return webElement;
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info("找不到对应方法");
				e.printStackTrace();
				
			}
		
		return null;
	}
	
	
	/**通过文本信息获取元素
	 * @param pageDesc
	 * @param locatorDesc
	 * @return
	 */
	public WebElement getElementByText(String pageDesc ,final String locatorDesc ){
		logger.info("在【"+pageDesc+"】"+"去查找元素【"+locatorDesc+"】");
		WebDriverWait webDriverWait = new WebDriverWait(getdriver(), 30);
		WebElement webElement = webDriverWait.until(new ExpectedCondition<WebElement>() {

			@Override
			public WebElement apply(WebDriver input) {
				// TODO Auto-generated method stub
				return getdriver().findElementByAndroidUIAutomator("new UiSelector().text("+locatorDesc+")");
			}
		});
		return webElement;
	}
	
	
	/**点击元素
	 * @param pageDesc
	 * @param locatorDesc
	 */
	public void click(String pageDesc,String locatorDesc ){
		WebElement webElement = getElement(pageDesc, locatorDesc);
		logger.info("点击了元素【"+locatorDesc+"】");
		webElement.click();
	}
	
	
	/**输入数据
	 * @param pageDesc
	 * @param locatorDesc
	 * @param data
	 */
	public void sendData(String pageDesc,String locatorDesc,String data){
		WebElement webElement = getElement(pageDesc, locatorDesc);
		logger.info("在输入框【"+locatorDesc+"】输入了【"+data+"】");
		webElement.sendKeys(data);
	}
	
	
	/**找toast文本信息
	 * @param tips
	 */
	public String getToast(final String tips){
		logger.info("查找toast信息【"+tips+"】");
		WebDriverWait webDriverWait = new WebDriverWait(getdriver(), 30);
		
		WebElement webElement = webDriverWait.until(new ExpectedCondition<WebElement>() {
			//根据xpath获取toast
			@Override
			public WebElement apply(WebDriver input) {
				// TODO Auto-generated method stub
				return getdriver().findElement(By.xpath("//*[contains(@text,"+tips+")]"));
			}
			
		});
		return webElement.getText();
	}
	
	/**实现app内部页面跳转
	 * @param appPackage
	 * @param appActivity
	 */
	 public void startPage(String pageDesc){
		 String activityName = "";
		 for (Page  page : XMLUtil.pageLists) {
			String pageDesc2=page.getPageDesc();
			if (pageDesc2.equals(pageDesc)) {
				activityName=page.getActivityName();
			}
		}
		 logger.info("开启【"+pageDesc+"】页面");
		  Activity activity = new Activity("com.lemon.lemonban", activityName);
		  getdriver().startActivity(activity);
	  }
	 
	 /**向下滑动
		 * @param androidDriver
		 */
		public  void swipeDown(){
			//创建触摸对象touchaction
			TouchAction touchAction=new TouchAction(getdriver());
			//分别得到屏幕的x轴，Y轴
			int windowX = getdriver().manage().window().getSize().getWidth();
			int windowY = getdriver().manage().window().getSize().getHeight();
			//滑动的起始点坐标
			int startX = windowX/2;
			int startY = windowY/4;
			
			int endX = windowX/2;
			int endY = 3*windowY/4;
			//滑动的时间
			Duration duration = Duration.ofMillis(1000);
			
			touchAction.press(startX, startY).waitAction(duration).moveTo(endX, endY).release();
			touchAction.perform();
		}
		

		/**向上滑动
		 * @param androidDriver
		 */
		public  void swipeUp()
		{
			TouchAction touchAction = new TouchAction(getdriver());
			//获取当前屏幕的x轴，Y轴
			int windowX = getdriver().manage().window().getSize().getWidth();
			int windowY = getdriver().manage().window().getSize().getHeight();
			//起始点坐标
			int startX = windowX/2;
			int startY = 3*windowY/4;
			int endX = windowX/2;
			int endY = windowY/2;
			//滑动时间
			Duration duration = Duration.ofMillis(1000);
			touchAction.press(startX, startY).waitAction(duration).moveTo(endX,endY).release();
			touchAction.perform();
					
		}
		
		
		/**向左滑动
		 * @param androidDriver
		 */
		public void swipeLeft(){
			
			TouchAction touchAction = new TouchAction(getdriver());
			
			int windowX = getdriver().manage().window().getSize().getWidth();
			int windowY = getdriver().manage().window().getSize().getHeight();
			
			int startX = windowX/2;
			int startY = windowY/4;
			
			int endX = windowX/4;
			int endY = windowY/4;
			
			Duration duration= Duration.ofMillis(1000);
			touchAction.press(startX, startY).waitAction(duration).moveTo(endX, endY).release();
			touchAction.perform();
		}
		
		
		/**向右滑动
		 * @param androidDriver
		 */
		public  void swipeRight(){
			
			TouchAction touchAction = new TouchAction(getdriver());
			
			int windowX = getdriver().manage().window().getSize().getWidth();
			int windowY = getdriver().manage().window().getSize().getHeight();
			
			int startX = windowX/2;
			int startY = windowY/4;
			
			int endX = 3*windowX/4;
			int endY = windowY/4;
			
			Duration duration= Duration.ofMillis(1000);
			touchAction.press(startX, startY).waitAction(duration).moveTo(endX, endY).release();
			touchAction.perform();
		}
		
		
		/**解锁九宫格--连续滑动
		 * @param androidDriver
		 */
		public  void unlock(){
			TouchAction touchAction = new TouchAction(getdriver());
			Duration duration = Duration.ofMillis(300);
			//move to 坐标的偏移量:第一个点（170,540），第二个点（380,540），偏移量：380-170,540-540即（210,0）
			//其余点坐标（600,540）（380,750）（170,980）（380,980）(600,980)Z字形
			touchAction.press(170,540).waitAction(duration).moveTo(210,0).moveTo(220,0).moveTo(-220, 210).moveTo(-210,230).moveTo(210, 0).moveTo(220, 0).release();
			touchAction.perform();
		}
		
		
		   /**放大操作
		 * @param x
		 * @param y
		 */
		public  void zoom(int x, int y) {
			MultiTouchAction multiTouch = new MultiTouchAction(getdriver());
			
			int scrHeight = getdriver().manage().window().getSize().getHeight();
			int yOffset = 100;
			
			if (y - 100 < 0) {
			    yOffset = y;
			} else if (y + 100 > scrHeight) {
			    yOffset = scrHeight - y;
			}
			
			TouchAction action0 = new TouchAction(getdriver()).press(x, y).moveTo(0, -yOffset).release();
			TouchAction action1 = new TouchAction(getdriver()).press(x, y).moveTo(0, yOffset).release();
			
			multiTouch.add(action0).add(action1);
			
			multiTouch.perform();
		 }
		
		
		/**缩小
		 * @param x
		 * @param y
		 */
		public  void pinch(int x, int y) {
	        MultiTouchAction multiTouch = new MultiTouchAction(getdriver());

	        int scrHeight = getdriver().manage().window().getSize().getHeight();
	        int yOffset = 100;

	        if (y - 100 < 0) {
	            yOffset = y;
	        } else if (y + 100 > scrHeight) {
	            yOffset = scrHeight - y;
	        }

	        TouchAction action0 = new TouchAction(getdriver()).press(x, y - yOffset).moveTo(x, y).release();
	        TouchAction action1 = new TouchAction(getdriver()).press(x, y + yOffset).moveTo(x, y).release();

	        multiTouch.add(action0).add(action1);

	        multiTouch.perform();
	    }
		
		/**多点点击
		 * @param androidDriver
		 * @param fingers
		 * @param x
		 * @param y
		 * @param duration
		 */
		public  void tap(int fingers, int x, int y) {
	        MultiTouchAction multiTouch = new MultiTouchAction(getdriver());

	        for (int i = 0; i < fingers; i++) {
	            multiTouch.add(createTap(x, y));
	        }

	        multiTouch.perform();
	    }
		
		
	    public  TouchAction createTap(int x, int y) {
	        TouchAction tap = new TouchAction(getdriver());
	        Duration duration = Duration.ofMillis(500);
	        return tap.press(x, y).waitAction(duration).release();
	    }

}
