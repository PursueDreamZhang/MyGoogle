package com.huida.googleplayfinal.ui.part;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.ui.activity.AppDetailActivity;
import com.huida.googleplayfinal.ui.activity.ImageScaleActivity;
import com.huida.googleplayfinal.util.CommonUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AppScreenPart extends BasePart<AppInfo>{
	private View view;
	private LinearLayout ll_screen_container;
	
	private AppDetailActivity activity;
	public AppScreenPart(AppDetailActivity activity){
		this.activity = activity;
		
		view = View.inflate(GooglePlayApplication.getContext(), R.layout.layout_detail_app_screen, null);
		ll_screen_container = (LinearLayout) view.findViewById(R.id.ll_screen_container);
	}
	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setData(AppInfo appInfo) {
		int width = (int) CommonUtil.getDimen(R.dimen.screen_image_width);
		int height = (int) CommonUtil.getDimen(R.dimen.screen_image_height);
		int leftMargin = (int) CommonUtil.getDimen(R.dimen.screen_image_margin);
		final ArrayList<String> screen = appInfo.getScreen();
		//由于screen的size是3-5张，所以要动态创建ImageView加入到ll_screen_container
		for (int i = 0; i < screen.size(); i++) {
			ImageView imageView = new ImageView(GooglePlayApplication.getContext());
			LayoutParams params = new LayoutParams(width, height);
			if(i>0){
				//给后面的4张图片都设置左边距
				params.leftMargin = leftMargin;
			}
			imageView.setLayoutParams(params);
			
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+screen.get(i), imageView, ImageLoaderOptions.pager_options);
			
			ll_screen_container.addView(imageView);
			
			final int temp = i;
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity,ImageScaleActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putStringArrayListExtra("urlList", screen);
					intent.putExtra("currentItem", temp);
//					intent.putExtra(name, value);
					activity.startActivity(intent);
				}
			});
		}
	}

}
