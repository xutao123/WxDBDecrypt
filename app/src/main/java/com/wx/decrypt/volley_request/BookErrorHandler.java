package com.wx.decrypt.volley_request;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.wx.decrypt.network.volley.error.IErrorHandler;


public class BookErrorHandler implements IErrorHandler {
    @Override
    public VolleyError handlerError(int sequence, String url, int code, String message, JsonElement results) {

        if (!TextUtils.isEmpty(url)) {
            if (url.equalsIgnoreCase("https://api.douban.com/v2/book/1016300")) {
                System.out.println("handle error");
            }
        }

        return null;
    }
}
