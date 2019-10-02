package com.lemon.pojo;

import java.util.List;

public class Page {
	
	private List<Locator> listLocator;
	private String pageDesc;
	private String activityName;
	
	public List<Locator> getListLocator() {
		return listLocator;
	}
	public void setListLocator(List<Locator> listLocator) {
		this.listLocator = listLocator;
	}
	public String getPageDesc() {
		return pageDesc;
	}
	public void setPageDesc(String pageDesc) {
		this.pageDesc = pageDesc;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	@Override
	public String toString() {
		return "Page [listLocator=" + listLocator + ", pageDesc=" + pageDesc + ", activityName=" + activityName + "]";
	}
	public Page(List<Locator> listLocator, String pageDesc, String activityName) {
		super();
		this.listLocator = listLocator;
		this.pageDesc = pageDesc;
		this.activityName = activityName;
	}
	
	
	
}
