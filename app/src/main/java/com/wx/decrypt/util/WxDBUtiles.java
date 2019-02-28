package com.wx.decrypt.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.threekilogram.objectbus.bus.ObjectBus;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WxDBUtiles {

  private static SQLiteDatabase mWxdb;
  private static Context mContext;
  private static File mDBFile;
  private static String mPassword;
  private static List<File> mWxDbPathList = new ArrayList<>();
  private static final ObjectBus mTask = ObjectBus.newList();

  private static WxDBUtiles mInstance;

  public static WxDBUtiles getInstance() {
    if (mInstance == null) {
      mInstance = new WxDBUtiles();
    }
    return mInstance;
  }

  public void init(Context context, File dbFile, String password) {
    mContext = context;
    mDBFile = dbFile;
    mPassword = password;
  }

  /**
   * 连接数据库
   */
  public void openWxDb() {
    SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
         @Override
         public void preKey(SQLiteDatabase database) {}

         @Override
         public void postKey(SQLiteDatabase database) {
             database.rawExecSQL("PRAGMA cipher_migrate;");
         }
      };
      //打开数据库连接
      if (mWxdb == null || !mWxdb.isOpen()) {
         mWxdb = SQLiteDatabase.openOrCreateDatabase(mDBFile, mPassword, null, hook);
      }
  }


  public void close() {
    if (mWxdb != null) {
      mWxdb.close();
    }
  }

  /////////////////////////////////////////////////////
  ///////////////参考
  /////////////////////////////////////////////////////

  public void runMessage() {
    mTask.toPool(new Runnable() {
      @Override
      public void run() {
        //子线程
        getMessageDate();
      }
    }).toMain(new Runnable() {
      @Override
      public void run() {
        //主线程
        Toast.makeText(mContext, "聊天信息查询完毕", Toast.LENGTH_LONG).show();
      }
    }).run();
  }

  /**
   * 获取群聊成员列表
   */
  public void runChatRoom() {
       getChatRoomDate();
  }

  /**
   * 获取群聊成员列表
   */
  public void getChatRoomDate() {
    Cursor c1 = null;
    try {
      c1 = mWxdb.rawQuery("select * from chatroom ", null);
      Log.e("openDb", "群组信息记录分割线=====================================================================================");
      while (c1.moveToNext()) {
        String roomowner = c1.getString(c1.getColumnIndex("roomowner"));
        String chatroomname = c1.getString(c1.getColumnIndex("chatroomname"));
        String memberlist = c1.getString(c1.getColumnIndex("memberlist"));
        Log.e("openDb", "群主====" + roomowner + "    群组成员id=====" + memberlist+ "    群id=====" + chatroomname);
      }
      c1.close();
    } catch (Exception e) {
      if (c1 != null) {
        c1.close();
      }
      if (mWxdb != null) {
        mWxdb.close();
      }
      Log.e("openDb", "读取数据库信息失败" + e.toString());
    }
  }

  /**
   * 查询聊天信息
   *  这里查出的聊天信息包含用户主动删除的信息
   *  无心的聊天信息删除不是物理删除，所哟只要不卸载仍然可以查到聊天记录
   */
  public void getMessageDate() {
    if (mWxdb == null || !mWxdb.isOpen()) {
      openWxDb();
    }

    Cursor c1 = null;
    try {
      // 这里只查询文本消息，type=1  图片消息是47，具体信息可以自己测试
      // http://emoji.qpic.cn/wx_emoji/gV159fHh6rYfCMejCAU1wIoP6eywxFMYjaJiaBzPbSjoc6XlTLoMyKQEh4nswfrX5/ （发送表情连接可以拼接的）
      /**
       * 可以指定查询和某个用户的聊天记录，通过createTime > 上次的查询时间来减少查询数据
       *
       * 可以个一段时间来查询一次，将所有用户的信息获取到
       *
       * type = 1:文字
       * type = 3:图片
       */
      c1 = mWxdb.rawQuery("select * from message where type = ? and createTime > ?", new String[] {"1", "1549955008065"});
      Log.e("openDb", "聊天记录分割线=====================================================================================");
      while (c1.moveToNext()) {
        //talker是聊天对象的微信id
        String talker = c1.getString(c1.getColumnIndex("talker"));
        String content = c1.getString(c1.getColumnIndex("content"));
        String createTime = c1.getString(c1.getColumnIndex("createTime"));
        String imgPath = c1.getString(c1.getColumnIndex("imgPath"));
        Log.e("openDb", "聊天对象微信号====" + talker
                + "    内容=====" + content+ "    时间=====" + createTime + "   图片===="+imgPath);
      }
      c1.close();
    } catch (Exception e) {
      if (c1 != null) {
        c1.close();
      }
      if (mWxdb != null) {
        mWxdb.close();
      }
      Log.e("openDb", "读取数据库信息失败" + e.toString());
    }
  }

  /**
   * 微信好友信息
   */
  public void runRecontact() {
    mTask.toPool(new Runnable() {
      @Override
      public void run() {
        getRecontactDate();
      }
    }).toMain(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(mContext, "查询通讯录完毕", Toast.LENGTH_LONG).show();
        runMessage();
      }
    }).run();
  }

  /**
   * 获取当前用户的微信所有联系人
   *
   * username原始微信号/群号，和message中的talker就是使用的这个
   * alias是自己设置的微信号，如bhpg128
   * nikename:用户昵称，如原来足球是圆的/群名称
   *
   * 7未设置微信号的好友；
   *
   */
  public void getRecontactDate() {
    Cursor c1 = null;
    try {
      //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
      c1 = mWxdb.rawQuery(
          "select * from rcontact where verifyFlag = 0 and type != 4 and type != 2 and nickname != ''",
          null);
      while (c1.moveToNext()) {
        String userName = c1.getString(c1.getColumnIndex("username"));
        String nickName = c1.getString(c1.getColumnIndex("nickname"));
        Log.e("openDb", "userName====" + userName + "    nickName=====" + nickName);
      }
      c1.close();
    } catch (Exception e) {
      if (c1 != null) {
        c1.close();
      }
      Log.e("openDb", "读取数据库信息失败" + e.toString());
    }
  }

  /**
   * 获得头像信息
   * username:原始微信号
   * reserved1:大图图标url
   * reserved2:小图图标url
   */
  public void getHeadImg(String username) {
    Cursor c1 = null;
    try {
      c1 = mWxdb.rawQuery(
              "select * from img_flag where username = " + username,
              null);
      while (c1.moveToNext()) {
        String reserved2 = c1.getString(c1.getColumnIndex("reserved2"));
        Log.e("openDb", "userName====" + username + "    headurl=====" + reserved2);
      }
      c1.close();
    } catch (Exception e) {
      if (c1 != null) {
        c1.close();
      }
      Log.e("openDb", "读取数据库信息失败" + e.toString());
    }
  }

  /**
   * 复制单个文件
   *
   * @param oldPath String 原文件路径 如：c:/fqf.txt
   * @param newPath String 复制后路径 如：f:/fqf.txt
   * @return boolean
   */
  public void copyFile(String oldPath, String newPath) {
    try {
      int byteRead = 0;
      File oldFile = new File(oldPath);
      if (oldFile.exists()) { //文件存在时
        InputStream inStream = new FileInputStream(oldPath); //读入原文件
        FileOutputStream fs = new FileOutputStream(newPath);
        byte[] buffer = new byte[1444];
        while ((byteRead = inStream.read(buffer)) != -1) {
          fs.write(buffer, 0, byteRead);
        }
        inStream.close();
      }
    } catch (Exception e) {
      Log.e("copyFile", "复制单个文件操作出错");
      e.printStackTrace();
    }
  }


  /**
   * 递归查询微信本地数据库文件
   *
   * @param file 目录
   * @param fileName 需要查找的文件名称
   */
  public void searchFile(File file, String fileName) {
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files != null) {
        for (File childFile : files) {
          searchFile(childFile, fileName);
        }
      }
    } else {
      if (fileName.equals(file.getName())) {
        mWxDbPathList.add(file);
      }
    }
  }

}
