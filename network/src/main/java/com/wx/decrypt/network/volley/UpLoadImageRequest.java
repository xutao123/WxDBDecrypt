package com.wx.decrypt.network.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.wx.decrypt.network.response.BaseResponseData;
import com.wx.decrypt.network.util.BoundaryUtil;
import com.wx.decrypt.network.volley.error.VolleyErrorListener;
import com.wx.decrypt.network.volley.file.UploadImageFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UpLoadImageRequest<T> extends BaseRequest<BaseResponseData> {

    private ArrayList<UploadImageFile> mFileList;
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public UpLoadImageRequest(ArrayList<UploadImageFile> fileList, String url, Response.Listener<BaseResponseData> listener, VolleyErrorListener errorListener) {
        super(url, listener, errorListener);
        this.mFileList = fileList;
    }

    public UpLoadImageRequest(String url, Response.Listener<BaseResponseData> listener, VolleyErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mFileList == null || mFileList.size() == 0){
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        int N = mFileList.size() ;
        UploadImageFile formImage ;
        for (int i = 0; i < N ;i++) {
            formImage = mFileList.get(i) ;
            StringBuffer sb= new StringBuffer() ;
            /*第一行*/
            //`"--" + BOUNDARY + "\r\n"`
            sb.append("--"+ BoundaryUtil.getBoundary());
            sb.append("\r\n") ;
            /*第二行*/
            //Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formImage.getTag()) ;
            sb.append("\"") ;
            sb.append("; filename=\"") ;
            sb.append(formImage.getFileName()) ;
            sb.append("\"");
            sb.append("\r\n") ;
            /*第三行*/
            //Content-Type: 文件的 mime 类型 + "\r\n"
            sb.append("Content-Type: ");
            sb.append(getMIMEType(formImage.getFileName()));
            sb.append("\r\n") ;
            /*第四行*/
            //"\r\n"
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes("utf-8"));
                /*第五行*/
                //文件的二进制数据 + "\r\n"
                bos.write(formImage.getValue());
                bos.write("\r\n".getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*结尾行*/
        //`"--" + BOUNDARY + "--" + "\r\n"`
        String endLine = "--" + BoundaryUtil.getBoundary() + "--" + "\r\n" ;
        try {
            bos.write(endLine.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("zgy","=====formImage====\n"+bos.toString()) ;
        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA+"; boundary=" + BoundaryUtil.getBoundary();
    }

    /**
     * 通过文件名获得MIMEType
     *
     * @param fileName
     * @return
     */
    private String getMIMEType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String MIMEType = null;
        try {
            MIMEType = fileNameMap.getContentTypeFor(URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return MIMEType;
    }

}
