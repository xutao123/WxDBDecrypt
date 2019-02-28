package com.wx.decrypt.util;

import android.text.TextUtils;

/**
 * @author xutao
 */
public class UrlUtil {

    private static final String HTTP ="http://";
    private static final String HTTPS ="https://";

    public static final String TEST_HOST = "yzstest.study.163.com";
    public static final String ONLINE_HOST = "yzsonline.study.163.com";


    public static String getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        return HTTP + SPHelper.getInstance().getRootDomainPath() + url;
    }

}
