package com.wx.decrypt.network.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.wx.decrypt.network.app.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * "data:image/jpeg;base64,"
 * data:image/png;base64,解码之前要把这些去掉
 * @author xutao
 */
public class Base64Util {

    /**
     * 将图片转换成Base64编码的字符串
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (!exitsFile(path)) {
            return "";
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null !=is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private static boolean exitsFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file != null && file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * base64编码字符集转化成图片文件。
     * @param base64Str
     * @param path 文件存储路径
     * @return 是否成功
     */
    public static boolean base64ToFile(String base64Str, String path) {
        if (TextUtils.isEmpty(base64Str)) {
            return false;
        }
        byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
        for (int i = 0; i < data.length; i++) {
            if(data[i] < 0){
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void test() throws IOException {
        ArrayList<ImageData> list = new ArrayList<>();
//        String path = BaseApplication.getInstance().getExternalCacheDir().getAbsolutePath() + "/test.png";
//        String path = "/storage/emulated/0/tencent/MicroMsg/2b1cf049f366fb4684ce0c24af45e336/image2/69/d5/69d5a7d7e0a0fb74cf21b319ab8145f8.jpg";
        String path = "/storage/emulated/0/tencent/MicroMsg/e0164fa76b2ced4cf1f94fcb9ac7157c/image2/8d/24/8d249df21e7c3f0830f7fd841652ceea.jpg";
        //文件大于200k进行压缩
        File file = CompressUtil.compress(path);
//        File file = new File(path);

        System.out.println("文件大小:" + file.length());

        String str = imageToBase64(BaseApplication.getInstance().getExternalCacheDir().getAbsolutePath() + "/test.jpg");
        System.out.println("图片数据：" + str);

        base64ToFile(str, BaseApplication.getInstance().getExternalCacheDir().getAbsolutePath() + "/test_wx_big.jpg");
    }

    static class ImageData {
        String name;
        String data;
    }

}
