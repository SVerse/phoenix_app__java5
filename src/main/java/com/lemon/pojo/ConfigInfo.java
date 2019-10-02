package com.lemon.pojo;

import java.util.List;

public class ConfigInfo {
	private String platformName;
	private String appPackage;
	private String appActivity;
	private String testName;
	private List<String> csList;
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	public String getAppActivity() {
		return appActivity;
	}
	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public List<String> getCsList() {
		return csList;
	}
	public void setCsList(List<String> csList) {
		this.csList = csList;
	}
	@Override
	public String toString() {
		return "ConfigInfo [platformName=" + platformName + ", appPackage=" + appPackage + ", appActivity="
				+ appActivity + ", testName=" + testName + ", csList=" + csList + "]";
	}
	public ConfigInfo(String platformName, String appPackage, String appActivity, String testName,
			List<String> csList) {
		super();
		this.platformName = platformName;
		this.appPackage = appPackage;
		this.appActivity = appActivity;
		this.testName = testName;
		this.csList = csList;
	}

	
}
