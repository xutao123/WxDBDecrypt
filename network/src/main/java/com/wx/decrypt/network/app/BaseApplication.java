package com.wx.decrypt.network.app;

import android.app.Application;
import android.util.Log;

/**
 * 该Application应该放在Framework module中，未了简便，放在这里
 */
public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    protected static BaseApplication sBaseApp;

    public static <T extends BaseApplication> T getInstance() {
        if (sBaseApp == null) {
            Log.e(TAG, "sBaseApp not create or be terminated!");
        }
        return (T)sBaseApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sBaseApp = this;
    }
}
