package com.zhiqi.campusassistant.config;

/**
 * Created by ming on 2016/10/8.
 * app 常量
 */

public class AppConstant {

    /**
     * 验证码长度
     */
    public static final int LOGIN_VERIFY_LENGTH = 6;

    /**
     * 账号最小长度
     */
    public static final int LOGIN_ACCOUNT_MIN_LENGTH = 6;

    /**
     * 密码最小长度
     */
    public static final int LOGIN_PASSWORD_MIN_LENGTH = 6;

    /**
     * 支付密码长度
     */
    public static final int PAY_PASSWORD_LENGTH = 6;

    /**
     * 申请备注信息字符串最大长度
     */
    public static final int ARROVAL_COMMENT_MAX_LENGTH = 50;

    /**
     * 最大上传数量
     */
    public static final int MAX_UPLOAD_PHOTO_SIZE = 8;

    /**
     * 手机号
     */
    public static final String EXTRA_PHONE = "phone";

    /**
     * 用户id参数名
     */
    public static final String EXTRA_USER_ID = "user_id";

    /**
     * 区域信息参数名
     */
    public static final String EXTRA_AREA_INFO = "area_info";

    /**
     * 用户信息参数名
     */
    public static final String EXTRA_USER_INFO = "user_info";

    /**
     * 用户信息参数名
     */
    public static final String EXTRA_DEPARTMENT_ID = "department_id";

    /**
     * 课程表信息
     */
    public static final String EXTRA_COURSE_INFO = "course_info";

    /**
     * 课程ID信息
     */
    public static final String EXTRA_COURSE_ID = "course_id";

    /**
     * 维修ID
     */
    public static final String EXTRA_REPAIR_ID = "repair_id";

    /**
     * 是否为自己的数据
     */
    public static final String EXTRA_IS_SELF = "is_self";

    /**
     * 维修请求
     */
    public static final String EXTRA_REPAIR_REQUEST = "repair_request";

    /**
     * 上传文件时，文件的key
     */
    public static final String UPLOAD_KEY = "files";

    /**
     * 请假ID
     */
    public static final String EXTRA_LEAVE_ID = "leave_id";

    /**
     * 请假请求信息
     */
    public static final String EXTRA_LEAVE_REQUEST = "leave_request";

    /**
     * 请假用户类型
     */
    public static final String EXTRA_LEAVE_USER_TYPE = "leave_user_type";

    /**
     * 请假用户类型：员工
     */
    public static final int VALUE_LEAVE_USER_TYPE_EMPLOYEE = 0;

    /**
     * 请假用户类型：学生
     */
    public static final int VALUE_LEAVE_USER_TYPE_STUDENT = 1;

    /**
     * 栏目ID
     */
    public static final String EXTRA_CATEGORY_ID = "category_id";

    /**
     * url
     */
    public static final String EXTRA_URL = "url";

    /**
     * 绑定手机号操作类型
     */
    public static final String EXTRA_BIND_PHONE_OPT = "bind_phone_opt";

    /**
     * 验证信息
     */
    public static final String EXTRA_VERIFY_INFO = "verify_info";

    /**
     * 验证码校验结果token
     */
    public static final String EXTRA_RESET_TOKEN = "reset_token";

    /**
     * 模块
     */
    public static final String EXTRA_APP_MODULE = "app_module";

    /**
     * 申请人信息
     */
    public static final String EXTRA_APPLICANT_INFO = "applicant_info";

    /**
     * 学校版本相关数据
     */
    public static final String EXTRA_CAMPUS_VERSION_DATA = "campus_version_data";

    /**
     * 失物招领类型
     */
    public static final String EXTRA_LOST_TYPE = "lost_type";

    /**
     * 学年
     */
    public static final String EXTRA_SCORE_SCHOOL_YEAR = "score_school_year";

    /**
     * 学期
     */
    public static final String EXTRA_SEMESTER = "semester";

    /**
     * 课程
     */
    public static final String EXTRA_COURSE_NAME = "course_name";

    /**
     * 浏览器host
     */
    public static final String HOST_BROWSER = "browser";
    /**
     * 支付消费的id
     */
    public static final String EXTRA_PAY_EXPENSE_ID = "expense_id";

    /**
     * 支付订单id
     */
    public static final String EXTRA_PAY_ORDER_ID = "order_id";

    /**
     * 支付订单号
     */
    public static final String EXTRA_PAY_ORDER_NO = "order_no";

    /**
     * 定位信息
     */
    public static final String EXTRA_LOCATION_INFO = "location_info";

    /**
     * 住宿详细信息
     */
    public static final String EXTRA_BEDROOM_DETAIL = "bed_room_detail";

    /**
     * 宿舍id
     */
    public static final String EXTRA_BEDROOM_ID = "bed_room_id";

    /**
     * 是否有支付密码
     */
    public static final String EXTRA_CARD_HAS_PAY_PASSWORD = "has_pay_password";

    /**
     * 校园卡
     */
    public static final String EXTRA_CARD_BALANCE = "card_balance";

    /**
     * 请求增加code
     */
    public static final int REQUEST_CODE_FOR_ADD = 20001;

    /**
     * 请求修改code
     */
    public static final int REQUEST_CODE_FOR_UPDATE = 20002;

    /**
     * 绑定手机request
     */
    public static final int ACTIVITY_REQUEST_VERIFY_CODE = 8000;

    /**
     * 请求支付
     */
    public static final int ACTIVITY_REQUEST_PAY_CODE = 8001;

    /**
     * 选择床位request
     */
    public static final int ACTIVITY_REQUEST_CHOOSE_BED = 8002;

    /**
     * 校园通充值
     */
    public static final int ACTIVITY_REQUEST_ONE_CARD_TOP_UP = 8003;

    /**
     * 校园卡密码验证
     */
    public static final int ACTIVITY_REQUEST_ONE_CARD_VERIFY_PWD = 8004;

    /**
     * 校园卡余额请求
     */
    public static final int ACTIVITY_REQUEST_ONE_CARD_BALANCE = 8005;

    /**
     * 安装应用请求
     */
    public static final int ACTIVITY_REQUEST_UNKNOWN_APP_SOURCES = 8006;
}
