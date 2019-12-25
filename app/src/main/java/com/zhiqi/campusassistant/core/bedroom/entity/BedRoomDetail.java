package com.zhiqi.campusassistant.core.bedroom.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ming on 2017/8/23.
 * 已选择宿舍床位详情
 */

public class BedRoomDetail extends BedRoomInfo implements Parcelable {

    public String qr_code;

    public String bed_name;

    public String student_no;

    public String service_tel;

    public BedRoomDetail() {
    }

    protected BedRoomDetail(Parcel in) {
        room_id = in.readLong();
        image = in.readString();
        room = in.readString();
        location = in.readString();
        type_name = in.readString();
        is_selected = in.readByte() != 0;
        qr_code = in.readString();
        bed_name = in.readString();
        student_no = in.readString();
        service_tel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(room_id);
        dest.writeString(image);
        dest.writeString(room);
        dest.writeString(location);
        dest.writeString(type_name);
        dest.writeByte((byte) (is_selected ? 1 : 0));
        dest.writeString(qr_code);
        dest.writeString(bed_name);
        dest.writeString(student_no);
        dest.writeString(service_tel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BedRoomDetail> CREATOR = new Creator<BedRoomDetail>() {
        @Override
        public BedRoomDetail createFromParcel(Parcel in) {
            return new BedRoomDetail(in);
        }

        @Override
        public BedRoomDetail[] newArray(int size) {
            return new BedRoomDetail[size];
        }
    };
}
