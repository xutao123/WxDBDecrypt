package com.wx.decrypt.network.volley;

public interface Cancelable {

    /**
     * 取消任务
     * @param id 任务ID
     */
    void cancel(int id);

    /**
     * 取消所有任务
     */
    void cancelAll();

}
