package com.wx.decrypt.network.volley.error;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;

public interface IErrorHandler {
    VolleyError handlerError(final int sequence, final String url, final int code, final String message, JsonElement results);
}
