package com.wx.decrypt.network.volley.error;

import com.android.volley.VolleyError;

public interface VolleyErrorListener {
    void onErrorResponse(int sequence, String url, VolleyError error);
}
