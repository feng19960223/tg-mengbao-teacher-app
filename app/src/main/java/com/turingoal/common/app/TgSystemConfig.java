package com.turingoal.common.app;

import android.os.Environment;

/**
 * 系统配置
 */
public final class TgSystemConfig {
    public static final int WELCOME_DELAY_TIME = 2000; // 欢迎界面延迟时间
    public static final String SERVER_PROTOCOL = "http"; // server 协议 http，https
    public static final String SERVER_IP = "192.168.1.122"; // 测试 server ip
//    public static final String SERVER_IP = "123.56.195.193"; // 上线 server ip
    public static final Integer SERVER_PORT = 8080; // server port
    public static final String SERVER_NAME = "tg-mengbao"; // server name
    public static final String LOG_TAG = "tg-mengbao"; // log tag
    public static final String SP_NAME = "tg-mengbao_data"; // SharedPreferences名称
    public static final String DB_NAME = "tg-mengbao.db"; // 数据库名称
    public static final String BASE_STORE_PATH = Environment.getExternalStorageDirectory() + "/mengbao/"; // 基本保存路径
    public static final String IMG_STORE_PATH = BASE_STORE_PATH + "imgs/"; // 基本保存路径,图片
    // 应用下载地址
    public static final String APP_DOWNLOAD_URL = "https://www.baidu.com";

    private TgSystemConfig() {
        throw new Error("工具类不能实例化！");
    }

    /**
     * 获取服务器基础路径
     */
    public static String getServerBaseUrl() {
        return SERVER_PROTOCOL + "://" + SERVER_IP + ":" + SERVER_PORT + "/" + SERVER_NAME;
    }
}
