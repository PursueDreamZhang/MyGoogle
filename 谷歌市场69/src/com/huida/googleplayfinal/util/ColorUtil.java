package com.huida.googleplayfinal.util;

import java.util.Random;

import android.graphics.Color;

public class ColorUtil {
	/**
	 * 随机生成颜色
	 * @return
	 */
	public static int randomColor(){
		Random random = new Random();
		//为了生成的颜色不至于太黑或太白，对三原色的值进行限定
		int red = random.nextInt(150)+50;//50-199
		int green = random.nextInt(150)+50;//50-199
		int blue = random.nextInt(150)+50;//50-199
		return Color.rgb(red, green, blue);//混合出一种新的颜色  
	}
}
