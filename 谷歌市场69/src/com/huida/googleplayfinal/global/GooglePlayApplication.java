package com.huida.googleplayfinal.global;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class GooglePlayApplication extends Application{
	private static Context context;
	private static Handler mainHandler;//主线程的handler
	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化公共变量
		context = this;
		mainHandler = new Handler();
		
//		new Thread(){
//			public void run() {
//				//如果在子线程创建handler必须创建Looper对象
//				Looper.prepare();
//				Looper.loop();//开启轮询器
//				mainHandler = new Handler();
//			};
//		}.start();
		
		initImageLoader(this);//初始化ImageLoader
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
//		  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();//不会在内存中缓存多个大小的图片
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//为了保证图片名称唯一
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		//内存缓存大小默认是：app可用内存的1/8
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
//		ImageLoader.getInstance().init( ImageLoaderConfiguration.createDefault(this));
	}
	
	public static Handler getHandler(){
		return mainHandler;
	}
	
	public static Context getContext(){
		return context;
	}
}
