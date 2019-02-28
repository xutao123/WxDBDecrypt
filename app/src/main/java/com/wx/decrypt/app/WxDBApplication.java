package com.wx.decrypt.app;

import android.content.Intent;
import android.util.Log;

import com.wx.decrypt.network.app.BaseApplication;
import com.wx.decrypt.network.okhttp.OkHttpUtils;
import com.wx.decrypt.network.okhttp.https.HttpsUtils;
import com.wx.decrypt.service.DBWorkService;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class WxDBApplication extends BaseApplication {

    private static final String TAG = "WxDBApplication";

    public static WxDBApplication getInstance() {
        if (sBaseApp != null && sBaseApp instanceof WxDBApplication) {
            return (WxDBApplication) sBaseApp;
        } else {
            Log.e(TAG, "sBaseApp not create or be terminated!");
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 配置OkHttp
         */
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        /**
         * 加载数据库lib
         */
        loadDbLib();

        /**
         * 启动Service
         */
        startService(new Intent(this, DBWorkService.class));
    }


    private void loadDbLib() {
        SQLiteDatabase.loadLibs(this);
    }

}
