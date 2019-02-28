package com.wx.decrypt.network.volley.error;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandlerFactory {

    private static List<IErrorHandler> mHandlers = new ArrayList<>();

    public static void addErrorHandler(IErrorHandler handler) {
        mHandlers.add(handler);
    }

    public static VolleyError handleError(int sequence, String url, int code, String message, JsonElement results) {
        if (mHandlers != null && !mHandlers.isEmpty()) {
            for (IErrorHandler handler : mHandlers) {
                VolleyError error = handler.handlerError(sequence, url, code, message, results);
                if (error != null) {
                    return error;
                }
            }
        }
        return null;
    }

}
