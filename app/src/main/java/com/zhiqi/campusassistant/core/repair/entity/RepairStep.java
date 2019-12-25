package com.zhiqi.campusassistant.core.repair.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/2/5.
 * 维修步骤
 */

public class RepairStep implements Parcelable, BaseJsonData {

    public int step;

    public long operator_id;

    public String operator_head;

    public String operator_name;

    public RepairStatus status;

    public String comment;

    public String position;

    public String set_time;

    public RepairStep() {
    }

    protected RepairStep(Parcel in) {
        step = in.readInt();
        operator_id = in.readLong();
        operator_head = in.readString();
        operator_name = in.readString();
        comment = in.readString();
        position = in.readString();
        set_time = in.readString();
    }

    public static final Creator<RepairStep> CREATOR = new Creator<RepairStep>() {
        @Override
        public RepairStep createFromParcel(Parcel in) {
            return new RepairStep(in);
        }

        @Override
        public RepairStep[] newArray(int size) {
            return new RepairStep[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(step);
        dest.writeLong(operator_id);
        dest.writeString(operator_head);
        dest.writeString(operator_name);
        dest.writeString(comment);
        dest.writeString(position);
        dest.writeString(set_time);
    }
}
