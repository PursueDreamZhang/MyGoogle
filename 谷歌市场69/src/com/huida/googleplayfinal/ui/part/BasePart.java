package com.huida.googleplayfinal.ui.part;

import android.view.View;

public abstract class BasePart<T> {
	/**
	 * 返回模块的view
	 * @return
	 */
	public abstract View getView();
	/**
	 * 处理数据的业务逻辑的方法
	 * @param t
	 */
	public abstract void setData(T t);
}
