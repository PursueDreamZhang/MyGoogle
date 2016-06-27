package com.huida.googleplayfinal.manager;

import java.io.File;

import com.huida.googleplayfinal.bean.AppInfo;

/**
 * 定义DownloadInfo，用于封装每个下载任务的相关数据
 * @author Administrator
 *
 */
public class DownloadInfo {
	private int id;//唯一标识，用于存取
	private int state;//下载状态
	private long currentLength;//已经下载的长度
	private long size;//总长度
	private String downloadUrl;//下载地址
	
	private String path;//apk文件的保存路径
	
	/**
	 * 根据AppInfo对象快速创建DownloadInfo
	 * @param appInfo
	 * @return
	 */
	public static DownloadInfo create(AppInfo appInfo){
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setId(appInfo.getId());
		downloadInfo.setSize(appInfo.getSize());
		downloadInfo.setDownloadUrl(appInfo.getDownloadUrl());
		downloadInfo.setState(DownloadManager.STATE_NONE);//设置为未下载的状态
		
		// /mnt/sdcard/com.itheima.googleplay/download/有缘网.apk
		downloadInfo.setPath(DownloadManager.DOWNLOAD_DIR+File.separator+appInfo.getName()+".apk");
		
		return downloadInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCurrentLength() {
		return currentLength;
	}

	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
