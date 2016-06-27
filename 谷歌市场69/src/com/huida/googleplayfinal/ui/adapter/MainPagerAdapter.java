package com.huida.googleplayfinal.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.ui.fragment.FragmentFactory;
import com.huida.googleplayfinal.util.CommonUtil;

public class MainPagerAdapter extends FragmentPagerAdapter{
	private String[] tabs;
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		//获取字符串的数组
		tabs = CommonUtil.getStringArray(R.array.tab_names);
	}

	@Override
	public Fragment getItem(int position) {
		return FragmentFactory.create(position);
	}

	@Override
	public int getCount() {
		return tabs.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return tabs[position];
	}

}
