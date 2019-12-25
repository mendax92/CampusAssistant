package com.zhiqi.campusassistant.core.usercenter.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ming on 2016/11/24.
 * 区域信息
 */

public class AreaInfo implements Parcelable {

    public String areaCode;

    public String areaState;

    public AreaInfo(String areaCode, String areaState) {
        this.areaCode = areaCode;
        this.areaState = areaState;
    }

    private AreaInfo(Parcel in) {
        areaCode = in.readString();
        areaState = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaCode);
        dest.writeString(areaState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AreaInfo> CREATOR = new Creator<AreaInfo>() {
        @Override
        public AreaInfo createFromParcel(Parcel in) {
            return new AreaInfo(in);
        }

        @Override
        public AreaInfo[] newArray(int size) {
            return new AreaInfo[size];
        }
    };
}
