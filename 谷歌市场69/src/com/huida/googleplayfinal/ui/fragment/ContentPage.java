package com.huida.googleplayfinal.ui.fragment;

import java.util.List;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.util.CommonUtil;


/**
 * 管理界面加载的功能，根据不同的state显示对应的VIew
 * @author Administrator
 *
 */
public abstract class ContentPage extends FrameLayout{
	//生成父类的构造方法：alt+shift+s->c
	public ContentPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initContentPage();
	}
	public ContentPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initContentPage();
	}
	public ContentPage(Context context) {
		super(context);
		initContentPage();
	}

	//定义4种状态常量
	enum PageState{
		STATE_LOADING(0),//加载中的状态
		STATE_SUCCESS(1),//加载成功的状态
		STATE_ERROR(2),//加载失败的状态
		STATE_EMPTY(3);//加载数据为空的状态
		private int value;
		PageState(int value){
			this.value = value;
		}
		public int getValue(){
			return value;
		}
	}
	
	//每个界面的默认状态是加载中
	private PageState mState = PageState.STATE_LOADING;
	private View loadingView;//加载中的View
	private View errorView;//加载失败的View
	private View emptyView;//加载数据为空的View
	private View successView;//加载成功的View
	
	/**
	 * 初始化COntentPage
	 */
	private void initContentPage(){
		//1.往ContentPage中添加4个状态对应的View
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		if(loadingView==null){
			loadingView = View.inflate(getContext(), R.layout.page_loading, null);
		}
		addView(loadingView,params);
		
		if(errorView==null){
			errorView = View.inflate(getContext(), R.layout.page_error, null);
			Button btn_reload = (Button) errorView.findViewById(R.id.btn_reload);
			btn_reload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//1.先重置为loading状态，
					mState = PageState.STATE_LOADING;
					showPage();
					//2.重新加载数据，然后刷新page
					loadDataAndRefreshPage();
				}
			});
		}
		addView(errorView,params);
		
		if(emptyView==null){
			emptyView = View.inflate(getContext(), R.layout.page_empty, null);
		}
		addView(emptyView,params);
		
		//添加成功的View
		if(successView==null){
			successView = createSuccessView();
		}
		if(successView!=null){
			addView(successView,params);
		}else {
			throw new IllegalArgumentException("The method createSuccessView() can not return null!");
		}
		
		//2.根据当前state，显示对应的View
		showPage();
		
		//3.然后加载数据，并刷新VIew
		loadDataAndRefreshPage();
	}
	/**
	 * 根据对应的state显示对应的View
	 */
	private void showPage(){
		loadingView.setVisibility(View.INVISIBLE);
		successView.setVisibility(View.INVISIBLE);
		errorView.setVisibility(View.INVISIBLE);
		emptyView.setVisibility(View.INVISIBLE);
		
		switch (mState.getValue()) {
		case 0://加载中
			loadingView.setVisibility(View.VISIBLE);
			break;
		case 1://加载成功
			successView.setVisibility(View.VISIBLE);
			break;
		case 2://加载失败
			errorView.setVisibility(View.VISIBLE);
			break;
		case 3://加载数据为空
			emptyView.setVisibility(View.VISIBLE);
			break;
		}
	}
	/**
	 * 加载数据，然后刷新page
	 */
	public void loadDataAndRefreshPage(){
		
		new Thread(){
			public void run() {
				//模拟请求服务器的耗时操作
				SystemClock.sleep(1500);//睡1.5秒
				
				//1.得到请求回来的数据
				Object data = loadData();
				
				//2.根据data判断对应的状态,并赋值给当前的state
				mState = checkData(data);
				
				//3.根据新的state，刷新Page
				CommonUtil.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						//在主线程更新UI
						showPage();
					}
				});
			};
		}.start();
		
	}
	
	/**
	 * 根据data判断对应的state
	 * @param data
	 * @return
	 */
	private PageState checkData(Object data){
		if(data==null){
			return PageState.STATE_ERROR;//加载数据失败的状态
		}else {
			if(data instanceof List){
				List list = (List) data;
				if(list.size()==0){
					//说明本次请求成功，但是木有数据了
					return PageState.STATE_EMPTY;//加载数据为空的状态
				}else {
					//说明服务器返回的是列表数据
					return PageState.STATE_SUCCESS;//加载数据成功的状态
				}
			}else {
				//就是java bean
				return PageState.STATE_SUCCESS;//加载数据成功的状态
			}
		}
	}
	
	/**
	 * 创建成功的View，由于每个界面的SuccessView不一样，所以应该有每个界面自己
	 * 提供和实现
	 * @return
	 */
	public abstract View createSuccessView();
	
	/**
	 * 加载数据，由于我们只关心请求回来的数据，并不关心加载数据的过程，所以每个子类只需要实现该方法
	 * @return
	 */
	public abstract Object loadData();
	
}
