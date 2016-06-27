package com.huida.googleplayfinal.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.ui.adapter.AppAdapter;
import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.JsonUtil;

public class GameFragment extends BaseFragment{
	private PullToRefreshListView refreshListView;
	private ListView listView;
	
	private AppAdapter adapter;
	private ArrayList<AppInfo> list = new ArrayList<AppInfo>();
	@Override
	protected View getSuccessView() {
		
		initRefreshListView();//初始化PullToRefreshListView
		
		//给ListView设置Adapter
		adapter = new AppAdapter(list);
		listView.setAdapter(adapter);
		
		return refreshListView;
	}

	/**
	 * 初始化RefreshListView
	 */
	private void initRefreshListView(){
		refreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
		refreshListView.setMode(Mode.BOTH);//两边都可以拉
		//1.设置刷新监听
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(refreshView.getCurrentMode()==Mode.PULL_FROM_START){
					//下拉
					CommonUtil.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							//PullToRefreshListView完成刷新
							refreshListView.onRefreshComplete();
						}
					});
				}else {
					//上拉,加载下一页的数据
					contentPage.loadDataAndRefreshPage();
				}
			}
		});
		
		//2.获取ListView对象，设置Adapter
		listView = refreshListView.getRefreshableView();
		listView.setDividerHeight(0);//隐藏listview的divider
		listView.setSelector(android.R.color.transparent);//隐藏listview的selector
	}
	
	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.Game+list.size());
		ArrayList<AppInfo> appInfos = (ArrayList<AppInfo>) JsonUtil.parseJsonToList(result, new TypeToken<List<AppInfo>>(){}.getType());
		
		if(appInfos!=null){
			list.addAll(appInfos);//数据更新了
			
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
					
					//让refreshListView完成刷新
					refreshListView.onRefreshComplete();
				}
			});
		}
		
		
		return appInfos;
	}
}
