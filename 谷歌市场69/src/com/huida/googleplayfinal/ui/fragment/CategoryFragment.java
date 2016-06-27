package com.huida.googleplayfinal.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.huida.googleplayfinal.R;
import com.huida.googleplayfinal.bean.Category;
import com.huida.googleplayfinal.bean.CategoryInfo;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.ui.adapter.CategoryAdapter;
import com.huida.googleplayfinal.util.CommonUtil;
import com.huida.googleplayfinal.util.JsonUtil;

public class CategoryFragment extends BaseFragment{

	private ListView listView;
	//用来统一存放title和CategoryInfo的
	private ArrayList<Object> list = new ArrayList<Object>();
	private CategoryAdapter categoryAdapter;
	@Override
	protected View getSuccessView() {
		listView = (ListView) View.inflate(getActivity(), R.layout.listview, null);
		categoryAdapter = new CategoryAdapter(list);
		listView.setAdapter(categoryAdapter);
		return listView;
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.Category);
		ArrayList<Category> categories = (ArrayList<Category>) JsonUtil.parseJsonToList(result, new TypeToken<List<Category>>(){}.getType());
		
		if(categories!=null){
			//对数据进行处理，将categories中的category类的title和infos放入同一个集合
			for(Category category : categories){
				//1.将title放入list中
				String title = category.getTitle();
				list.add(title);
				//2.将infos的数据放入集合中
				ArrayList<CategoryInfo> infos = category.getInfos();
				list.addAll(infos);
	//			list.add(infos);//注意：千万不要这样写
			}
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					categoryAdapter.notifyDataSetChanged();
				}
			});
		}
		
		return categories;
	}
}
