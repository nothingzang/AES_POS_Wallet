package com.example.aes_pos_wallet.utils;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ExecutorsUtils {

	public static void setThreadPoolRunning(boolean threadPoolRunning) {
		ExecutorsUtils.threadPoolRunning = threadPoolRunning;
	}
	public static boolean isThreadPoolRunning() {
		return threadPoolRunning;
	}
	private static boolean threadPoolRunning = true;

	private ExecutorsUtils(){}
	private static final ExecutorService mThreadPool = ExecutorsUtils.newCachedThreadPool(5);
	public static ExecutorService getThreadPoolInstance(){
		return mThreadPool;
	}

	private static final ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(5);
	public static ExecutorService getFixedThreadPoolInstance(){
		return mFixedThreadPool;
	}

	private static ThreadPoolExecutor newCachedThreadPool(int corePoolSize) {
		if(corePoolSize < 0)
			return null;
		return new ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE,
                3L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
	public static boolean isMainThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}
}
