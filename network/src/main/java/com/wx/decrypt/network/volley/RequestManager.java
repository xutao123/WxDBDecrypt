package com.wx.decrypt.network.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wx.decrypt.network.app.BaseApplication;

public class RequestManager implements IRequestManager{

    private static final String TAG = "RequestManager";
    private static RequestManager sRequestManager;
    private boolean mIsQueueStopped = false;
    private RequestQueue mRequestQueue = null;

    public static RequestManager getInstance() {
        if (sRequestManager == null) {
            synchronized (RequestManager.class) {
                if (sRequestManager == null) {
                    sRequestManager = new RequestManager();
                }
            }
        }
        startRequestQueue();
        return sRequestManager;
    }

    private RequestManager() {
        mRequestQueue = Volley.newRequestQueue(BaseApplication.getInstance());
        if (mRequestQueue != null && mIsQueueStopped) {
            mRequestQueue.start();
            mIsQueueStopped = false;
        }
    }

    private static void startRequestQueue() {
        if (sRequestManager != null && sRequestManager.mRequestQueue != null && sRequestManager.mIsQueueStopped) {
            synchronized (RequestManager.class) {
                if (sRequestManager.mIsQueueStopped) {
                    sRequestManager.mRequestQueue.start();
                    sRequestManager.mIsQueueStopped = false;
                }
            }
        }
    }

    /**
     * 发送请求
     * @param request
     * @return
     */
    @Override
    public int postRequest(Request<?> request) {
        if (mRequestQueue != null) {
            return mRequestQueue.add(request).getSequence();
        }
        return 0;
    }

    @Override
    public void cancel(final int id) {
        if (id <= 0 || mRequestQueue == null) {
            return;
        }
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {
                try {
                    return request.getSequence() == id;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public void cancelAll() {
        cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void cancelAll(RequestQueue.RequestFilter filter) {
        if (mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue.cancelAll(filter);
            mIsQueueStopped = true;
        }
    }
}
