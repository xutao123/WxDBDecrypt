package com.wx.decrypt.service;

import android.os.FileObserver;
import android.support.annotation.Nullable;

import com.wx.decrypt.wxdb.EnMicroMsgDBManager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xutao
 */
public class DBObserver extends FileObserver {

    private static final long SEARCH_DB_DELAY_TIME_MILLES = 2000L;

    private long mCurrFirstObserverTime = 0L;
    private long mLastSearchDBTime = 0L;
    ScheduledExecutorService mPool = new ScheduledThreadPoolExecutor(3);

    public DBObserver(String path) {
        super(path);
    }

    public DBObserver(String path, int mask) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        switch (event) {
            /**
             * 设置一个时间限制，2s内只响应一次,且在2s之后再去查询数据库
             */
            case FileObserver.MODIFY:
                long currTime = System.currentTimeMillis();

                if ((currTime - mCurrFirstObserverTime) > SEARCH_DB_DELAY_TIME_MILLES) {
                    System.out.println("time:" + (currTime - mCurrFirstObserverTime));
                    mCurrFirstObserverTime = currTime;
                    //做个延时任务，2s后搜索数据库
                    mPool.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            EnMicroMsgDBManager.getInstance().getMessageDate(Long.toString(mLastSearchDBTime), "");
                            mLastSearchDBTime = mCurrFirstObserverTime + SEARCH_DB_DELAY_TIME_MILLES;
                        }
                    }, SEARCH_DB_DELAY_TIME_MILLES, TimeUnit.MILLISECONDS);
                }
                break;
            default:
                break;
        }
    }
}
