package com.huida.googleplayfinal.ui.part;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.bean.SafeInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.global.ImageLoaderOptions;
import com.huida.googleplayfinal.http.Url;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AppSafePart extends BasePart<AppInfo> implements OnClickListener{
	private View view;
	private ImageView iv_safe_image1,iv_safe_image2,iv_safe_image3,iv_safe_arrow;
	private ImageView iv_safe_des1,iv_safe_des2,iv_safe_des3;
	private TextView tv_safe_des1,tv_safe_des2,tv_safe_des3;
	private LinearLayout ll_safe_des2,ll_safe_des3;
	private RelativeLayout rl_safe;
	private LinearLayout ll_safe_des_container;
	private int maxHeight;
	public AppSafePart(){
		view = View.inflate(GooglePlayApplication.getContext(), R.layout.layout_detail_app_safe, null);
		iv_safe_image1 = (ImageView) view.findViewById(R.id.iv_safe_image1);
		iv_safe_image2 = (ImageView) view.findViewById(R.id.iv_safe_image2);
		iv_safe_image3 = (ImageView) view.findViewById(R.id.iv_safe_image3);
		iv_safe_arrow = (ImageView) view.findViewById(R.id.iv_safe_arrow);
		iv_safe_des1 = (ImageView) view.findViewById(R.id.iv_safe_des1);
		iv_safe_des2 = (ImageView) view.findViewById(R.id.iv_safe_des2);
		iv_safe_des3 = (ImageView) view.findViewById(R.id.iv_safe_des3);
		tv_safe_des1 = (TextView) view.findViewById(R.id.tv_safe_des1);
		tv_safe_des2 = (TextView) view.findViewById(R.id.tv_safe_des2);
		tv_safe_des3 = (TextView) view.findViewById(R.id.tv_safe_des3);
		
		ll_safe_des2 = (LinearLayout) view.findViewById(R.id.ll_safe_des2);
		ll_safe_des3 = (LinearLayout) view.findViewById(R.id.ll_safe_des3);
		
		rl_safe = (RelativeLayout) view.findViewById(R.id.rl_safe);
		ll_safe_des_container = (LinearLayout) view.findViewById(R.id.ll_safe_des_container);
		
		rl_safe.setOnClickListener(this);
		
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setData(AppInfo appInfo) {
		ArrayList<SafeInfo> safeList = appInfo.getSafe();
		//因为safeList的size是1-3，所以显示的时候需要判断
		SafeInfo info1 = safeList.get(0);
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info1.getSafeUrl(), iv_safe_image1, ImageLoaderOptions.list_options);
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info1.getSafeDesUrl(), iv_safe_des1, ImageLoaderOptions.list_options);
		tv_safe_des1.setText(info1.getSafeDes());
		
		//显示第2个和第3个需要判断
		if(safeList.size()>1){
			//表明有第2个safeINfo
			SafeInfo info2 = safeList.get(1);
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info2.getSafeUrl(), iv_safe_image2, ImageLoaderOptions.list_options);
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info2.getSafeDesUrl(), iv_safe_des2, ImageLoaderOptions.list_options);
			tv_safe_des2.setText(info2.getSafeDes());
		}else {
			ll_safe_des2.setVisibility(View.GONE);
		}
		
		if(safeList.size()>2){
			//表明有第3个safeINfo
			SafeInfo info3 = safeList.get(2);
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info3.getSafeUrl(), iv_safe_image3, ImageLoaderOptions.list_options);
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+info3.getSafeDesUrl(), iv_safe_des3, ImageLoaderOptions.list_options);
			tv_safe_des3.setText(info3.getSafeDes());
		}else {
			ll_safe_des3.setVisibility(View.GONE);
		}
		
		//1.记录ll_safe_des_container的原来的高度
		ll_safe_des_container.measure(0, 0);
		maxHeight = ll_safe_des_container.getMeasuredHeight();
		
		//2.让ll_safe_des_container的高度变为0
		ll_safe_des_container.getLayoutParams().height = 0;
		ll_safe_des_container.requestLayout();//让view的布局params生效
		
		
		//3.给rl_safe增加平移动画
		//事先让rl_safe移动到左边
//		ViewPropertyAnimator.animate(rl_safe).translationXBy(-rl_safe.getMeasuredWidth()).setDuration(0).start();
//		rl_safe.setTranslationX(-rl_safe.getMeasuredWidth());//3.0之后的方法
		ViewHelper.setTranslationX(rl_safe, -rl_safe.getMeasuredWidth());//直接更改view的translationX的值
		
		ViewPropertyAnimator.animate(rl_safe).translationXBy(rl_safe.getMeasuredWidth())
		.setDuration(500)
		.setInterpolator(new OvershootInterpolator(4))
		.setStartDelay(600)
		.start();
	}
	
	private boolean isExtendSafe = false;//是否展开安全描述
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_safe:
			ValueAnimator valueAnimator;
			if(isExtendSafe){
				valueAnimator = ValueAnimator.ofInt(maxHeight,0);
			}else {
				valueAnimator = ValueAnimator.ofInt(0,maxHeight);
			}
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					//在该方法回调中能够获取动画的当前值
					int animatedValue = (Integer) animator.getAnimatedValue();
					ll_safe_des_container.getLayoutParams().height = animatedValue;
					ll_safe_des_container.requestLayout();//让view的布局params生效
				}
			});
			valueAnimator.setDuration(350);
			valueAnimator.addListener(new SafeDesAnimListener());
			valueAnimator.start();
			break;
		}
	}
	/**
	 * SafeDes模块伸展动画
	 * @author Administrator
	 *
	 */
	class SafeDesAnimListener implements AnimatorListener{
		@Override
		public void onAnimationCancel(Animator arg0) {
		}
		@Override
		public void onAnimationEnd(Animator arg0) {
			isExtendSafe = !isExtendSafe;
//			iv_safe_arrow.setBackgroundResource(isExtendSafe?R.drawable.arrow_up:R.drawable.arrow_down);
			
		}
		@Override
		public void onAnimationRepeat(Animator arg0) {
		}
		@Override
		public void onAnimationStart(Animator arg0) {
			ViewPropertyAnimator.animate(iv_safe_arrow).rotationBy(180).setDuration(350).start();
			
//			ViewPropertyAnimator.animate(iv_safe_image1).translationXBy(25)
//			.setDuration(300)
//			.setInterpolator(new OvershootInterpolator(4))//先超过一点，再弹回来
//			.setInterpolator(new BounceInterpolator())//像球落地一样，
//			.setInterpolator(new CycleInterpolator(4))//来回晃动，
//			.start();
//			ViewPropertyAnimator.animate(iv_safe_image1).scaleXBy(0.2f).setDuration(300).start();
//			ViewPropertyAnimator.animate(iv_safe_image1).scaleYBy(0.2f).setDuration(300).start();
			
			
		}
	}

}
