package com.huida.googleplayfinal.ui.activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.lib.PagerSlidingTab;
import com.huida.googleplayfinal.ui.adapter.MainPagerAdapter;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout drawerLayout;
	private PagerSlidingTab pagerSlidingTab;
	private ViewPager viewPager;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setActionBar();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		pagerSlidingTab = (PagerSlidingTab) findViewById(R.id.pagerSlidingTab);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		
		//1.给viewpager设置适配器
		viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
		//2.绑定viewPager和pagerSlidingTab
		pagerSlidingTab.setViewPager(viewPager);
	}

	/**
	 * 设置ActionBar
	 */
	private void setActionBar() {
		// 1.获取v4包中的ActionBar对象
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle(getString(R.string.app_name));

		// 2.设置ActionBar的home按钮可以点击
		actionBar.setDisplayHomeAsUpEnabled(true);// 设置Home按钮显示
		actionBar.setDisplayShowHomeEnabled(true);// 设置home按钮可以被点击

		// 3.使用drawerToggle同步状态
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer_am, 0, 0);
		drawerToggle.syncState();// 同步ActionBar和DrawerLayout的状态

		// 4.给3条线增加动画
		drawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
				// Log.e("tag", "newState: "+newState);
				drawerToggle.onDrawerStateChanged(newState);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// Log.e("tag", "slideOffset: "+slideOffset);
				drawerToggle.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// Log.e("tag", "onDrawerOpened: ");
				drawerToggle.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// Log.e("tag", "onDrawerClosed: ");
				drawerToggle.onDrawerClosed(drawerView);
			}
		});
	}

	// 当ActionBar的home按钮被点击的时候会调用该方法
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		drawerToggle.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
