package com.wx.decrypt.volley_request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.wx.decrypt.network.util.GsonUtil;
import com.wx.decrypt.network.volley.BaseRequest;
import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.network.volley.error.VolleyErrorListener;

import java.io.UnsupportedEncodingException;

public class BookInfoRequest<T> extends BaseRequest<BookDetailInfo> {

    public BookInfoRequest(String url, Response.Listener<BookDetailInfo> listener, VolleyErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<BaseResponseData> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            String contentType = response.headers.get("Content-Type");
            if (contentType != null && contentType.contains("charset")) {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } else {
                parsed = new String(response.data, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        BookDetailInfo brd = GsonUtil.gsonBuild().fromJson(parsed, BookDetailInfo.class);
        if (brd == null) {
            return null;
        }

        BaseResponseData data = new BaseResponseData();
        data.setCode(0);
        data.setSquence(getSequence());
        data.data = brd;

        return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
