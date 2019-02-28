package com.wx.decrypt.request.dto;

import com.wx.decrypt.network.util.GsonUtil;

/**
 * @author xutao
 */
public class ChatInfo implements NoProguard {

    String msgId;
    String talker;
    String content;
    /**
     *
     */
    String type;
    String createTime;
    String realImagePath;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRealImagePath() {
        return realImagePath;
    }

    public void setRealImagePath(String realImagePath) {
        this.realImagePath = realImagePath;
    }

    public String getJsonString() {
        return GsonUtil.gsonBuild().toJson(this);
    }
}
