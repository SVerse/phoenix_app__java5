package com.lemon.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

/**动态改变测试方法中注解参数，只针对@Test注解生效
 * @author lym
 *
 */
public class RetryListener implements IAnnotationTransformer {

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		//得到IRetryAnalyzer对象
		IRetryAnalyzer iRetryAnalyzer = annotation.getRetryAnalyzer();
		//判断测试方法中有没有配置对应的参数
		if (iRetryAnalyzer==null) {
			annotation.setRetryAnalyzer(TestngRetry.class);
		}
	}
	

}
