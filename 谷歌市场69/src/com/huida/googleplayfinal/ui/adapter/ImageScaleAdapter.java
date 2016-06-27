package com.huida.googleplayfinal.ui.adapter;

import java.util.ArrayList;

import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.lib.photoview.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageScaleAdapter extends PagerAdapter{
	private ArrayList<String> list;
	
	public ImageScaleAdapter(ArrayList<String> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(GooglePlayApplication.getContext());
		
		//绑定ImageView,
		final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+list.get(position), imageView, ImageLoaderOptions.pager_options,new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				//在图片加载完成之后，更新一下所绑定的ImageVIew
				attacher.update();
			}
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});
		
		container.addView(imageView);//将ImageView加入到ViewPager中
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

}
