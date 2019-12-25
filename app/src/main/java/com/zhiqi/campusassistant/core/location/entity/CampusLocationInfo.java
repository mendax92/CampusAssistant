package com.zhiqi.campusassistant.core.location.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/8/20.
 * 校园定位信息
 */

public class CampusLocationInfo implements BaseJsonData, Parcelable {

    public double latitude;

    public double longitude;

    public String title;

    public String subtitle;

    public String icon;

    public CampusLocationInfo() {
    }

    protected CampusLocationInfo(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        title = in.readString();
        subtitle = in.readString();
        icon = in.readString();
    }

    public static final Creator<CampusLocationInfo> CREATOR = new Creator<CampusLocationInfo>() {
        @Override
        public CampusLocationInfo createFromParcel(Parcel in) {
            return new CampusLocationInfo(in);
        }

        @Override
        public CampusLocationInfo[] newArray(int size) {
            return new CampusLocationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeString(icon);
    }
}
