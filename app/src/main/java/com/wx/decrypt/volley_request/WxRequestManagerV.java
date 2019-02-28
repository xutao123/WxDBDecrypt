package com.wx.decrypt.volley_request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.network.volley.IRequestManager;
import com.wx.decrypt.network.volley.RequestManager;
import com.wx.decrypt.network.volley.error.ErrorHandlerFactory;
import com.wx.decrypt.network.volley.error.VolleyErrorListener;
import com.wx.decrypt.request.dto.WxInfo;

public class WxRequestManagerV implements IRequestManager {

    private static WxRequestManagerV mInstance;

    public static WxRequestManagerV getInstance() {
        if (mInstance == null) {
            mInstance = new WxRequestManagerV();
            ErrorHandlerFactory.addErrorHandler(new BookErrorHandler());
        }
        return mInstance;
    }

    public int doUploadWxInfo(String url, WxInfo info, Response.Listener<String> listener, VolleyErrorListener errorListener) {
        WxInfoRequest request = new WxInfoRequest(url, info, listener, errorListener);
        return postRequest(request);
    }

    @Override
    public int postRequest(Request<?> request) {
        return RequestManager.getInstance().postRequest(request);
    }

    @Override
    public void cancel(int id) {
        RequestManager.getInstance().cancel(id);
    }

    @Override
    public void cancelAll() {
        RequestManager.getInstance().cancelAll();
    }
}
