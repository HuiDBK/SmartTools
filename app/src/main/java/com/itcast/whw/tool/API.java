package com.itcast.whw.tool;

import android.net.Uri;

/**
 * 数据接口类
 */

public class API {
    /**
     * 协议
     */
    private static final String AGGREMENT = "http";
    /**
     * ip地址
     */
    private static final String LOCALHOST = "39.108.56.171";
    /**
     * 端口号
     */
    private static final String PORT = "8080";
    /**
     * url地址
     */
    private static String URL = AGGREMENT + "://" + LOCALHOST + ":" + PORT + "/";


    /**
     * 用户注册接口
     */
    public static final String API_REGISTER = URL + "register/";
    /**
     * 用户登录接口
     */
    public static final String API_LOGIN = URL + "login/";
}
