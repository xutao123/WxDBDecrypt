package com.wx.decrypt.wxdb;

import android.text.TextUtils;
import android.util.Log;

import com.threekilogram.objectbus.bus.ObjectBus;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;

/**
 * @author xutao
 */
public abstract class BaseDBManager {

    private static final String TAG = "BaseDBManager";

    protected SQLiteDatabase mWxdb;
    protected File mDBFile;
    protected String mPassword;
    private final ObjectBus mTask = ObjectBus.newList();

    public void init(String dbPath, String password) {
        mDBFile = new File(dbPath);
        mPassword = password;
    }

    /**
     * 连接数据库
     */
    public boolean openDb() {
        if (mDBFile == null
                || !mDBFile.exists()
                || TextUtils.isEmpty(mPassword)) {
            Log.e(TAG, "open db failed, db not exists or pwd error");
            return false;
        }

        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            @Override
            public void preKey(SQLiteDatabase database) {}

            @Override
            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");
            }
        };
        if (mWxdb == null || !mWxdb.isOpen()) {
            try {
                mWxdb = SQLiteDatabase.openOrCreateDatabase(mDBFile, mPassword, null, hook);
            } catch (Exception e) {
                Log.e(TAG, "open db " + mDBFile.getName() + " failed, exception:" + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public void closeDB() {
        if (mWxdb != null) {
            mWxdb.close();
        }
    }
}
