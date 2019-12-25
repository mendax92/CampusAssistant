package com.zhiqi.campusassistant.core.onecard.entity;

import com.google.gson.annotations.JsonAdapter;
import com.ming.base.bean.BaseJsonData;
import com.ming.base.gson.BooleanDeserializer;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;

import java.util.List;

/**
 * Created by ming on 2018/2/3.
 * 校园卡二维码信息
 */

public class CardQrCodeInfo extends BannerInfos implements BaseJsonData {

    public int is_release;
    public int is_open;
    public int has_pay_password;
    public String instructions_url;
    public List<QrInfo> qrcode_list;

    public boolean getIs_release() {
        return is_release == 1;
    }

    public boolean getHas_pay_password() {
        return has_pay_password == 1;
    }

    public boolean getIs_open() {
        return is_open == 1;
    }

    public void setIs_release(int is_release) {
        this.is_release = is_release;
    }


    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }


    public void setHas_pay_password(int has_pay_password) {
        this.has_pay_password = has_pay_password;
    }

    public static class QrInfo implements BaseJsonData {

        public int seq;
        public boolean isShow;
        public String qrcode;
    }
}
