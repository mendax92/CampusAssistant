package com.zhiqi.campusassistant.core.course.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

/**
 * Created by ming on 2016/12/5.
 * 课程表信息
 */

public class CourseInfo implements BaseJsonData, Parcelable {

    public long id;

    public String name;

    public String teacher;

    public int week_start;

    public int week_end;

    public int weekday;

    public WeekType week_type;

    public int class_start;

    public int class_end;

    public int course_type;

    public String type_name;

    public String location;

    public CourseInfo() {
    }

    protected CourseInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        teacher = in.readString();
        week_start = in.readInt();
        week_end = in.readInt();
        weekday = in.readInt();
        week_type = WeekType.formatValue(in.readInt());
        class_start = in.readInt();
        class_end = in.readInt();
        course_type = in.readInt();
        type_name = in.readString();
        location = in.readString();
    }

    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel in) {
            return new CourseInfo(in);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(teacher);
        dest.writeInt(week_start);
        dest.writeInt(week_end);
        dest.writeInt(weekday);
        dest.writeInt(week_type == null ? 0 : week_type.getValue());
        dest.writeInt(class_start);
        dest.writeInt(class_end);
        dest.writeInt(course_type);
        dest.writeString(type_name);
        dest.writeString(location);
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
