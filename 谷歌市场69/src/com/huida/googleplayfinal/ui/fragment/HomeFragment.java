package com.huida.googleplayfinal.ui.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.bean.Home;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.ui.activity.AppDetailActivity;
import com.huida.googleplayfinal.ui.adapter.HomeAdapter;
import com.huida.googleplayfinal.ui.adapter.HomeHeaderAdapter;
import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.JsonUtil;

public class HomeFragment extends BaseFragment{
	private HomeAdapter homeAdapter;
	private ArrayList<AppInfo> list = new ArrayList<AppInfo>();
	private PullToRefreshListView refreshListView;
	private ListView listView;
	private Home home;
	private ViewPager viewPager;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
			
			handler.sendEmptyMessageDelayed(0, 2015);
		};
	};
	
	@Override
	protected View getSuccessView() {
		initRefreshListView();//初始化RefreshListView
		initHomeHeader();//初始化headerView
		
		homeAdapter = new HomeAdapter(list);
		listView.setAdapter(homeAdapter);
		
		//设置item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),AppDetailActivity.class);
//				LogUtil.e(this, "position: "+position);
				intent.putExtra("packageName", list.get(position-2).getPackageName());
				startActivity(intent);
			}
		});
		return refreshListView;
	}
	/**
	 * 初始化header布局
	 */
	private void initHomeHeader(){
		View headerView = View.inflate(getActivity(), R.layout.layout_home_header, null);
		viewPager = (ViewPager) headerView.findViewById(R.id.homePager);
		//1.动态根据图片的宽高比设置viewPager的高度
		int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		//根据宽高比计算出高度，2.65=width/height
		float height = width/2.65f;
		LayoutParams params = viewPager.getLayoutParams();
		params.height = (int) height;
		viewPager.setLayoutParams(params);
		
		listView.addHeaderView(headerView);
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
		String result = HttpHelper.get(Url.Home + list.size());
		home = JsonUtil.parseJsonToBean(result, Home.class);
		list.addAll(home.getList());
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				//只有第一页的数据才有大图的url,
				if(home.getPicture()!=null && home.getPicture().size()>0){
					//表明有轮播大图的数据
					HomeHeaderAdapter headerAdapter = new HomeHeaderAdapter(home.getPicture());
					viewPager.setAdapter(headerAdapter);//给ViewPager设置Adapter
//					headerAdapter.notifyDataSetChanged()://注意，此方法无效
					
					//默认给ViewPager选中比较大的item
					viewPager.setCurrentItem(home.getPicture().size()*100000);
				}
				
				//在主线程中更新adapter
				homeAdapter.notifyDataSetChanged();
				//PullToRefreshListView完成刷新
				refreshListView.onRefreshComplete();
			}
		});
		
		
		return home;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		handler.sendEmptyMessageDelayed(0, 2015);//延时发送消息
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		handler.removeCallbacksAndMessages(null);
	}
	
	private Home parseJson(String json){
		if(TextUtils.isEmpty(json))return null;
		
		Home home = new Home();
		try {
			JSONObject jsonObject = new JSONObject(json);
			//1.解析picture字段
			JSONArray pictureArray = jsonObject.getJSONArray("picture");
			ArrayList<String> picture = new ArrayList<String>();
			for (int i = 0; i < pictureArray.length(); i++) {
				String string = pictureArray.getString(i);
				picture.add(string);
			}
			home.setPicture(picture);
			
			//2.解析list字段
			JSONArray listArray = jsonObject.getJSONArray("list");
			ArrayList<AppInfo> list = new ArrayList<AppInfo>();
			for (int i = 0; i < listArray.length(); i++) {
				JSONObject object = listArray.getJSONObject(i);
				AppInfo appInfo = new AppInfo();
				appInfo.setDes(object.getString("des"));
				appInfo.setDownloadUrl(object.getString("downloadUrl"));
				appInfo.setIconUrl(object.getString("iconUrl"));
				appInfo.setId(object.getInt("id"));
				appInfo.setName(object.getString("name"));
				appInfo.setPackageName(object.getString("packageName"));
				appInfo.setSize(object.getLong("size"));
				appInfo.setStars((float) object.getDouble("stars"));
				
				list.add(appInfo);
			}
			home.setList(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return home;
	}
}
