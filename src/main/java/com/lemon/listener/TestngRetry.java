package com.lemon.listener;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** 自定义TestngRetry类来实现IRetryAnalyzer接口，完成testng重试机制
 * @author lym
 *
 */
public class TestngRetry implements IRetryAnalyzer {
	private Logger logger = Logger.getLogger(TestngRetry.class);
	//多线程安全
	public static ThreadLocal<Integer> threadRetryLocal = new ThreadLocal<Integer>();
	//当前重试次数
	public static int retryCount = 1;
	//最大重试次数
	private int retryMaxCount = 2;
	@Override
	public boolean retry(ITestResult result) {
		// TODO Auto-generated method stub
		//设置条件让testng retry在满足条件的情况下执行重试机制
		if (retryCount<=retryMaxCount) {
			//返回true代表执行重试机制
			logger.info("开始第【"+retryCount+"】次重试机制"+"测试方法为【"+result.getName()+"】");
			retryCount++;
			//将变化之后的retryCount添加到threadRetryLocal中
			setRetryCout(retryCount);
			return true;
			
		}
		//返回false表示不执行重试机制
		return false;
	}
	//解决testng和dataprovider冲突问题：
	//1、将testng版本升级到6.11+
	//2、在每条测试用例执行完之后将retryCount置为1（不管是成功还是失败都置为1）
	 
	
	public static int getRetryCount(){
		return threadRetryLocal.get();
	}
	
	
	public static void setRetryCout(int retryCount){
		threadRetryLocal.set(retryCount);
	}
}
