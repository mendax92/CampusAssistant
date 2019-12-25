package com.zhiqi.campusassistant.core.leave.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.common.entity.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2017/3/9.
 * 请假详情
 */

public class LeaveDetails implements Parcelable, BaseJsonData {

    public long id;

    public long applicant_id;

    public String applicant_name;

    public String department;

    public long start_time;

    public long end_time;

    public float total_days;

    public String reason;

    public String apply_time;

    public LeaveStatus status;

    public String status_name;

    public String approver;

    public String head;

    public String type_name;

    public int type_id;

    public List<ImageData> images;

    public List<LeaveAction> user_actions;

    public List<LeaveStep> steps;

    protected LeaveDetails(Parcel in) {
        id = in.readLong();
        applicant_id = in.readLong();
        applicant_name = in.readString();
        department = in.readString();
        start_time = in.readLong();
        end_time = in.readLong();
        total_days = in.readFloat();
        reason = in.readString();
        apply_time = in.readString();
        status_name = in.readString();
        approver = in.readString();
        head = in.readString();
        type_name = in.readString();
        type_id = in.readInt();
        status = LeaveStatus.formatValue(in.readInt());
        images = in.createTypedArrayList(ImageData.CREATOR);
        steps = in.createTypedArrayList(LeaveStep.CREATOR);
        int[] actions = in.createIntArray();
        if (actions != null) {
            user_actions = new ArrayList<>();
            for (int action : actions) {
                user_actions.add(LeaveAction.formatValue(action));
            }
        }
    }

    public static final Creator<LeaveDetails> CREATOR = new Creator<LeaveDetails>() {
        @Override
        public LeaveDetails createFromParcel(Parcel in) {
            return new LeaveDetails(in);
        }

        @Override
        public LeaveDetails[] newArray(int size) {
            return new LeaveDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(applicant_id);
        dest.writeString(applicant_name);
        dest.writeString(department);
        dest.writeLong(start_time);
        dest.writeLong(end_time);
        dest.writeFloat(total_days);
        dest.writeString(reason);
        dest.writeString(apply_time);
        dest.writeString(status_name);
        dest.writeString(approver);
        dest.writeString(head);
        dest.writeString(type_name);
        dest.writeInt(type_id);
        dest.writeInt(status.getValue());
        dest.writeTypedList(images);
        dest.writeTypedList(steps);
        int[] actions = null;
        if (user_actions != null) {
            actions = new int[user_actions.size()];
            for (int i = 0; i < user_actions.size(); i++) {
                actions[i] = user_actions.get(i).getValue();
            }
        }
        dest.writeIntArray(actions);
    }
}
