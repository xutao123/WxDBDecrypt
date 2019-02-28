package com.wx.decrypt.network.volley.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UploadImageFile {

    public String tag;
    public String fileName;
    public String filePath;

    public UploadImageFile(String tag, String fileName, String filePath) {
        this.tag = tag;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLoadFile() {
        return filePath;
    }

    public void setLoadFile(String loadFile) {
        this.filePath = loadFile;
    }

    //对图片进行二进制转换
    public byte[] getValue() {
        if (!TextUtils.isEmpty(filePath)) {
            filePath = Environment.getExternalStorageDirectory()+"/"+filePath;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap != null && bitmap.getByteCount() > 0) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bitmap.recycle();
                return bos.toByteArray();
            }
        }
        return "".getBytes();
    }
}
