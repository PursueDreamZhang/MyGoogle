package com.huida.googleplayfinal.ui.adapter;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.CategoryInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryAdapter extends BasicAdapter<Object>{

	public CategoryAdapter(ArrayList<Object> list) {
		super(list);
	}
	
	//1.定义不同的item类型
	private final int ITEM_TITLE = 0;//title类型的item
	private final int ITEM_INFO = 1;//info类型的item
	@Override
	public int getViewTypeCount() {
		return 2;//2种item类型
	}
	/**
	 * 返回指定position的item是什么类型的
	 */
	@Override
	public int getItemViewType(int position) {
		Object object = list.get(position);
		if(object instanceof String){
			return ITEM_TITLE;//title类型的item
		}else {
			return ITEM_INFO;//info类型的item
		}
//		return super.getItemViewType(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemViewType = getItemViewType(position);
		switch (itemViewType) {
		case ITEM_TITLE:
			if(convertView==null){
				convertView = View.inflate(GooglePlayApplication.getContext(), R.layout.adapter_category_title, null);
			}
			TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			String title = (String) list.get(position);
			tv_title.setText(title);
			break;
		case ITEM_INFO:
			if(convertView==null){
				convertView = View.inflate(GooglePlayApplication.getContext(), R.layout.adapter_category_info, null);
			}
			CategoryInfoHolder infoHolder = CategoryInfoHolder.getHolder(convertView);
			CategoryInfo info = (CategoryInfo) list.get(position);
			
			infoHolder.tv_name1.setText(info.getName1());
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info.getUrl1(), infoHolder.iv_image1, ImageLoaderOptions.list_options);
			//显示第二个和第三个的时候可能为空，所以需要判断
			if(!TextUtils.isEmpty(info.getName2())){
				//需要显示的时候要设置为可见，因为下面的会复用上面的，如果不设置将会仍然不可见
				infoHolder.ll_info2.setVisibility(View.VISIBLE);
				
				infoHolder.tv_name2.setText(info.getName2());
				ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info.getUrl2(), infoHolder.iv_image2, ImageLoaderOptions.list_options);
			}else {
				//隐藏ll_info2
				infoHolder.ll_info2.setVisibility(View.INVISIBLE);
			}
			
			if(!TextUtils.isEmpty(info.getName3())){
				//需要显示的时候要设置为可见，因为下面的会复用上面的，如果不设置将会仍然不可见
				infoHolder.ll_info3.setVisibility(View.VISIBLE);
				
				infoHolder.tv_name3.setText(info.getName3());
				ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info.getUrl3(), infoHolder.iv_image3, ImageLoaderOptions.list_options);
			}else {
				//隐藏ll_info3
				infoHolder.ll_info3.setVisibility(View.INVISIBLE);
			}
			break;
		}
		return convertView;
	}

	static class CategoryInfoHolder{
		ImageView iv_image1,iv_image2,iv_image3;
		TextView tv_name1,tv_name2,tv_name3;
		LinearLayout ll_info2,ll_info3;
		public CategoryInfoHolder(View convertView){
			iv_image1 = (ImageView) convertView.findViewById(R.id.iv_image1);
			iv_image2 = (ImageView) convertView.findViewById(R.id.iv_image2);
			iv_image3 = (ImageView) convertView.findViewById(R.id.iv_image3);
			tv_name1 = (TextView) convertView.findViewById(R.id.tv_name1);
			tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
			tv_name3 = (TextView) convertView.findViewById(R.id.tv_name3);
			ll_info2 = (LinearLayout) convertView.findViewById(R.id.ll_info2);
			ll_info3 = (LinearLayout) convertView.findViewById(R.id.ll_info3);
		}
		
		public static CategoryInfoHolder getHolder(View convertView){
			CategoryInfoHolder categoryInfoHolder = (CategoryInfoHolder) convertView.getTag();
			if(categoryInfoHolder==null){
				categoryInfoHolder = new CategoryInfoHolder(convertView);
				convertView.setTag(categoryInfoHolder);
			}
			return categoryInfoHolder;
		}
	}
}
