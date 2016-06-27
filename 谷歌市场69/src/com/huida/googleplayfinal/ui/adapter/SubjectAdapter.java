package com.huida.googleplayfinal.ui.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.Subject;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.ui.view.RatioImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SubjectAdapter extends BasicAdapter<Subject>{

	public SubjectAdapter(ArrayList<Subject> list) {
		super(list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(GooglePlayApplication.getContext(), R.layout.adapter_subject, null);
		}
		SubjectHolder holder = SubjectHolder.getHolder(convertView);
		//设置数据
		Subject subject = list.get(position);
		holder.tv_des.setText(subject.getDes());
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+subject.getUrl(), holder.iv_image, ImageLoaderOptions.list_options);
		
		return convertView;
	}
	
	static class SubjectHolder{
		RatioImageView iv_image;
		TextView tv_des;
		public SubjectHolder(View convertView){
			iv_image = (RatioImageView) convertView.findViewById(R.id.iv_image);
			tv_des = (TextView) convertView.findViewById(R.id.tv_des);
		}
		
		public static SubjectHolder getHolder(View convertView){
			SubjectHolder subjectHolder = (SubjectHolder) convertView.getTag();
			if(subjectHolder==null){
				subjectHolder = new SubjectHolder(convertView);
				convertView.setTag(subjectHolder);
			}
			return subjectHolder;
		}
	}

}
