package com.huida.googleplayfinal.ui.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasicAdapter<T> extends BaseAdapter{
	protected ArrayList<T> list;
	//使用成员变量生成构造方法：alt+shift+s->o
	public BasicAdapter(ArrayList<T> list) {
		super();
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
