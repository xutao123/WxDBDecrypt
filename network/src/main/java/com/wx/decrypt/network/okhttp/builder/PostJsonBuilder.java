package com.wx.decrypt.network.okhttp.builder;

import com.wx.decrypt.network.okhttp.request.PostJsonRequest;
import com.wx.decrypt.network.okhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xutao
 */
public class PostJsonBuilder extends OkHttpRequestBuilder<PostJsonBuilder> implements HasParamsable {

    @Override
    public PostJsonBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostJsonBuilder addParams(String key, String val) {
        if (this.params == null)
        {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostJsonRequest(url, tag, params, headers, id).build();
    }
}
