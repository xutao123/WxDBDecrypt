package com.wx.decrypt.util;

import android.os.Build;

import static android.os.Build.VERSION_CODES.O_MR1;

public class PhoneUtil {

    /**
     * adb devices获得的devices id，Appium自动化用
     * @return
     */
    public static String getAdbDeviceId() {
        if (Build.VERSION.SDK_INT >= O_MR1) {
            return android.os.Build.getSerial();
        } else {
            return android.os.Build.SERIAL;
        }
    }

}

