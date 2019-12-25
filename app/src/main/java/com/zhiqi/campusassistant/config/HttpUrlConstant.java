package com.zhiqi.campusassistant.config;

import com.zhiqi.campusassistant.BuildConfig;

/**
 * Created by Edmin on 2016/8/30 0030.
 * http 链接常量
 */
public class HttpUrlConstant {

    /*public static final String TEST_HOST = "http://fc974085-b9c9-431f-9acb-ea9f013a4ccf.mock.pstmn.io";

    public static final String TEST_BASE_URL = TEST_HOST + "/campuslabs/api/";*/

    /**
     * http HOST
     */
    public static final String HOST = BuildConfig.HOST;

    /**
     * web动态host地址
     */
    public static final String OA_HOST = BuildConfig.OA_HOST;

    /**
     * API基本路径
     */
    public static final String BASE_URL = HOST + "/campuslabs/api/";

    /**
     * 上传文件
     */
    public static final String UPLOAD_FILE_URL = "uploadpics";

    /**
     * 获取验证码
     */
    public static final String GET_VERIFY_CODE = "user/getverificationcode";

    /**
     * 校验验证码
     */
    public static final String CHECK_VERIFY_CODE = "user/checkverificationcode";

    /**
     * 重置密码
     */
    public static final String RESET_PASSWORD = "user/resetpassword";

    /**
     * 修改密码
     */
    public static final String CHANGE_PASSWORD = "user/changepassword";

    /**
     * 登录
     */
    public static final String USER_LOGIN = "user/login";

    /**
     * 退出
     */
    public static final String USER_LOGOUT = "user/logout";

    /**
     * 查询服务器时间
     */
    public static final String QUERY_SERVER_TIME = "security/servertime";

    /**
     * 查询支付历史记录
     */
    public static final String QUERY_PAYMENT_HISTORY = "campuscard/transaction";

    /**
     * 查询通讯录列表
     */
    public static final String QUERY_CONTACTS_LIST = "contacts/index";

    /**
     * 查询用户个人详细信息
     */
    public static final String QUERY_USER_DETAILS = "contacts/details";

    /**
     * 搜索通讯录
     */
    public static final String SEARCH_CONTACTS = "contacts/search";

    /**
     * 查询课程表列表
     */
    public static final String QUERY_COURSE_LIST = "course/list";

    /**
     * 查询课程表列表
     */
    public static final String QUERY_COURSE_DETAIL = "course/details";

    /**
     * 获取消息列表
     */
    public static final String QUERY_MESSAGE_LIST = "message/index";

    /**
     * 查询维修记录
     */
    public static final String QUERY_REPAIR_RECORD = "repair/recordlist";

    /**
     * 查询维修相关的数据字典
     */
    public static final String QUERY_REPAIR_DATA_DICTIONARY = "repair/datalist";

    /**
     * 维修申请
     */
    public static final String REQUEST_REPAIR_APPLY = "repair/apply";

    /**
     * 修改维修申请
     */
    public static final String REQUEST_UPDATE_REPAIR_APPLY = "repair/update";

    /**
     * 维修详情
     */
    public static final String QUERY_REPAIR_DETAILS = "repair/details";

    /**
     * 维修审批操作
     */
    public static final String DO_REPAIR_ACTIONS = "repair/actions";

    /**
     * 查询维修审批list
     */
    public static final String QUERY_REPAIR_APPROVAL_LIST = "repair/handlelist";

    /**
     * 查询报修人list
     */
    public static final String QUERY_REPAIR_APPLICANT_LIST = "repair/applicantinfolist";

    /**
     * 查询请假记录
     */
    public static final String QUERY_LEAVE_RECORD = "leave/recordlist";

    /**
     * 查询请假类型
     */
    public static final String QUERY_LEAVE_VACATION_TYPES = "leave/vacationtypes";

    /**
     * 请假申请
     */
    public static final String REQUEST_LEAVE_APPLY = "leave/apply";

    /**
     * 修改请假申请
     */
    public static final String REQUEST_UPDATE_LEAVE_APPLY = "leave/update";

    /**
     * 查询请假详情
     */
    public static final String QUERY_LEAVE_DETAILS = "leave/details";

    /**
     * 请假审批操作
     */
    public static final String DO_LEAVE_ACTION = "leave/actions";

    /**
     * 查询请假审批列表
     */
    public static final String QUERY_LEAVE_APPROVAL_LIST = "leave/examinelist";

    /**
     * 查询app列表
     */
    public static final String QUERY_APP_LIST = "applications/applist";

    /**
     * 查询新闻栏目
     */
    public static final String QUERY_NEWS_CATEGORY = "news/category";

    /**
     * 查询新闻列表
     */
    public static final String QUERY_NEWS_LIST = "news/list";

    /**
     * 用户反馈
     */
    public static final String USER_CENTER_FEEDBACK = "feedback/advice";

    /**
     * 设置用户的JPush registrationid
     */
    public static final String PUSH_USER_REGISTRATIONID = "user/registrationid";

    /**
     * 获取通知公告列表
     */
    public static final String GET_NOTICE_LIST = "notice/list";

    /**
     * 查询失物招领列表
     */
    public static final String GET_LOST_LIST = "lost/list";

    /**
     * 获取我的失物
     */
    public static final String GET_MY_LOST = "lost/my";

    /**
     * 获取失物类型
     */
    public static final String GET_LOST_TYPE = "lost/losttypes";

    /**
     * 失物招领申请
     */
    public static final String REQUEST_LOST_APPLY = "lost/apply";

    /**
     * 操作失物招领
     */
    public static final String DO_LOST_ACTION = "lost/actions";

    /**
     * 查询学期信息
     */
    public static final String GET_SCORES_SEMESTER = "studentscore/dropdata";

    /**
     * 查询课程成绩
     */
    public static final String GET_COURSE_SCORES = "studentscore/list";

    /**
     * 查询成绩详情
     */
    public static final String GET_SCORE_DETAIL = "studentscore/detail";

    /**
     * 检测app更新
     */
    public static final String CHECK_UPGRADE = "setting/checkupdate";

    /**
     * 获取学生支付信息
     */
    public static final String GET_STUDENT_EXPENSE = "expense/stuexpenselist";

    /**
     * 获取支付详细信息
     */
    public static final String GET_EXPENSE_INFO = "expense/expenseinfo";

    /**
     * 生成学生订单信息
     */
    public static final String GET_STUDENT_ORDER_INFO = "payment/wepay/apppay/student";

    /**
     * 订单支付结果
     */
    public static final String GET_STUDENT_ORDER_RESULT = "payment/wepay/checkstatus/student";

    /**
     * 获取已完成资助缴费列表
     */
    public static final String GET_SELF_ORDER_LIST = "expense/orderlist";

    /**
     * 获取缴费详情
     */
    public static final String GET_ORDER_DETAIL = "expense/orderdetail";

    /**
     * 获取宿舍列表
     */
    public static final String GET_BEDROOM_LIST = "bedselection/list";

    /**
     * 选择住宿信息
     */
    public static final String GET_BEDROOM_CHOOSE = "bedselection/select";

    /**
     * 获取报到指南列表
     */
    public static final String GET_ENTRANCE_LIST = "reportingguideline/list";

    /**
     * 获取校园定位列表
     */
    public static final String GET_CAMPUS_LOCATION_LIST = "campusmap/locations";

    /**
     * 选择床位
     */
    public static final String REQUEST_CHOOSE_ROOM_APPLY = "bedselection/apply";

    /**
     * 获取余额
     */
    public static final String GET_CARD_BALANCE = "campuscard/balance";

    /**
     * 加载充值信息
     */
    public static final String LOAD_CARD_TOP_UP = "campuscard/payment/info";

    /**
     * 获取校园卡支付信息
     */
    public static final String LOAD_CARD_ORDER = "campuscard/wepay/apppay/submit";

    /**
     * 检查校园卡支付结果状态
     */
    public static final String CHECK_ORDER_STATUS = "campuscard/wepay/apppay/checkstatus";

    /**
     * 获取校园卡订单详情
     */
    public static final String GET_CARD_ORDER_DETAIL = "campuscard/order/detail";

    /**
     * 获取校园卡订单列表
     */
    public static final String GET_CARD_ORDER_LIST = "campuscard/order/list";

    /**
     * 获取二维码信息
     */
    public static final String GET_CARD_QR_CODE = "campuscard/qrcode/base";

    /**
     * 开启校园卡支付功能
     */
    public static final String OPEN_CARD_PAYMENT = "campuscard/verification/submit";

    /**
     * 修改支付密码
     */
    public static final String CHANGE_PAY_PASSWORD = "campuscard/payment/password/update";

    /**
     * 重置支付密码
     */
    public static final String RESET_PAY_PASSWORD = "campuscard/payment/password/reset";

    /**
     * 帮助界面URL
     */
    public static final String BROWSER_HELP_URL = HOST + "/faq/faq.html";

    /**
     * 服务条款浏览URL
     */
    public static final String BROWSER_TERMS_OF_SERVICE = HOST + "/other/terms_of_service.html";

    /**
     * 隐私条款浏览URL
     */
    public static final String BROWSER_PRIVACY_POLICY = HOST + "/other/privacy_policy.html";

    /**
     * 校园网OA URL
     */
    public static final String OA_CAMPUS_NETWORK = OA_HOST + "/h5/network/index/%s";
    /**
     * banner
     */
    public static final String ADVERTISE_GETADVER = "advertise/getAdver";
    /**
     * 校验APP 点击跳转逻辑
     */
    public static final String APP_REDIRECT = "message/redirect";
}
