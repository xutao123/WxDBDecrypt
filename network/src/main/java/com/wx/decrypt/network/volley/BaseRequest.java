package com.wx.decrypt.network.volley;

import android.support.v4.util.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.network.util.GsonUtil;
import com.wx.decrypt.network.volley.error.ErrorHandlerFactory;
import com.wx.decrypt.network.volley.error.VolleyErrorListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * 普通表单提交
 * @param <T>
 */
public class BaseRequest<T> extends Request<BaseResponseData> {

    private static final String TAG = "BaseRequest";

    private static final int TIMEOUT_10S = 10*1000;
    private static final int MAX_RETRY_NUM = 1;

    public String mUrl;
    private Response.Listener<T> mListener;
    private VolleyErrorListener mErrorListener;

    public BaseRequest(String url, Response.Listener<T> listener, VolleyErrorListener errorListener) {
        super(Method.POST, url, null);

        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_10S, MAX_RETRY_NUM, 1f));
        mUrl = url;
        mListener = listener;
        mErrorListener = errorListener;
        generalQueryUrl();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new ArrayMap<>();
        headers.put("app-type", "Android");
        headers.put("device-id", android.os.Build.SERIAL);
        return headers;
    }

    @Override
    protected Response<BaseResponseData> parseNetworkResponse(NetworkResponse response) {
        BaseResponseData brd = getBaseResponseData(response);
        if (brd == null) {
            return Response.error(new VolleyError("服务器返回数据为空"));
        }
        if (brd.getCode() != 0) {
            System.out.println(getUrl() + " code " + brd.getCode());
            return Response.error(
                    ErrorHandlerFactory.handleError(
                            getSequence(), mUrl, brd.getCode(), brd.getMessage(), brd.result));
        } else {
            if (brd.data == null) {
                System.out.println(getUrl() + " brd.data is null AND result is not allow null");
                return Response.error(new VolleyError("服务器返回数据为空"));
            } else {
                return Response.success(brd, HttpHeaderParser.parseCacheHeaders(response));
            }
        }
    }

    protected BaseResponseData getBaseResponseData(NetworkResponse response) {
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

        BaseResponseData brd = GsonUtil.gsonBuild().fromJson(parsed, BaseResponseData.class);
        if (brd == null) {
            return brd;
        }

        brd.setSquence(getSequence());
        brd.setUrl(mUrl);
        //获得泛型类型
        Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        try {
            if (type == String.class) {
                brd.data = (T) brd.result;
            } else {
                brd.data = GsonUtil.gsonBuild().fromJson(brd.result, type);
            }
        } catch (Error e) {
            System.out.println("Request type = " + this.getClass().getName() + ", error message = " + e
                    .getMessage() + ", response = " + parsed);
        }
        return brd;
    }

    @Override
    protected void deliverResponse(BaseResponseData response) {
        if (response == null) {
            postErrorCode(-1);
            return;
        } else if (response.getCode() != 0) {
            postErrorCode(response.getCode());
            return;
        }
        if (mListener != null) {
            mListener.onResponse((T) response.data);
            mListener = null;
            mErrorListener = null;
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json;charset=" + getParamsEncoding();
    }

    public void postErrorCode(int code) {
        //可在deliverError之前加上一个ErrorHandler调用来处理error，在回调给回调接口
        deliverError(new VolleyError("Common error"));
    }

    @Override
    public void deliverError(VolleyError error) {
        if (mErrorListener != null) {

//            BaseResponseData brd = null;
//            Cache.Entry entry = this.getCacheEntry();
//            if (entry != null) {
//                brd = getBaseResponseData(new NetworkResponse(entry.data, entry.responseHeaders));
//            }

            mErrorListener
                    .onErrorResponse(getSequence(), mUrl, error);
            mListener = null;
            mErrorListener = null;
        }
    }

}
