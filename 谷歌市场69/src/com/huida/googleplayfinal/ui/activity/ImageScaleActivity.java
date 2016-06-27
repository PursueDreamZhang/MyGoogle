package com.huida.googleplayfinal.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.lib.photoview.HackyViewPager;
import com.huida.googleplayfinal.ui.adapter.ImageScaleAdapter;

public class ImageScaleActivity extends Activity{
	private HackyViewPager viewPager;
	private ImageScaleAdapter imageScaleAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<String> urlList = getIntent().getStringArrayListExtra("urlList");
		int currentItem = getIntent().getIntExtra("currentItem", 0);
		
		setContentView(R.layout.activity_image_scale);
		viewPager = (HackyViewPager) findViewById(R.id.viewPager);
		
		imageScaleAdapter = new ImageScaleAdapter(urlList);
		viewPager.setAdapter(imageScaleAdapter);
		//默认选中第几个
		viewPager.setCurrentItem(currentItem);
		
	}
}
