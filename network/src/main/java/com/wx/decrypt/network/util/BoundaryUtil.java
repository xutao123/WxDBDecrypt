package com.wx.decrypt.network.util;

import java.util.UUID;

public class BoundaryUtil {

    public static String getBoundary() {
        return UUID.randomUUID().toString();
    }

}
