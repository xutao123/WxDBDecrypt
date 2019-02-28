package com.wx.decrypt.network.okhttp.builder;


import com.wx.decrypt.network.okhttp.OkHttpUtils;
import com.wx.decrypt.network.okhttp.request.OtherRequest;
import com.wx.decrypt.network.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
