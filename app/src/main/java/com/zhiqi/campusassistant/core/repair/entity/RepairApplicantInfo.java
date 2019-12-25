package com.zhiqi.campusassistant.core.repair.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/3/30.
 * 报修人信息
 */

public class RepairApplicantInfo implements BaseJsonData, Parcelable {

    public long id;

    public int district_id;

    public int location_id;

    public String phone;

    public String applicant_name;

    public String address;

    public RepairApplicantInfo() {
    }

    protected RepairApplicantInfo(Parcel in) {
        id = in.readLong();
        district_id = in.readInt();
        location_id = in.readInt();
        phone = in.readString();
        applicant_name = in.readString();
        address = in.readString();
    }

    public static final Creator<RepairApplicantInfo> CREATOR = new Creator<RepairApplicantInfo>() {
        @Override
        public RepairApplicantInfo createFromParcel(Parcel in) {
            return new RepairApplicantInfo(in);
        }

        @Override
        public RepairApplicantInfo[] newArray(int size) {
            return new RepairApplicantInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(district_id);
        dest.writeInt(location_id);
        dest.writeString(phone);
        dest.writeString(applicant_name);
        dest.writeString(address);
    }
}
