package com.wx.decrypt.network.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by xt on 2018/01/21.
 */

public class GsonUtil {
    private static Gson mGson = null;

    public static Gson gsonBuild() {
        if (mGson == null) {
            mGson = new GsonBuilder().create();
        }
        return mGson;
    }
}
