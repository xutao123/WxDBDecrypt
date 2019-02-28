package com.wx.decrypt.network.volley;

import com.android.volley.Request;

public interface IRequestManager extends Cancelable {

    int postRequest(Request<?> request);

}
