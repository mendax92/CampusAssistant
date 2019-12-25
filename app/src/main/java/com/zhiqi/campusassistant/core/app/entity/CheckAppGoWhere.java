package com.zhiqi.campusassistant.core.app.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * @author Wangwz
 * @description:
 * @date :2019/12/25 11:54
 */
public class CheckAppGoWhere implements BaseJsonData {

    /**
     * datas : vsuppid=1001021&appid=wx09421b240f691771&sign=a1824ad361bf30d5749f137a296db317&deptId=1001&userId=303916&nonce=F8aYkWkkUZ&username=张三&timestamp=1577256399
     * apps : {"appType":"2","appUrl":"pages/index/index"}
     */

    private String datas;
    private AppsBean apps;
    //        apps包含参数：appType（跳转类型：1-网页、2-微信小程序等）和appUrl-跳转地址
    public static final int TYPE_WEB = 1;
    public static final int TYPE_APPLETS = 2;

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public AppsBean getApps() {
        return apps;
    }

    public void setApps(AppsBean apps) {
        this.apps = apps;
    }

    public class AppsBean {
        /**
         * appType : 2
         * appUrl : pages/index/index
         */

        private String appType;
        private String appUrl;

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        @Override
        public String toString() {
            return "AppsBean{" +
                    "appType='" + appType + '\'' +
                    ", appUrl='" + appUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CheckAppGoWhere{" +
                "datas='" + datas + '\'' +
                ", apps=" + apps +
                '}';
    }
}
