package com.huida.googleplayfinal.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
	private static ThreadPoolManager mInstance = new ThreadPoolManager();
	private int corePoolSize;//核心线程池的数量，表示能同时执行的线程数量
	private int maximumPoolSize;//最大线程池数量,它包含了corePoolSize;
	private int keepAliveTime = 1;//存活时间,表示maximumPoolSize中的线程存活时间
	private TimeUnit unit = TimeUnit.HOURS;//时间单位
	private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();//缓冲队列,里面的任务处于等待状态
	private ThreadPoolExecutor executor;
	public static ThreadPoolManager getInstance(){
		return mInstance;
	}
	private ThreadPoolManager(){
		corePoolSize = Runtime.getRuntime().availableProcessors()*2 + 1;//处理器的核数*2+1
		maximumPoolSize = corePoolSize;//这里maximumPoolSize的值不能为0
		executor = new ThreadPoolExecutor(
				corePoolSize, //3
				maximumPoolSize, //5
				keepAliveTime, 
				unit, 
				workQueue, 
				Executors.defaultThreadFactory(), //创建线程的工厂
				new ThreadPoolExecutor.AbortPolicy());
	}
	/**
	 * 执行任务
	 * @param runnable
	 */
	public void execute(Runnable runnable){
		if(runnable==null)return;
		
		executor.execute(runnable);
	}
	/**
	 * 移除任务
	 * @param runnable
	 */
	public void remove(Runnable runnable){
		if(runnable==null)return;
		
		executor.remove(runnable);
	}
}
