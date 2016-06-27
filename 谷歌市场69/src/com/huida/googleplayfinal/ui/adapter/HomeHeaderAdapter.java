package com.huida.googleplayfinal.ui.adapter;

import java.util.ArrayList;

import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomeHeaderAdapter extends PagerAdapter{
	private ArrayList<String> list;
	public HomeHeaderAdapter(ArrayList<String> list) {
		super();
		this.list = list;
	}
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
//		return list.size();
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(GooglePlayApplication.getContext());
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+list.get(position%list.size()), imageView, ImageLoaderOptions.pager_options);
		
		container.addView(imageView);//将ImageView加入到ViewPager中
		return imageView;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

}
