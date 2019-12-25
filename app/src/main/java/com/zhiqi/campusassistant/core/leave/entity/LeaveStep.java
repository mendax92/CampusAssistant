package com.zhiqi.campusassistant.core.leave.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/3/9.
 * 请假审批流程步骤
 */

public class LeaveStep implements Parcelable, BaseJsonData {

    public int step;

    public long operator_id;

    public String operator_head;

    public String operator_name;

    public LeaveStatus status;

    public String comment;

    public String position;

    public String set_time;

    protected LeaveStep(Parcel in) {
        step = in.readInt();
        operator_id = in.readLong();
        operator_head = in.readString();
        operator_name = in.readString();
        comment = in.readString();
        position = in.readString();
        set_time = in.readString();
        status = LeaveStatus.formatValue(in.readInt());
    }

    public static final Creator<LeaveStep> CREATOR = new Creator<LeaveStep>() {
        @Override
        public LeaveStep createFromParcel(Parcel in) {
            return new LeaveStep(in);
        }

        @Override
        public LeaveStep[] newArray(int size) {
            return new LeaveStep[size];
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
        dest.writeInt(status.getValue());
    }
}
