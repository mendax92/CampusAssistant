package com.zhiqi.campusassistant.config;

/**
 * Created by Edmin on 2016/11/5 0005.
 */

public class HttpErrorCode {

    /**
     * 默认成功
     */
    public static final int SUCCESS = 0;
    /**
     * 异常
     */
    public static final int ERROR_EXCEPTION = -1;

    /**
     * timestamp失效  HTTP Status Code:400
     */
    public static final int ERROR_TIMESTAMP = 10003;

    /**
     * 尚未登录   HTTP Status Code:400
     */
    public static final int ERROR_NOT_LOGIN = 20001;

    /**
     * token失效
     */
    public static final int ERROR_TOKEN_INVALID = 20104;

    /**
     * 数据失效
     */
    public static final int ERROR_DATA_INVALID = 20004;

    /**
     * 上传文件不存在
     */
    public static final int ERROR_UPLOAD_FILE_NOT_EXIST = 22201;

    /**
     * 支付密码错误
     */
    public static final int ERROR_PAYMENT_PASSWORD = 20204;
}
