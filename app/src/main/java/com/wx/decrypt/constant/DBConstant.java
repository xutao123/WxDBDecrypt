package com.wx.decrypt.constant;

public class DBConstant {

    public static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";
    public static final String WX_DB_DIR_PATH = WX_ROOT_PATH + "MicroMsg";

    //db文件名称
    public static final String ENMICROMSG_DB_NAME = "EnMicroMsg.db";
    public static final String FILE_INDEX_DB_NAME = "WxFileIndex.db";

    public static final String REAL_IMAGE_PATH_PREFIX = "/storage/emulated/0/tencent/MicroMsg/";

    //通过监听wal文件变化来监听聊天数据
    public static final String WX_DB_WAL_FILE_NAME = "EnMicroMsg.db-wal";

}
