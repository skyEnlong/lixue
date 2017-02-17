package com.lixue.app.common.constants;

/**
 * Created by enlong on 2017/2/13.
 */

public class HttpConst {
    public static final int HTTPS_PORT = 443;
    // public static final String HTTP_HOST = "http://192.168.1.245";//
    // "http://api.xiaogd.com";//
    public static final String HTTP_HOST = "https://api.codoon.com";// "http://api.xiaogd.com";//
    // "http://www.codoon.com";api.xiaogd.com
    public static final String HTTP_API_PUBLIC = HTTP_HOST + "/api";

    public static final String HTTP_LOGIN = HTTP_API_PUBLIC + "／login";

    public static final String HTTP_UPDATE_TOKEN = HTTP_API_PUBLIC + "／update_token";
}
