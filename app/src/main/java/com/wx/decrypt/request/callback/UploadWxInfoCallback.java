package com.wx.decrypt.request.callback;

import com.wx.decrypt.network.okhttp.callback.BaseCallback;
import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.util.SPHelper;

import okhttp3.Call;

import static com.wx.decrypt.constant.NetWorkConstrant.RESPONSE_SUCCESS;

/**
 * @author xutao
 */
public class UploadWxInfoCallback extends BaseCallback<Boolean> {
    @Override
    public void onError(Call call, Exception e, int i) {
        System.out.println("upload wxinfo error");
    }

    @Override
    public void onResponse(BaseResponseData responseData, int i) {
        if (responseData.getCode() == RESPONSE_SUCCESS) {
            SPHelper.getInstance().setCanAutoUploadWxInfo(false);
        }
    }
}
