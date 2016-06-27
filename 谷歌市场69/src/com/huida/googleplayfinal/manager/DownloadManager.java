package com.huida.googleplayfinal.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.huida.googleplayfinal.bean.AppInfo;
import com.huida.googleplayfinal.global.GooglePlayApplication;
import com.huida.googleplayfinal.http.HttpHelper;
import com.huida.googleplayfinal.http.Url;
import com.huida.googleplayfinal.http.HttpHelper.HttpResult;
import com.huida.googleplayfinal.util.CommonUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;

/**
 * 统一管理下载模块
 * @author Administrator
 *
 */
public class DownloadManager {
	//下载目录:/mnt/sdcard/com.itheima.googleplay/download
	public static String DOWNLOAD_DIR = Environment.getExternalStorageDirectory()+File.separator
			+GooglePlayApplication.getContext().getPackageName()+File.separator+"download";
	//定义6种下载状态常量
	public static final int STATE_NONE = 0;//未下载的状态，初始化状态
	public static final int STATE_DOWNLOADING = 1;//下载中的状态
	public static final int STATE_PAUSE = 2;//暂停的状态
	public static final int STATE_FINISH = 3;//下载完成的状态
	public static final int STATE_ERROR = 4;//下载失败的状态
	public static final int STATE_WAITING = 5;//处于等待的状态
	
	private static DownloadManager mInstance = new DownloadManager();
	public static DownloadManager getInstance(){
		return mInstance;
	}
	//用于存放所有界面的下载监听器
	private ArrayList<DownloadObserver> observerList = new ArrayList<DownloadManager.DownloadObserver>();
	//用于存取每个下载任务对应的downloadInfo，当前的下载信息是保存在内存中的，并没有持久化保存
	private HashMap<Integer, DownloadInfo> downloadInfoMap = new HashMap<Integer, DownloadInfo>();
//	private SparseArray<DownloadInfo> sparseArray;
	//用于存放每个DownloadTask对象，便于暂停的时候取出对应的task对象，及时从线程池中移除，为等待的任务腾出资源
	private HashMap<Integer, DownloadTask> downloadTaskMap = new HashMap<Integer, DownloadManager.DownloadTask>();
	
	private DownloadManager(){
		//初始化下载目录
		File file = new File(DOWNLOAD_DIR);
		if(!file.exists()){
			file.mkdirs();//创建下载目录结构
		}
	}
	/**
	 * 获取downlaodInfo
	 * @param appInfo
	 * @return
	 */
	public DownloadInfo getDownloadInfo(AppInfo appInfo){
		return downloadInfoMap.get(appInfo.getId());
	}
	
	/**
	 * 下载的方法
	 */
	public void download(AppInfo appInfo){
		//1.获取下载信息DownloadInfo,才能有下一步的操作
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo==null){
			downloadInfo = DownloadInfo.create(appInfo);
			downloadInfoMap.put(downloadInfo.getId(), downloadInfo);//将DownloadInfo存放起来
		}
		
		//2.判断当前的state能否进行下载，只有3种状态才能进行下载:none,pause,error
		if(downloadInfo.getState()==STATE_NONE || downloadInfo.getState()==STATE_PAUSE
				|| downloadInfo.getState()==STATE_ERROR){
			//可以进行下载了
			DownloadTask downloadTask = new DownloadTask(downloadInfo);
			downloadTaskMap.put(downloadInfo.getId(), downloadTask);
			
			//将DownloadInfo的state设置等待状态
			downloadInfo.setState(STATE_WAITING);
			notifyDownloadStateChange(downloadInfo);//通知监听器状态改变
			
			//将下载任务交给线程池管理和维护
			ThreadPoolManager.getInstance().execute(downloadTask);
		}
	}
	
	/**
	 * 真正的下载逻辑处理
	 * @author Administrator
	 *
	 */
	class DownloadTask implements Runnable{
		private DownloadInfo downloadInfo;
		public DownloadTask(DownloadInfo downloadInfo) {
			super();
			this.downloadInfo = downloadInfo;
		}
		@Override
		public void run() {
			//3.run方法一执行说明下载任务已经开始了，需要将state设置为下载中
			downloadInfo.setState(STATE_DOWNLOADING);
			notifyDownloadStateChange(downloadInfo);//通知监听器状态改变
			
			//4.进行下载文件: 分2种情况：从头下载，断点下载
			HttpResult httpResult = null;
			File file = new File(downloadInfo.getPath());
			if(!file.exists() || file.length()!=downloadInfo.getCurrentLength()){
				//需要重头下载的情况
				file.delete();//删除无效文件
				downloadInfo.setCurrentLength(0);//重置已经下载的长度
				
				String url = String.format(Url.Download, downloadInfo.getDownloadUrl());
				httpResult = HttpHelper.download(url);
			}else {
				//属于断点下载的情况
				String url = String.format(Url.Break_Download, downloadInfo.getDownloadUrl(),downloadInfo.getCurrentLength());
				httpResult = HttpHelper.download(url);
			}
			
			//5.拿到httpResult后，进行文件的读写
			if(httpResult!=null && httpResult.getInputStream()!=null){
				//说明请求到了服务器的文件数据
				InputStream is = httpResult.getInputStream();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file, true);//往后添加
					byte[] buffer = new byte[1024*8];//8k的缓冲区
					int len = -1;
					while((len=is.read(buffer))!=-1 && downloadInfo.getState()==STATE_DOWNLOADING){
						fos.write(buffer, 0, len);
						fos.flush();
						//更新currentLength
						downloadInfo.setCurrentLength(downloadInfo.getCurrentLength()+len);
						notifyDownloadProgressChange(downloadInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					//下载失败的情况
					downloadInfo.setState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);
					file.delete();//删除无效文件
					downloadInfo.setCurrentLength(0);//重置已经下载的长度
				}finally{
					//关闭流和链接
					httpResult.close();
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				//6.走到这里有几种情况:a.下载完成     b.暂停     c.下载出错
				if(file.length()==downloadInfo.getSize() && downloadInfo.getState()==STATE_DOWNLOADING){
					downloadInfo.setState(STATE_FINISH);//将状态更改为下载完成
					notifyDownloadStateChange(downloadInfo);//通知监听器状态更改
				}else if (downloadInfo.getState()==STATE_PAUSE) {
					notifyDownloadStateChange(downloadInfo);//通知监听器状态更改
				}else if (file.length()!=downloadInfo.getCurrentLength()) {
					//加强判断，增加代码的健壮性
					downloadInfo.setState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);
					file.delete();//删除无效文件
					downloadInfo.setCurrentLength(0);//重置已经下载的长度
				}
			}else {
				//属于下载失败的情况
				downloadInfo.setState(STATE_ERROR);
				notifyDownloadStateChange(downloadInfo);
				file.delete();//删除无效文件
				downloadInfo.setCurrentLength(0);//重置已经下载的长度
			}
			
			//走到末尾，意味着run方法即将结束
			downloadTaskMap.remove(downloadInfo.getId());
		}
		
	}
	
	/**
	 * 通知所有的监听器状态改变了
	 */
	private void notifyDownloadStateChange(final DownloadInfo downloadInfo){
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				for (DownloadObserver observer : observerList) {
					observer.onDownloadStateChange(downloadInfo);
				}
			}
		});
	}
	/**
	 * 通知所有的监听器下载进度改变了
	 */
	private void notifyDownloadProgressChange(final DownloadInfo downloadInfo){
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				for (DownloadObserver observer : observerList) {
					observer.onDownloadProgressChange(downloadInfo);
				}
			}
		});
	}
	
	/**
	 * 暂停的方法
	 */
	public void pause(AppInfo appInfo){
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			//将downloadInfo的状态更改为pause
			downloadInfo.setState(STATE_PAUSE);
			notifyDownloadStateChange(downloadInfo);//通知监听器状态更改
			
			//取出对应的DownloadTask对象，及时从线程池中移除，为缓冲队列的线程腾出资源
			DownloadTask downloadTask = downloadTaskMap.get(downloadInfo.getId());
			ThreadPoolManager.getInstance().remove(downloadTask);
		}
	}
	/**
	 * 安装apk的方法
	 */
	public void installApk(AppInfo appInfo){
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			 /*<action android:name="android.intent.action.VIEW" />
             <category android:name="android.intent.category.DEFAULT" />
             <data android:scheme="content" />
             <data android:scheme="file" />
             <data android:mimeType="application/vnd.android.package-archive" />*/
			//具体的安装操作
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//求创建任务栈
			intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()), "application/vnd.android.package-archive");
			GooglePlayApplication.getContext().startActivity(intent);
		}
	}
	
	/**
	 * 注册一个下载监听器对象
	 * @param observer
	 */
	public void registerDownloadObserver(DownloadObserver observer){
		if(!observerList.contains(observer)){
			observerList.add(observer);
		}
	}
	
	/**
	 * 取消注册下载监听器对象
	 * @param observer
	 */
	public void unregisterDownloadObserver(DownloadObserver observer){
		if(observerList.contains(observer)){
			observerList.remove(observer);
		}
	}
	
	
	
	/**
	 * 下载状态和进度改变的监听器
	 * @author Administrator
	 *
	 */
	public interface DownloadObserver{
		/**
		 * 下载状态改变的回调
		 */
		void onDownloadStateChange(DownloadInfo downloadInfo);
		/**
		 * 下载进度改变的回调
		 */
		void onDownloadProgressChange(DownloadInfo downloadInfo);
	}
	
}
