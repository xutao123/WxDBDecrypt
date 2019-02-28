package com.wx.decrypt.network.okhttp.request;

import com.wx.decrypt.network.util.GsonUtil;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author xutao
 */
public class PostJsonRequest extends OkHttpRequest {
    private Map<String, String> params = null;
    public PostJsonRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int id) {
        super(url, tag, params, headers, id);
        this.params = params;
    }

    @Override
    public RequestBody buildRequestBody() {
        String params = GsonUtil.gsonBuild().toJson(this.params);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, params);
        return body;
    }

    @Override
    public Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
