package com.wx.decrypt.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int QUEUE_INIT_CAPACITY = 20;

    private static ThreadPoolManager poolManager;
    private static ExecutorService executorService;

    private ThreadPoolManager() {
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                20L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(QUEUE_INIT_CAPACITY),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    public static ThreadPoolManager getInstance() {
        if (poolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (poolManager == null) {
                    poolManager = new ThreadPoolManager();
                }
            }
        }
        return poolManager;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }

    public void submit(Callable task) {
        executorService.submit(task);
    }

}
