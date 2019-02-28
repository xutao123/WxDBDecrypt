package com.wx.decrypt.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wx.decrypt.network.app.BaseApplication;

import static com.wx.decrypt.util.UrlUtil.ONLINE_HOST;

public class SPHelper {

    private static final String SP_NAME = "WX_DB_SP";

    private static volatile SPHelper mInstance;

    private SPHelper() {}

    public static SPHelper getInstance() {
        if (mInstance == null) {
            synchronized(SPHelper.class) {
                if (mInstance == null) {
                    mInstance = new SPHelper();
                }
            }
        }
        return mInstance;

    }

    private SharedPreferences getSharedPreferences() {
        return BaseApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 用于存储获取设置的域名
     */
    private static final String GET_ROOT_DOMAIN_PATH = "get_root_domain_path";

    public void setRootDomainPath(String domainPath) {
        getSharedPreferences().edit().putString(GET_ROOT_DOMAIN_PATH, domainPath).commit();
    }

    public String getRootDomainPath() {
        return getSharedPreferences().getString(GET_ROOT_DOMAIN_PATH, ONLINE_HOST);
    }

    /**
     * 用于存储是否可以主动上传微信信息
     */
    private static final String CAN_AUTO_UPLOAD_WX_INFO = "can_auto_upload_wx_info";

    public void setCanAutoUploadWxInfo(boolean canAutoUploadWxInfo) {
        getSharedPreferences().edit().putBoolean(CAN_AUTO_UPLOAD_WX_INFO, canAutoUploadWxInfo);
    }

    public boolean getCanAutoUploadWxInfo() {
        return getSharedPreferences().getBoolean(CAN_AUTO_UPLOAD_WX_INFO, true);
    }
}
