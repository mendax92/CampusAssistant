package com.zhiqi.campusassistant.core.usercenter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/3/19.
 * 验证码信息
 */

public class VerifyInfo implements BaseJsonData, Parcelable {

    public String code;

    public String request_id;

    public VerifyFunction function;

    public VerifyInfo() {
    }

    protected VerifyInfo(Parcel in) {
        code = in.readString();
        request_id = in.readString();
        function = VerifyFunction.formatValue(in.readInt());
    }

    public static final Creator<VerifyInfo> CREATOR = new Creator<VerifyInfo>() {
        @Override
        public VerifyInfo createFromParcel(Parcel in) {
            return new VerifyInfo(in);
        }

        @Override
        public VerifyInfo[] newArray(int size) {
            return new VerifyInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(request_id);
        dest.writeInt(function.getValue());
    }
}
