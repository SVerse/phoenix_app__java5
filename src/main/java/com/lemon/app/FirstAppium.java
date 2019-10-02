package com.lemon.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.activation.Activatable;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.Arg;
import org.apache.tools.ant.launch.Locator;
import org.eclipse.jetty.websocket.common.events.AbstractEventDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lemon.base.BasePage;
import com.sun.jna.platform.win32.WinUser.HHOOK;

import io.appium.java_client.MobileBy.ByAndroidUIAutomator;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class FirstAppium {
	public static void main(String args[])throws Exception{
		//配置设备信息
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("deviceName", "192.168.19.101:5555");
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("appPackage", "com.lemon.lemonban");
		desiredCapabilities.setCapability("appActivity", "com.lemon.lemonban.activity.WelcomeActivity");
		//desiredCapabilities.setCapability("appPackage", "'com.xxzb.fenwoo'");
		//desiredCapabilities.setCapability("appActivity", "com.xxzb.fenwoo.activity.addition.WelcomeActivity");
		//指定noReset不清除应用数据
		//desiredCapabilities.setCapability("noReset", true);
		//指定automationName为uiautomator2,为了获取toast
		desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,AutomationName.ANDROID_UIAUTOMATOR2);
		//创建驱动对象,
		AndroidDriver<WebElement> androidDriver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
		Thread.sleep(2000);
		//定位页面元素
		//定位我的柠檬--id
		//androidDriver.findElement(By.id("com.lemon.lemonban:id/navigation_my")).click();
		//定位全程班--text    若用到uiautomator检查jdk版本在1.8以上
		//androidDriver.findElementByAndroidUIAutomator("new UiSelector().text(\"就业信息\")").click();
		//Thread.sleep(3000);
		//--className
//		List<WebElement> list = androidDriver.findElements(By.className("android.widget.LinearLayout"));
//		list.get(5).click();
		//--AccessibilityId
		//androidDriver.findElementByAccessibilityId("题库").click();
		
		
		//相对定位  先找到父元素，再找到子元素
		//WebElement fatherElement = androidDriver.findElement(By.id("com.lemon.lemonban:id/linearLayout"));
		//fatherElement.findElement(By.id("com.lemon.lemonban:id/category_title")).click();

		//操作页面元素模拟用户操作
		//通过断言和日志查看测试结果
		//swipeDown(androidDriver);
		//swipeUp(androidDriver);
		//swipeLeft(androidDriver);
		//swipeRight(androidDriver);
		//pinch(androidDriver, 200, 400);
		//unlock(androidDriver);
		//按音量+键
		//androidDriver.pressKeyCode(24);
		//启动APP的某一个activity
//		Activity activity = new Activity("com.lemon.lemonban", ".activity.LoginActActivity");
//		androidDriver.startActivity(activity);
		//跨应用调用手机自带浏览器
//		Activity activity = new Activity("com.android.browser", "com.android.browser.BrowserActivity");
//		androidDriver.startActivity(activity);
		//获取当前页面所有元素
//     	String pageSource = androidDriver.getPageSource();
//		System.out.println(pageSource);
		//获取当前正在运行的activity
//		System.out.println(androidDriver.currentActivity());
		//获取desiredCapabilities相关配置信息
//		System.out.println(androidDriver.getCapabilities());
		
		login(androidDriver);
//		testHybrid(androidDriver);
//		testListView(androidDriver);
		Thread.sleep(5000);
		//退出
		androidDriver.quit();
	}
	
	
	
	
	public static void testListView(AndroidDriver<WebElement> androidDriver) throws InterruptedException{
		//点击题库
		androidDriver.findElementByAndroidUIAutomator("new UiSelector().text(\"题库\")").click();
		Thread.sleep(3000);
//		String beforeSoure = "";
//		String afterSoure = "";
//		beforeSoure = androidDriver.getPageSource();
//		swipeUp(androidDriver);
//		afterSoure = androidDriver.getPageSource();
//		//每次向上滑动一段距离，判断当前页面是否包含接口测试这个元素，如果不包含继续滑动
//		//前后页面信息一致说明列表已经滑动到了底部，
//		while (!beforeSoure.equals(afterSoure) && !afterSoure.contains("接口测试")) {
//			beforeSoure = androidDriver.getPageSource();
//			swipeUp(androidDriver);
//			afterSoure = androidDriver.getPageSource();
//		}
//		//找到滑动列表中的接口测试
//		if (afterSoure.contains("接口测试")) {
//			
//			androidDriver.findElementByAndroidUIAutomator("new UiSelector().text(\"接口测试\")").click();
//		}
//	直接方式实现查找接口测试
		androidDriver.findElementByAndroidUIAutomator(
		"new UiScrollable(new UiSelector().scrollable(true).instance(0).scrollIntoView(new UiSelector().textMatches(\"接口测试\").instance(0))").click();
	}
	
	
	public static void testHybrid(AndroidDriver<WebElement> androidDriver) throws InterruptedException{
		//找到全程班元素
		androidDriver.findElementByAndroidUIAutomator("new UiSelector().text(\"全程班\")").click();
		Thread.sleep(5000);
		//找到所有的context
		Set<String> contexts = androidDriver.getContextHandles();
		System.out.println(contexts);
		Thread.sleep(3000);
		//切换到webview context
		androidDriver.context("WEBVIEW_com.lemon.lemonban");
		Thread.sleep(5000);
		androidDriver.findElement(By.xpath("//*[@id=\"react-body\"]/main/div[1]")).click();
		Thread.sleep(5000);
		
		
		
	}
	
	/**
	 * 登录
	 * @param androidDriver
	 */
	public static void login(AndroidDriver<WebElement> androidDriver){
		//找到我的柠檬元素
		getElement(androidDriver, By.id("com.lemon.lemonban:id/navigation_my")).click();
		//找到头像元素并点击
		getElement(androidDriver, By.id("com.lemon.lemonban:id/fragment_my_lemon_avatar_title")).click();
		//找到手机号并输入
		getElement(androidDriver, By.id("com.lemon.lemonban:id/et_mobile")).sendKeys("18811111111");
		//找到密码输入框并输入
		getElement(androidDriver, By.id("com.lemon.lemonban:id/et_password")).sendKeys("123456");
		//点击登录按钮.
		getElement(androidDriver, By.id("com.lemon.lemonban:id/btn_login")).click();
		//根据xpath获取toast
		String toastTip=getElement(androidDriver, By.xpath("//*[contains(@text,'错误的账号')]")).getText();
		System.out.println(toastTip);
	}
	
	
	
	/**
	 * 显示等待
	 * @param androidDriver
	 * @param locator
	 * @return
	 */
	public static WebElement getElement(AndroidDriver<WebElement> androidDriver,By locator){
		WebDriverWait  webDriverWait = new WebDriverWait(androidDriver, 30);
		try {
			WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
			return webElement;
			
		} catch (Exception e) {
			System.out.println("定位元素超时");
		}
		return null;
	}
	
	
	
	/**向下滑动
	 * @param androidDriver
	 */
	public static void swipeDown(AndroidDriver<WebElement> androidDriver){
		//创建触摸对象touchaction
		TouchAction touchAction=new TouchAction(androidDriver);
		//分别得到屏幕的x轴，Y轴
		int windowX = androidDriver.manage().window().getSize().getWidth();
		int windowY = androidDriver.manage().window().getSize().getHeight();
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
	public static void swipeUp(AndroidDriver<WebElement> androidDriver){
		TouchAction touchAction = new TouchAction(androidDriver);
		//获取当前屏幕的x轴，Y轴
		int windowX = androidDriver.manage().window().getSize().getWidth();
		int windowY = androidDriver.manage().window().getSize().getHeight();
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
	public static void swipeLeft(AndroidDriver<WebElement> androidDriver){
		
		TouchAction touchAction = new TouchAction(androidDriver);
		
		int windowX = androidDriver.manage().window().getSize().getWidth();
		int windowY = androidDriver.manage().window().getSize().getHeight();
		
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
	public static void swipeRight(AndroidDriver<WebElement> androidDriver){
		
		TouchAction touchAction = new TouchAction(androidDriver);
		
		int windowX = androidDriver.manage().window().getSize().getWidth();
		int windowY = androidDriver.manage().window().getSize().getHeight();
		
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
	public static void unlock(AndroidDriver<WebElement> androidDriver){
		TouchAction touchAction = new TouchAction(androidDriver);
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
	public static void zoom(AndroidDriver<WebElement> androidDriver,int x, int y) {
		MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);
		
		int scrHeight = androidDriver.manage().window().getSize().getHeight();
		int yOffset = 100;
		
		if (y - 100 < 0) {
		    yOffset = y;
		} else if (y + 100 > scrHeight) {
		    yOffset = scrHeight - y;
		}
		
		TouchAction action0 = new TouchAction(androidDriver).press(x, y).moveTo(0, -yOffset).release();
		TouchAction action1 = new TouchAction(androidDriver).press(x, y).moveTo(0, yOffset).release();
		
		multiTouch.add(action0).add(action1);
		
		multiTouch.perform();
	 }
	
	
	/**缩小
	 * @param x
	 * @param y
	 */
	public static void pinch(AndroidDriver<WebElement> androidDriver,int x, int y) {
        MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);

        int scrHeight = androidDriver.manage().window().getSize().getHeight();
        int yOffset = 100;

        if (y - 100 < 0) {
            yOffset = y;
        } else if (y + 100 > scrHeight) {
            yOffset = scrHeight - y;
        }

        TouchAction action0 = new TouchAction(androidDriver).press(x, y - yOffset).moveTo(x, y).release();
        TouchAction action1 = new TouchAction(androidDriver).press(x, y + yOffset).moveTo(x, y).release();

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
	public static void tap(AndroidDriver<WebElement> androidDriver,int fingers, int x, int y) {
        MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);

        for (int i = 0; i < fingers; i++) {
            multiTouch.add(createTap(androidDriver,x, y));
        }

        multiTouch.perform();
    }
	
	
    public static TouchAction createTap(AndroidDriver<WebElement> androidriver,int x, int y) {
        TouchAction tap = new TouchAction(androidriver);
        Duration duration = Duration.ofMillis(500);
        return tap.press(x, y).waitAction(duration).release();
    }
}
