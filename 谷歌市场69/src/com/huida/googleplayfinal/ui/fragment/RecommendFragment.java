package com.huida.googleplayfinal.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.lib.randomlayout.StellarMap;
import com.huida.googleplayfinal.lib.randomlayout.StellarMap.Adapter;
import com.huida.googleplayfinal.util.ColorUtil;
import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.JsonUtil;
import com.huida.googleplayfinal.util.ToastUtil;

public class RecommendFragment extends BaseFragment{

	private StellarMap stellarMap;
	private ArrayList<String> list;
	@Override
	protected View getSuccessView() {
		stellarMap = new StellarMap(getActivity());
		
		int innderPadding = (int) CommonUtil.getDimen(R.dimen.stellar_map_padding);
		//设置子view距离stellarMap的间距
		stellarMap.setInnerPadding(innderPadding, innderPadding, innderPadding, innderPadding);
		
		return stellarMap;
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.Recommend);
		list = (ArrayList<String>) JsonUtil.parseJsonToList(result, new TypeToken<List<String>>(){}.getType());
		if(list!=null){
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					//设置Adapter
					stellarMap.setAdapter(new StellarMapAdapter());
					
					stellarMap.setGroup(0, true);//设置默认显示第0组的数据
					
					//注意：x和y的值不要设置过大或者过小，差不多意思一下
					stellarMap.setRegularity(15, 15);//设置x和y方向上子view的分布的密度
				}
			});
		}
		
		return list;
	}
	
	class StellarMapAdapter implements Adapter{
		/**
		 * 返回多少组数据
		 * @return
		 */
		@Override
		public int getGroupCount() {
			return 3;
		}
		/**
		 * 每组多少个数据
		 * @param group
		 * @return
		 */
		@Override
		public int getCount(int group) {
			return 11;
		}

		/**
		 * 返回每个view对象
		 * @param group  当前组
		 * @param position  当前组中的position
		 * @param convertView
		 * @return
		 */
		@Override
		public View getView(int group, int position, View convertView) {
			final TextView textView = new TextView(getActivity());
//			LogUtil.e(this, "group: "+group+  " position: "+position);
			//1.根据group和group总的position计算出对应在list中的位置
			int listPosition = group*getCount(group) + position;
			textView.setText(list.get(listPosition));
			textView.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
			//2.设置随机字体大小
			Random random = new Random();
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, random.nextInt(8)+14);//14-21
			//3.上色，设置随机颜色
			textView.setTextColor(ColorUtil.randomColor());
			
			//设置点击事件
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.showToast(textView.getText().toString());
				}
			});
			
			return textView;
		}
		
		/**
		 * 并没有什么卵用
		 * @param group
		 * @param degree
		 * @return
		 */
		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return 0;
		}
		/**
		 * 缩放完成后下一组加载哪一组的数据
		 * @param group  当前组
		 * @param isZoomIn
		 * @return
		 */
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			//0->1->2->0
			return (group+1)%getGroupCount();
		}
		
	}
}
