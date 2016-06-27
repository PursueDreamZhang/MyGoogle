package com.huida.googleplayfinal.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * 专门创建fragment对象
 * @author Administrator
 *
 */
public class FragmentFactory {
	/**
	 * 根据position创建对应的fragment
	 * @param position
	 * @return
	 */
	public static Fragment create(int position){
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new AppFragment();
			break;
		case 2:
			fragment = new GameFragment();
			break;
		case 3:
			fragment = new SubjectFragment();
			break;
		case 4:
			fragment = new RecommendFragment();
			break;
		case 5:
			fragment = new CategoryFragment();
			break;
		case 6:
			fragment = new HotFragment();
			break;
		}
		return fragment;
	}
}
