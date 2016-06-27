package com.huida.googleplayfinal.util;

import com.huida.googleplayfinal.global.GooglePlayApplication;

import android.widget.Toast;

public class ToastUtil {
	private static Toast toast;
	/**
	 * 强大的吐司，可以连续弹，不用等上一个显示
	 * @param text
	 */
	public static void showToast(String text){
		if(toast==null){
			toast = Toast.makeText(GooglePlayApplication.getContext(), text, 0);
		}
		toast.setText(text);//将文本设置为toast
		toast.show();
	}
}
