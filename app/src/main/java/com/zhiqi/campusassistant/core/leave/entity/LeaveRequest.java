package com.zhiqi.campusassistant.core.leave.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.gson.Ignore;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.entity.UploadType;

import java.util.List;

/**
 * Created by ming on 2017/3/9.
 * 请假请求
 */

public class LeaveRequest extends BaseUploadBean implements Parcelable {

    @Ignore
    public UploadType uploadType = UploadType.Vacation;

    public long form_id;

    public int type;

    /**
     * 提交图片数据
     */
    public List<String> images;

    /**
     * 远程图片数据
     */
    @Ignore
    public List<ImageData> imageDatas;

    public long start_time;

    public long end_time;

    public float total_days;

    public String reason;

    public LeaveData data_version;

    public LeaveRequest() {
    }

    protected LeaveRequest(Parcel in) {
        uploadType = UploadType.formatValue(in.readString());
        form_id = in.readLong();
        type = in.readInt();
        images = in.createStringArrayList();
        start_time = in.readLong();
        end_time = in.readLong();
        total_days = in.readFloat();
        reason = in.readString();
        data_version = in.readParcelable(LeaveData.class.getClassLoader());
        imageDatas = in.createTypedArrayList(ImageData.CREATOR);
    }

    public static final Creator<LeaveRequest> CREATOR = new Creator<LeaveRequest>() {
        @Override
        public LeaveRequest createFromParcel(Parcel in) {
            return new LeaveRequest(in);
        }

        @Override
        public LeaveRequest[] newArray(int size) {
            return new LeaveRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uploadType != null ? uploadType.getValue() : null);
        dest.writeLong(form_id);
        dest.writeInt(type);
        dest.writeStringList(images);
        dest.writeLong(start_time);
        dest.writeLong(end_time);
        dest.writeFloat(total_days);
        dest.writeString(reason);
        dest.writeParcelable(data_version, flags);
        dest.writeTypedList(imageDatas);
    }

    @Override
    public UploadType getUploadType() {
        return uploadType;
    }

    @Override
    public List<String> getUploadFiles() {
        return images;
    }

    @Override
    public void setUploadFiles(List<String> uploadFiles) {
        this.images = uploadFiles;
    }

    public static class LeaveData implements BaseJsonData, Parcelable {
        public int vacation_type;

        public LeaveData() {
        }

        public LeaveData(int vacationType) {
            this.vacation_type = vacationType;
        }

        protected LeaveData(Parcel in) {
            vacation_type = in.readInt();
        }

        public static final Creator<LeaveData> CREATOR = new Creator<LeaveData>() {
            @Override
            public LeaveData createFromParcel(Parcel in) {
                return new LeaveData(in);
            }

            @Override
            public LeaveData[] newArray(int size) {
                return new LeaveData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(vacation_type);
        }
    }
}
