package com.wx.decrypt.network.oldokhttp;

public interface NetCallBack {
    //网络请求失败，没连网
    void onNetFailure();

    //网络请求成功
    void onSuccess(String result);

    //网络请求成功，后台服务器有误
    void onError(String result, String errorMessage);
}
