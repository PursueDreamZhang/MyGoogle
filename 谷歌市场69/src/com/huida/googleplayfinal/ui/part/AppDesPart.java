package com.huida.googleplayfinal.ui.part;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class AppDesPart extends BasePart<AppInfo> implements OnClickListener{
	private View view;
	private TextView tv_des,tv_author;
	private ImageView iv_des_arrow;
	private int minHeight,maxHeight;
	private ScrollView scrollView;
	public AppDesPart(ScrollView scrollView){
		this.scrollView = scrollView;
		
		view = View.inflate(GooglePlayApplication.getContext(), R.layout.layout_detail_app_des, null);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
		tv_author = (TextView) view.findViewById(R.id.tv_author);
		iv_des_arrow = (ImageView) view.findViewById(R.id.iv_des_arrow);
		
		view.setOnClickListener(this);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setData(AppInfo appInfo) {
		tv_des.setText(appInfo.getDes());
		tv_author.setText(appInfo.getAuthor());
		
		//1.首先，获取tv_des5行时候的高度和最大的高度
		tv_des.setMaxLines(5);
		//当TextView的内容的高度大于控件的高度，测量之后的值是不准确的
//		tv_des.measure(0, 0);
//		int minHeight = tv_des.getMeasuredHeight();
		//在tv_des执行完layout之后再获取宽高
		tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			/**
			 * 该方法会在父View的所有子View执行完layout之后回调
			 */
			@Override
			public void onGlobalLayout() {
				//由于只要父View中某个字view执行layout，都再次回调该方法，所以用完之后立即移除监听器
				tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//				tv_des.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				minHeight = tv_des.getHeight();//获取5行时候的高度
				
				//2.获取最大高度,首先让tv_des显示全部的文本
				tv_des.setMaxLines(Integer.MAX_VALUE);//让textView完全显示文本
				tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						maxHeight = tv_des.getHeight();//获取tv_des最大的高度
						
						//3.让tv_Des默认显示5行时候的高度
						tv_des.getLayoutParams().height = minHeight;
						tv_des.requestLayout();
					}
				});
				
			}
		});
	}
	private boolean isExtendDes = false;//是否展开描述区域
	@Override
	public void onClick(View v) {
		if(v==view){
			ValueAnimator valueAnimator;
			if(isExtendDes){
				valueAnimator = ValueAnimator.ofInt(maxHeight,minHeight);
			}else {
				valueAnimator = ValueAnimator.ofInt(minHeight,maxHeight);
			}
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					//获取动画的值，设置给view的高度
					int animatedValue = (Integer) animator.getAnimatedValue();
					tv_des.getLayoutParams().height = animatedValue;
					tv_des.requestLayout();
					
					//只有在tv_des的高度增高的时候才需要scrollView滚动
					if(!isExtendDes){
						scrollView.scrollBy(0, 1000);//向上滚动
					}
				}
			});
			valueAnimator.setDuration(350);
			valueAnimator.addListener(new DesAnimListener());
			valueAnimator.start();
		}
	}

	class DesAnimListener implements AnimatorListener{
		@Override
		public void onAnimationCancel(Animator arg0) {
		}
		@Override
		public void onAnimationEnd(Animator arg0) {
			isExtendDes = !isExtendDes;
		}
		@Override
		public void onAnimationRepeat(Animator arg0) {
		}
		@Override
		public void onAnimationStart(Animator arg0) {
			ViewPropertyAnimator.animate(iv_des_arrow).rotationBy(180).setDuration(350).start();
		}
	}
}
