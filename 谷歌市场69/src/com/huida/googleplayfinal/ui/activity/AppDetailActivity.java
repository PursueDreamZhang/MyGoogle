package com.huida.googleplayfinal.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.manager.DownloadInfo;
import com.huida.googleplayfinal.manager.DownloadManager;
import com.huida.googleplayfinal.manager.DownloadManager.DownloadObserver;
import com.huida.googleplayfinal.ui.fragment.ContentPage;
import com.huida.googleplayfinal.ui.part.AppDesPart;
import com.huida.googleplayfinal.ui.part.AppInfoPart;
import com.huida.googleplayfinal.ui.part.AppSafePart;
import com.huida.googleplayfinal.ui.part.AppScreenPart;
import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.JsonUtil;

public class AppDetailActivity extends ActionBarActivity implements OnClickListener,DownloadObserver{
	private String packageName;
	private ContentPage contentPage;
	private AppInfo appInfo;
	private LinearLayout ll_part_container;
	private ScrollView scrollview;
	private ProgressBar pb_progress;
	private Button btn_download;
	
	//app info模块的控件
	private AppInfoPart appInfoPart;
	private AppSafePart appSafePart;
	private AppScreenPart appScreenPart;
	private AppDesPart appDesPart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		packageName = getIntent().getStringExtra("packageName");
		//1.初始化ContentPage
		contentPage = new ContentPage(this) {
			@Override
			public Object loadData() {
				return requestData();
			}
			@Override
			public View createSuccessView() {
				View view = View.inflate(AppDetailActivity.this, R.layout.activity_app_detail, null);
				scrollview = (ScrollView) view.findViewById(R.id.scrollview);
				ll_part_container = (LinearLayout) view.findViewById(R.id.ll_part_container);
				pb_progress = (ProgressBar) view.findViewById(R.id.pb_progress);
				btn_download = (Button) view.findViewById(R.id.btn_download);
				//1.初始化app info的模块
				appInfoPart = new AppInfoPart();
				ll_part_container.addView(appInfoPart.getView());
				//2.初始化app safe模块
				appSafePart = new AppSafePart();
				ll_part_container.addView(appSafePart.getView());
				//3.初始化app screen模块
				appScreenPart = new AppScreenPart(AppDetailActivity.this);
				ll_part_container.addView(appScreenPart.getView());
				//4.初始化app des模块
				appDesPart = new AppDesPart(scrollview);
				ll_part_container.addView(appDesPart.getView());
				
				
				btn_download.setOnClickListener(AppDetailActivity.this);
				
				return view;
			}
		};
		//2.将ContentPage作为页面加载管理的类
		setContentView(contentPage);
		
		//3.设置ActionBar
		setActionBar();
		
		//4.注册下载监听器
		DownloadManager.getInstance().registerDownloadObserver(this);
	}
	/**
	 * 设置ActionBar
	 */
	private void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(R.string.app_detail));
		
		//显示home按钮
		actionBar.setDisplayHomeAsUpEnabled(true);//显示ActionBar的home按钮
		actionBar.setDisplayShowHomeEnabled(true);//让home按钮可以被点击
		
	}
	/**
	 * 请求数据
	 * @return
	 */
	private Object requestData(){
		String url = String.format(Url.Detail, packageName);
		String result = HttpHelper.get(url);
		appInfo = JsonUtil.parseJsonToBean(result, AppInfo.class);
		
		if(appInfo!=null){
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					//更新UI
					refreshUI();
				}
			});
		}
		
		return appInfo;
	}
	/**
	 * 刷新UI
	 */
	private void refreshUI(){
		//1.显示app info模块
		appInfoPart.setData(appInfo);
		//2.显示app safe模块
		appSafePart.setData(appInfo);
		//3.显示app screen模块
		appScreenPart.setData(appInfo);
		//4.显示app des模块
		appDesPart.setData(appInfo);
		
		//5.根据下载状态更新文本
		DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
		if(downloadInfo!=null){
			refreshDownloadState(downloadInfo);
		}
	}
	
	/**
	 * 点击ActionBar的home按钮会回调该方法
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			//如果点击的是home按钮的时候才退出
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
			DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
			if(downloadInfo==null){
				//说明从来木有下载过,则直接可以下载
				DownloadManager.getInstance().download(appInfo);
			}else {
				if(downloadInfo.getState()==DownloadManager.STATE_FINISH){
					//执行安装操作
					DownloadManager.getInstance().installApk(appInfo);
				}else if (downloadInfo.getState()==DownloadManager.STATE_DOWNLOADING
						|| downloadInfo.getState()==DownloadManager.STATE_WAITING) {
					//执行暂停操作,注意：等待的任务也是需要暂停的
					DownloadManager.getInstance().pause(appInfo);
				}else if (downloadInfo.getState()==DownloadManager.STATE_PAUSE
						|| downloadInfo.getState()==DownloadManager.STATE_ERROR) {
					//应该下载
					DownloadManager.getInstance().download(appInfo);
				}
			}
			break;
		}
	}
	@Override
	public void onDownloadStateChange(final DownloadInfo downloadInfo) {
		refreshDownloadState(downloadInfo);
	}
	/**
	 * 刷新下载状态
	 */
	private void refreshDownloadState(DownloadInfo downloadInfo){
		if(appInfo==null || appInfo.getId()!=downloadInfo.getId()){
			//说明正在下载的app和当前界面的app不是同一个，
			return;
		}
		switch (downloadInfo.getState()) {
		case DownloadManager.STATE_FINISH:
			btn_download.setText("安装");
			break;
		case DownloadManager.STATE_WAITING:
			btn_download.setText("等待中...");
			break;
		case DownloadManager.STATE_PAUSE:
			btn_download.setBackgroundResource(0);//移除背景
			btn_download.setText("继续下载");
			float fraction = downloadInfo.getCurrentLength()*1f/downloadInfo.getSize();//0.01
			pb_progress.setProgress((int) (fraction*100));
			break;
		case DownloadManager.STATE_ERROR:
			btn_download.setText("出错,重新下载");
			break;
		}
	}
	
	@Override
	public void onDownloadProgressChange(final DownloadInfo downloadInfo) {
		if(appInfo==null || appInfo.getId()!=downloadInfo.getId()){
			//说明正在下载的app和当前界面的app不是同一个，
			return;
		}
		//1.获取下载进度
		float fraction = downloadInfo.getCurrentLength()*1f/downloadInfo.getSize();//0.01
		pb_progress.setProgress((int) (fraction*100));
		//2.移除btn_download的背景色
		btn_download.setBackgroundResource(0);//移除背景
		btn_download.setText(pb_progress.getProgress()+"%");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//当Activity退出的时候，将监听器对象移除
		DownloadManager.getInstance().unregisterDownloadObserver(this);
	}
}
