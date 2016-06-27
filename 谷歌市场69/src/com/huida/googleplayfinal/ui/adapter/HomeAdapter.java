package com.huida.googleplayfinal.ui.adapter;

import java.util.ArrayList;

import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeAdapter extends BasicAdapter<AppInfo>{
	
	public HomeAdapter(ArrayList<AppInfo> list) {
		super(list);
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(GooglePlayApplication.getContext(), R.layout.adapter_home, null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);
		
		//设置数据
		AppInfo appInfo = list.get(position);
		holder.tv_name.setText(appInfo.getName());
		holder.rb_stars.setRating(appInfo.getStars());
		holder.tv_size.setText(Formatter.formatFileSize(GooglePlayApplication.getContext(), appInfo.getSize()));
		holder.tv_des.setText(appInfo.getDes());
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.getIconUrl(), holder.iv_icon, ImageLoaderOptions.list_options);
		
//		convertView.measure(0, 0);
//		ViewHelper.setTranslationY(convertView, convertView.getMeasuredHeight());
		ViewHelper.setRotationX(convertView, 60);
		ViewPropertyAnimator.animate(convertView).rotationX(0).setDuration(600).setInterpolator(new OvershootInterpolator(4)).start();
//		ViewPropertyAnimator.animate(convertView).translationY(0).setDuration(500).setStartDelay(position*10).start();
		
		return convertView;
	}
	
	
	static class HomeHolder{
		ImageView iv_icon;
		TextView tv_name,tv_size,tv_des;
		RatingBar rb_stars;
		
		public HomeHolder(View convertView){
			iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			tv_des = (TextView) convertView.findViewById(R.id.tv_des);
			rb_stars = (RatingBar) convertView.findViewById(R.id.rb_stars);
		}
		
		public static HomeHolder getHolder(View convertView){
			HomeHolder homeHolder = (HomeHolder) convertView.getTag();
			if(homeHolder==null){
				homeHolder = new HomeHolder(convertView);
				convertView.setTag(homeHolder);
			}
			return homeHolder;
		}
	}
}
