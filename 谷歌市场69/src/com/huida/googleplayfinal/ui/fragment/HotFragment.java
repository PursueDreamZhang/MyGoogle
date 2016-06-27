package com.huida.googleplayfinal.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.ui.view.FlowLayout;
import com.huida.googleplayfinal.util.ColorUtil;
import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.DrawableUtil;
import com.huida.googleplayfinal.util.JsonUtil;
import com.huida.googleplayfinal.util.ToastUtil;

public class HotFragment extends BaseFragment{

	private FlowLayout flowLayout;
	private ScrollView scrollView;
	private int horizontalPadding,verticalPadding;
	private float radius;
	@Override
	protected View getSuccessView() {
		horizontalPadding = (int) CommonUtil.getDimen(R.dimen.horizontal_text_padding);
		verticalPadding = (int) CommonUtil.getDimen(R.dimen.vertical_text_padding);
		radius = CommonUtil.getDimen(R.dimen.hot_text_radius);
		
		scrollView = new ScrollView(getActivity());
		flowLayout = new FlowLayout(getActivity());//宽高params默认是match
		//设置flowLayout的padding值
		int flowLayoutPadding = (int) CommonUtil.getDimen(R.dimen.flow_layout_padding);
		flowLayout.setPadding(flowLayoutPadding,flowLayoutPadding, flowLayoutPadding, flowLayoutPadding);
		
		scrollView.addView(flowLayout);
		return scrollView;
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.Hot);
		final ArrayList<String> list = (ArrayList<String>) JsonUtil.parseJsonToList(result, new TypeToken<List<String>>(){}.getType());
		
		if(list!=null){
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					//遍历list，往FlowLayout中添加子view
					for (int i = 0; i < list.size(); i++) {
						final TextView textView = new TextView(getActivity());
						textView.setGravity(Gravity.CENTER);
						textView.setText(list.get(i));
						textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						textView.setTextColor(Color.WHITE);
//						textView.setTextColor(ColorUtil.randomColor());
//						textView.setBackgroundColor(ColorUtil.randomColor());
						Drawable normal = DrawableUtil.generateDrawable(ColorUtil.randomColor(), radius);
						Drawable pressed = DrawableUtil.generateDrawable(ColorUtil.randomColor(), radius);
						textView.setBackgroundDrawable(DrawableUtil.generateSelector(pressed, normal));
						textView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
						
						flowLayout.addView(textView);
						
						textView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ToastUtil.showToast(textView.getText().toString());
							}
						});
					}
				}
			});
		}
		
		return list;
	}
}
