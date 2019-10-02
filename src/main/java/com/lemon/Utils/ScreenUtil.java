package com.lemon.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.lemon.base.BasePage;

import io.appium.java_client.android.AndroidDriver;

public class ScreenUtil {
	private static Logger logger=Logger.getLogger(ScreenUtil.class);
	public static File takeScreenshot(String baseDir){
		AndroidDriver<WebElement> driver=BasePage.getdriver();
		File screenImg=null;
		logger.info("Android驱动开始截图");
		screenImg=driver.getScreenshotAs(OutputType.FILE);

		Date date=new Date();
		long time=date.getTime();
		File destFile=new File(baseDir+File.separator+time+".jpg");
		try {
			FileUtils.copyFile(screenImg, destFile);
			logger.info("拷贝图片文件"+time+".jpg到【"+destFile+"】");
		} catch (Exception e) {
			logger.info("拷贝失败");
			e.printStackTrace();
		}
		return destFile;
	}
}
