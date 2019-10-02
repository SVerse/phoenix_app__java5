package com.lemon.listener;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.lemon.Utils.ScreenUtil;


/**通过继承TestListenerAdapter来自定义一个监听器
 * @author lym
 *
 */
public class RepostListener extends TestListenerAdapter {
	private Logger  logger=Logger.getLogger(RepostListener.class);
	/* (non-Javadoc)
	 * @see org.testng.TestListenerAdapter#onTestFailure(org.testng.ITestResult)
	 * 当某一个用例（测试方法）执行失败了，就进入到此方法
	 */
	@Override
	public void onTestFailure(ITestResult tr) {
		
		String baseDir="target/surefire-reports/screenshot";
		String testName=tr.getTestContext().getCurrentXmlTest().getName();
		baseDir+=(File.separator+testName);
		String dateString=DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		baseDir+=(File.separator+dateString);
		logger.info("baseDir为：【"+baseDir+"】");
		//在baseDir目录下保存截图
		File destFile=ScreenUtil.takeScreenshot(baseDir);
		//截图文件的绝对路径
		String absPath=destFile.getAbsolutePath();
		//logger.info("截图绝对路径"+absPath);
		//字符串截取替换
		//拿到的地址：D:\workspace\phoenix_java_web\target\surefire-reports\screenshot\Register\2018-07-23\1532332741579.jpg
		//目标地址：http://localhost/screenshot/Register/2018-07-23/1532332741579.jpg
		String toBeRepalced=absPath.substring(0, absPath.indexOf("screenshot"));
		String acessPath=absPath.replace(toBeRepalced, "http://localhost/");
		logger.info("截图访问地址为："+acessPath);
		Reporter.log("<img src='"+acessPath+"' width='100' height='100'><a href='"+acessPath+"' target='_blank'>点击查看大图</a></img>");
		//在每条测试用例执行失败之后就将retryCount置为1
		TestngRetry.setRetryCout(1);
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
		//在每条测试用例执行成功之后就将retryCount置为1
		TestngRetry.setRetryCout(1);
		super.onTestSuccess(arg0);
	}
}
