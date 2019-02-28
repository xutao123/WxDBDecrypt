package com.wx.decrypt.volley_request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.wx.decrypt.network.volley.IRequestManager;
import com.wx.decrypt.network.volley.RequestManager;
import com.wx.decrypt.network.volley.UpLoadImageRequest;
import com.wx.decrypt.network.volley.error.ErrorHandlerFactory;
import com.wx.decrypt.network.volley.error.VolleyErrorListener;
import com.wx.decrypt.network.volley.file.UploadImageFile;

import java.util.ArrayList;

public class BookRequestManager implements IRequestManager {

    private static BookRequestManager mInstance;

    public static BookRequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new BookRequestManager();
            ErrorHandlerFactory.addErrorHandler(new BookErrorHandler());
        }
        return mInstance;
    }

    public int doGetBookInfo(String url, Response.Listener<BookDetailInfo> listener, VolleyErrorListener errorListener) {
        BookInfoRequest request = new BookInfoRequest(url, listener, errorListener);
        return postRequest(request);
    }

    public int doUploadImg(Response.Listener<String> listener, VolleyErrorListener errorListener) {
        ArrayList<UploadImageFile> list = new ArrayList();
        list.add(new UploadImageFile("uploadimg", "splash.jpg", "/Android/data/com.netease.edu.study.enterprise/files/xcache/splash_image/splash.jpg"));
//        http://thyrsi.com/t6/672/
        UpLoadImageRequest request = new UpLoadImageRequest(list, "", listener, errorListener);
        return postRequest(request);
    }

    @Override
    public int postRequest(Request<?> request) {
        return RequestManager.getInstance().postRequest(request);
    }

    @Override
    public void cancel(int id) {
        RequestManager.getInstance().cancel(id);
    }

    @Override
    public void cancelAll() {
        RequestManager.getInstance().cancelAll();
    }
}
