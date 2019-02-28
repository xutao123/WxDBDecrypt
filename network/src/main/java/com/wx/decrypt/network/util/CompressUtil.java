package com.wx.decrypt.network.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wx.decrypt.network.app.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author xutao
 */
public class CompressUtil {

    public static File compress(String filePath) throws IOException {
        File resultImgFile = new File(BaseApplication.getInstance().getExternalCacheDir().getAbsolutePath() + "/test.jpg");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(filePath, options);

        BitmapFactory.Options realOptions = new BitmapFactory.Options();
        realOptions.inSampleSize = computeSize(options.outWidth, options.outHeight);
        Bitmap resultBitmap = BitmapFactory.decodeFile(filePath, realOptions);
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 60, resultStream);
        resultBitmap.recycle();

        FileOutputStream fos = new FileOutputStream(resultImgFile);
        fos.write(resultStream.toByteArray());
        fos.flush();
        fos.close();
        resultStream.close();

        return resultImgFile;
    }

    private static Object getFileName(String filePath) {
        return "";
    }

    private static int computeSize(int srcWidth, int srcHeight) {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

}
