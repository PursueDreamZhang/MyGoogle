package com.huida.googleplayfinal.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 根据指定的宽高比(ratio)来动态设置自身的高度
 * @author Administrator
 *
 */
public class RatioImageView extends ImageView{
	private float ratio = 0;//宽高比
	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//将布局文件中的属性的值赋值给ratio变量
		ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/com.huida.googleplayfinal", "ratio", 0f);
	}

	public RatioImageView(Context context) {
		super(context);
	}
	
	/**
	 * 设置宽高比
	 * @param ratio
	 */
	public void setRatio(float ratio){
		this.ratio = ratio;
	}
	
	/**
	 * MeasureSpec: 测量规则，由size和mode组成
	 * size: 就是具体的像素值
	 * mode: 测量模式，有3种
	 * 		MeasureSpec.AT_MOST ： 对应的是wrap_content
	 * 		MeasureSpec.EXACTLY ： 对应的是具体dp值，match_parent
	 * 		MeasureSpec.UNSPECIFIED ： 未指定，未定义的，一般用在listview测量adapter的view中
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int width = MeasureSpec.getSize(widthMeasureSpec);//获取ImageView的宽度
		
		if(ratio!=0){
			float height = width/ratio;//根据宽高比计算出对应的高度
			//帮ImageView重新构造高度的测量规则
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
