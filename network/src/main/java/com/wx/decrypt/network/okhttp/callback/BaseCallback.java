package com.wx.decrypt.network.okhttp.callback;

import android.util.Log;

import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.network.util.GsonUtil;

import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;

import okhttp3.Response;

public abstract class BaseCallback<T> extends Callback<BaseResponseData> {

    private static final String TAG = "BaseCallback";

    @Override
    public BaseResponseData parseNetworkResponse(Response response, int i) throws Exception {
        String responseStr = response.body().string();
        String responseBodyString = "";
        if (responseStr != null) {
            //Invalid % sequence
//            responseStr = responseStr.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
//            responseStr = responseStr.replaceAll("\\+", "%2B");
            String urlDecodeStr = URLDecoder.decode(responseStr, "UTF-8");
            responseBodyString = urlDecodeStr;
        }
        BaseResponseData brd = GsonUtil.gsonBuild().fromJson(responseBodyString, BaseResponseData.class);
        if (brd == null) {
            return brd;
        }

        brd.setUrl(response.header("url"));
        //获得泛型类型
        Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        try {
            if (type == String.class || type == Boolean.class) {
                brd.data = (T) brd.result;
            } else {
                brd.data = GsonUtil.gsonBuild().fromJson(brd.result, type);
            }
        } catch (Error e) {
            Log.e(TAG, "Request type = " + this.getClass().getName() + ", error message = " + e
                    .getMessage() + ", response = " + responseStr);
        }
        return brd;
    }
}
