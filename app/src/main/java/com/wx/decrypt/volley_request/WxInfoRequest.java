package com.wx.decrypt.volley_request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.network.volley.BaseRequest;
import com.wx.decrypt.network.volley.error.VolleyErrorListener;
import com.wx.decrypt.request.dto.WxInfo;

import java.util.HashMap;
import java.util.Map;

public class WxInfoRequest <T> extends BaseRequest<BaseResponseData> {
    WxInfo info;
    public WxInfoRequest(String url, WxInfo info,  Response.Listener<BaseResponseData> listener, VolleyErrorListener errorListener) {
        super(url, listener, errorListener);
        this.info = info;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("cellphoneNum", info.getPhoneNum());
        map.put("uuid", info.getUuid());
        map.put("wechatId", info.getWxId());
        map.put("wechatAlias", info.getAlias());
        map.put("headImg", info.getHeadImg());
        map.put("nickName", info.getNickName());
        return map;
    }
}
