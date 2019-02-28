package com.wx.decrypt.wxdb;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.wx.decrypt.util.PhoneUtil;
import com.wx.decrypt.request.dto.WxInfo;
import net.sqlcipher.Cursor;

import java.util.concurrent.Executor;

/**
 * EnMicroMsg数据库的管理类
 * @author xutao
 */
public class EnMicroMsgDBManager extends BaseDBManager {

    private static final String TAG = "EnMicroMsgDBManager";

    private static volatile EnMicroMsgDBManager mInstance;

    private EnMicroMsgDBManager(){}

    public static EnMicroMsgDBManager getInstance() {
        if (mInstance == null) {
            synchronized(EnMicroMsgDBManager.class) {
                if (mInstance == null) {
                    mInstance = new EnMicroMsgDBManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取wx个人信息
     * phoneNumber在userinfo2表中
     * userinfo表：
     * @return
     */
    public WxInfo getWxInfo() {
        if (!openDb()) {
            Log.e(TAG, "getWxInfo failed , openDb failed");
            return null;
        }

        String uuid = PhoneUtil.getAdbDeviceId();
        if (TextUtils.isEmpty(uuid)) {
            Log.e(TAG, "获取Uuid失败");
            return null;
        }
        String wxId = getValueFromUserInfo("select value from userinfo where id = 2 and type = 3");
        if (TextUtils.isEmpty(wxId)) {
            Log.e(TAG, "获取wxId失败");
            return null;
        }

        WxInfo info = new WxInfo();
        info.setUuid(uuid);
        info.setWxId(wxId);
        info.setNickName(getValueFromUserInfo("select value from userinfo where id = 4 and type = 3"));
        info.setAlias(getValueFromUserInfo("select value from userinfo where id = 42 and type = 3"));
        info.setPhoneNum(getValueFromUserInfo("select value from userinfo where id = 6 and type = 3"));
        info.setHeadImg(getHeadImg(info.getWxId()));
        return info;
    }

    private String getValueFromUserInfo(String queryString) {
        String value = "";
        Cursor cursor = null;
        try {
            cursor = mWxdb.rawQuery(queryString, null);
            while (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex("value"));
            }
            closeCursor(cursor);
        } catch (Exception e){
            if (cursor != null) {
                cursor.close();
            }
            closeDB();
        } finally {
            return value;
        }
    }

    /**
     * 获得头像信息
     * username:原始微信号
     * reserved1:大图图标url
     * reserved2:小图图标url
     */
    public String getHeadImg(String username) {
        String imgPath = "";
        if (!openDb()) {
            Log.e(TAG, "getHeadImg failed , openDb failed");
            return imgPath;
        }

        Cursor cursor = null;
        try {
            cursor = mWxdb.rawQuery(
                    "select * from img_flag where username = ?",
                    new String[]{username});
            while (cursor != null && cursor.moveToNext()) {
                imgPath = cursor.getString(cursor.getColumnIndex("reserved1"));
                Log.e("openDb", "userName====" + username + "    headurl=====" + imgPath);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e(TAG, "读取数据库信息失败" + e.toString());
        } finally {
            return imgPath;
        }
    }

    /**
     *  查询聊天信息
     *  这里查出的聊天信息包含用户主动删除的信息
     */
    public void getMessageDate(@NonNull String lastSearchTimes, String endSearchTime) {
        if (!openDb()) {
            Log.e(TAG, "getMessageData failed , openDb failed");
            return;
        }
        Cursor cursor = null;
        try {
            String withoutTalker = getValueFromUserInfo("select value from userinfo where id = 2 and type = 3");
            /**
             * type = 1:文字
             * type = 3:图片 THUMBNAIL_DIRPATH://th_fc50067c3986bff07342b7d6132fc115
             * type = 34:语音
             * type = 43:视频
             */
            if (TextUtils.isEmpty(endSearchTime)) {
                cursor = mWxdb.rawQuery("select * from message where type in(1,3) and talker != ? and createTime > ?",
                        new String[]{withoutTalker, lastSearchTimes});
            } else {
                cursor = mWxdb.rawQuery("select * from message where type in(1,3) and talker != ? and createTime > ? and createTime < ?",
                        new String[]{withoutTalker, lastSearchTimes, endSearchTime});
            }
            Log.e("openDb", "聊天记录分割线=====================================================================================");
            while (cursor != null && cursor.moveToNext()) {
                //talker是聊天对象的微信id
                String talker = cursor.getString(cursor.getColumnIndex("talker"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String thumbImgName = cursor.getString(cursor.getColumnIndex("imgPath"));
                String realImagePath = "";
                if (!TextUtils.isEmpty(thumbImgName)) {
                    String bigImageName = getBigImgName(thumbImgName);
                    if (!TextUtils.isEmpty(bigImageName)) {
                        realImagePath = FileIndexDBManager.getInstance().getRealImagePath(bigImageName);
                    }
                }
                /**
                 * 微信处在后台时，图片url:/storage/emulated/0/tencent/MicroMsg/e0164fa76b2ced4cf1f94fcb9ac7157c/image2/SE/RV/SERVERID://1378469704288381864
                 */
                Log.e("openDb", "聊天对象微信号====" + talker
                        + "    内容=====" + content+ "    时间=====" + createTime + "   图片===="+realImagePath);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e(TAG, "读取数据库信息失败" + e.toString());
        }
    }

    public String getBigImgName(String thumbImgName) {
        String bigImgName = "";
        if (!openDb()) {
            Log.e(TAG, "getBigImgName failed , openDb failed");
            return bigImgName;
        }

        Cursor cursor = null;
        try {
            cursor = mWxdb.rawQuery("select bigImgPath from ImgInfo2 where thumbImgPath = ?", new String[]{thumbImgName});
            while (cursor != null && cursor.moveToNext()) {
                bigImgName = cursor.getString(cursor.getColumnIndex("bigImgPath"));
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e(TAG, "读取数据库信息失败" + e.toString());
        } finally {
            return bigImgName;
        }
    }

    /**
     * 获取群聊成员列表
     */
    public void getChatRoomDate() {
        if (!openDb()) {
            Log.e(TAG, "getChatRoomDate failed , openDb failed");
            return;
        }

        Cursor cursor = null;
        try {
            cursor = mWxdb.rawQuery("select * from chatroom ", null);
            Log.e("openDb", "群组信息记录分割线=====================================================================================");
            while (cursor.moveToNext()) {
                String roomowner = cursor.getString(cursor.getColumnIndex("roomowner"));
                String chatroomname = cursor.getString(cursor.getColumnIndex("chatroomname"));
                String memberlist = cursor.getString(cursor.getColumnIndex("memberlist"));
                Log.e("openDb", "群主====" + roomowner + "    群组成员id=====" + memberlist+ "    群id=====" + chatroomname);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e("openDb", "读取数据库信息失败" + e.toString());
        }
    }

    /**
     * 获取当前用户的微信所有联系人
     *
     * username原始微信号/群号，和message中的talker就是使用的这个
     * alias是自己设置的微信号，如bhpg128
     * nikename:用户昵称，如原来足球是圆的/群名称
     *
     * 7未设置微信号的好友；
     */
    public void getRecontactDate() {
        if (!openDb()) {
            Log.e(TAG, "getRecontactDate failed , openDb failed");
            return;
        }

        Cursor cursor = null;
        try {
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            cursor = mWxdb.rawQuery(
                    "select * from rcontact where verifyFlag = 0 and type != 4 and type != 2 and nickname != ''",
                    null);
            while (cursor.moveToNext()) {
                String userName = cursor.getString(cursor.getColumnIndex("username"));
                String nickName = cursor.getString(cursor.getColumnIndex("nickname"));
                Log.e("openDb", "userName====" + userName + "    nickName=====" + nickName);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e("openDb", "读取数据库信息失败" + e.toString());
        }
    }

    /**
     * fmessage_conversation表
     * 获取好友请求
     */
    public void getStrangerFriends() {
        if (!openDb()) {
            Log.e(TAG, "getRecontactDate failed , openDb failed");
            return;
        }

        Cursor cursor = null;
        try {
            //isNew=1,还未验证的好友
            cursor = mWxdb.rawQuery(
                    "select * from fmessage_conversation where isNew = 1",null);
            while (cursor.moveToNext()) {
                String wxId = cursor.getString(cursor.getColumnIndex("contentFromUsername"));
                String nickName = cursor.getString(cursor.getColumnIndex("contentNickname"));
                Log.e("openDb", "userName====" + wxId + "    nickName=====" + nickName);
            }
            closeCursor(cursor);
        } catch (Exception e) {
            closeCursor(cursor);
            closeDB();
            Log.e("openDb", "读取数据库信息失败" + e.toString());
        }
    }

}
