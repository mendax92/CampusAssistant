package com.zhiqi.campusassistant.core.repair.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.common.entity.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2017/2/5.
 * 维修详情
 */

public class RepairDetails implements Parcelable, BaseJsonData {

    public long id;

    public int repair_type;

    public String type_name;

    public String detail;

    public RepairStatus status;

    public String status_name;

    public String user_head;

    public String applicant_name;

    public String apply_time;

    public String order_no;

    public String phone;

    public int district_id;

    public int location_id;

    public String address;

    public long appointment;

    public List<RepairAction> user_actions;

    public List<RepairStep> steps;

    public List<ImageData> images;

    public RepairDetails() {
    }

    protected RepairDetails(Parcel in) {
        id = in.readLong();
        repair_type = in.readInt();
        type_name = in.readString();
        detail = in.readString();
        status = RepairStatus.formatValue(in.readInt());
        status_name = in.readString();
        user_head = in.readString();
        applicant_name = in.readString();
        apply_time = in.readString();
        order_no = in.readString();
        phone = in.readString();
        district_id = in.readInt();
        location_id = in.readInt();
        address = in.readString();
        appointment = in.readLong();
        steps = in.createTypedArrayList(RepairStep.CREATOR);
        images = in.createTypedArrayList(ImageData.CREATOR);
        int[] actions = in.createIntArray();
        if (actions != null) {
            user_actions = new ArrayList<>();
            for (int action : actions) {
                user_actions.add(RepairAction.formatValue(action));
            }
        }
    }

    public static final Creator<RepairDetails> CREATOR = new Creator<RepairDetails>() {
        @Override
        public RepairDetails createFromParcel(Parcel in) {
            return new RepairDetails(in);
        }

        @Override
        public RepairDetails[] newArray(int size) {
            return new RepairDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(repair_type);
        dest.writeString(type_name);
        dest.writeString(detail);
        dest.writeInt(status.getValue());
        dest.writeString(status_name);
        dest.writeString(user_head);
        dest.writeString(applicant_name);
        dest.writeString(apply_time);
        dest.writeString(order_no);
        dest.writeString(phone);
        dest.writeInt(district_id);
        dest.writeInt(location_id);
        dest.writeString(address);
        dest.writeLong(appointment);
        dest.writeTypedList(steps);
        dest.writeTypedList(images);
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
