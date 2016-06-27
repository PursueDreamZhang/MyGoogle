package com.huida.googleplayfinal.bean;

import java.util.ArrayList;

public class Category {
	private String title;//大分类的标题
	private ArrayList<CategoryInfo> infos;//小分类的集合
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<CategoryInfo> getInfos() {
		return infos;
	}
	public void setInfos(ArrayList<CategoryInfo> infos) {
		this.infos = infos;
	}
	
	
	
}
