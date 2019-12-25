package com.zhiqi.campusassistant.core.user.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

/**
 * Created by ming on 2016/10/20.
 */
public class UserInfo implements Parcelable, BaseJsonData {

    public long id;

    public String head;

    public String real_name;

    public String phone;

    public String position;

    public String department;

    public Gender gender;

    public String email;

    public String tel;

    public String short_number;

    public UserRole role_type;

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        id = in.readLong();
        head = in.readString();
        real_name = in.readString();
        phone = in.readString();
        position = in.readString();
        department = in.readString();
        gender = Gender.formatValue(in.readInt());
        email = in.readString();
        tel = in.readString();
        short_number = in.readString();
        role_type = UserRole.formatValue(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(head);
        dest.writeString(real_name);
        dest.writeString(phone);
        dest.writeString(position);
        dest.writeString(department);
        dest.writeInt(gender.getValue());
        dest.writeString(email);
        dest.writeString(tel);
        dest.writeString(short_number);
        if (role_type != null) {
            dest.writeInt(role_type.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
