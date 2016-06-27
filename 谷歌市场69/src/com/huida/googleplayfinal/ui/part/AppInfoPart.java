package com.huida.googleplayfinal.ui.part;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * app info模块的封装
 * @author Administrator
 *
 */
public class AppInfoPart extends BasePart<AppInfo>{
	private View view;
	private ImageView iv_icon;
	private TextView tv_name,tv_download_num,tv_version,tv_date,tv_size;
	private RatingBar rb_stars;

	public AppInfoPart(){
		view = View.inflate(GooglePlayApplication.getContext(), R.layout.layout_detail_app_info, null);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
		tv_version = (TextView) view.findViewById(R.id.tv_version);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_size = (TextView) view.findViewById(R.id.tv_size);
		rb_stars = (RatingBar) view.findViewById(R.id.rb_stars);
	}

	public View getView(){
		return view;
	}
	
	public void setData(AppInfo appInfo){
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.getIconUrl(), iv_icon, ImageLoaderOptions.list_options);
		tv_name.setText(appInfo.getName());
		rb_stars.setRating(appInfo.getStars());
		tv_download_num.setText("下载："+appInfo.getDownloadNum());
		tv_date.setText("日期："+appInfo.getDate());
		tv_version.setText("版本："+appInfo.getVersion());
		tv_size.setText("大小："+Formatter.formatFileSize(GooglePlayApplication.getContext(), appInfo.getSize()));
	}
}
