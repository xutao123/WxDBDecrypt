package com.wx.decrypt.wxdb;

import android.util.Log;

import net.sqlcipher.Cursor;

import static com.wx.decrypt.constant.DBConstant.REAL_IMAGE_PATH_PREFIX;

/**
 *
 * WxFileIndex.db管理类
 * 微信图片存储在/storage/emulated/0/tencent/MicroMsg/图片路径
 * 图片路径来自WxFileIndex.db数据库的WxFileIndex2表中的path中
 * 可以通过EnMicroMsg.db中的message表得到imgPath,通过这个字段，对应ImgInfo2表的thumbImgPath字段，查找bigImgPath,将bigImgPath和
 * /storage/emulated/0/tencent/MicroMsg/拼接就是真正的图片地址
 *
 * @author xutao
 */
public class FileIndexDBManager extends BaseDBManager {

    private static final String TAG = "FileIndexDBManager";
    private static volatile FileIndexDBManager mInstance;

    public static FileIndexDBManager getInstance() {
        if (mInstance == null) {
            synchronized (FileIndexDBManager.class) {
                if (mInstance == null) {
                    mInstance = new FileIndexDBManager();
                }
            }
        }
        return mInstance;
    }

    public String getRealImagePath(String imgPath) {
        String realPath = "";
        if (!openDb()) {
            Log.e(TAG, "getRealImagePath failed , openDb failed");
            return realPath;
        }
        Cursor cursor = null;
        try {
            cursor = mWxdb.rawQuery(
                    "select path from WxFileIndex2 where path like '%" + imgPath + "'",
                    null);
            while (cursor != null && cursor.moveToNext()) {
                realPath = REAL_IMAGE_PATH_PREFIX + cursor.getString(cursor.getColumnIndex("path"));
                Log.e(TAG, "realImagePath=====" + realPath);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e("openDb", "读取数据库信息失败" + e.toString());
        } finally {
            return realPath;
        }
    }
}
