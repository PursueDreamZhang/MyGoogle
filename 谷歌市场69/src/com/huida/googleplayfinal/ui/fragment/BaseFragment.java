package com.huida.googleplayfinal.ui.fragment;

import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.LogUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
	
	protected ContentPage contentPage;//注意修饰符不能是private
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(contentPage==null){
//			LogUtil.e(this, "onCreateView : 创建contentPage");
			contentPage = new ContentPage(getActivity()) {
				@Override
				public Object loadData() {
					//方法递归：StackOverFlow
					return requestData();
				}
				@Override
				public View createSuccessView() {
					return getSuccessView();
				}
			};
		}else {
//			LogUtil.e(this, "onCreateView : 复用contentPage-"+contentPage.getParent().getClass().getSimpleName());
			//干掉它爹
			CommonUtil.removeSelfFromParent(contentPage);
		}
		return contentPage;
	}
	/**
	 * 由于无法帮子类实现createSuccessView，所以每个子类需要自己实现
	 * @return
	 */
	protected abstract View getSuccessView();
	/**
	 * 每个子类去实现加载数据的方法
	 * @return
	 */
	protected abstract Object requestData();
}
