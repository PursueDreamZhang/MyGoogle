package com.huida.googleplayfinal.bean;

import java.util.ArrayList;

public class Home {
	private ArrayList<String> picture;//轮播大图的url
	private ArrayList<AppInfo> list;//app的集合
	public ArrayList<String> getPicture() {
		return picture;
	}
	public void setPicture(ArrayList<String> picture) {
		this.picture = picture;
	}
	public ArrayList<AppInfo> getList() {
		return list;
	}
	public void setList(ArrayList<AppInfo> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "Home [picture=" + picture.size() + ", list=" + list.size() + "]";
	}
	
	
	
}
