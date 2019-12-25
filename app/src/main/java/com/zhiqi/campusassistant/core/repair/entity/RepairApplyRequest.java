package com.zhiqi.campusassistant.core.repair.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.gson.Ignore;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.entity.UploadType;

import java.util.List;

/**
 * Created by ming on 2017/1/19.
 * 维修申请
 */

public class RepairApplyRequest extends BaseUploadBean implements Parcelable {

    @Ignore
    public UploadType uploadType = UploadType.Maintenance;

    public long form_id;

    public int type;

    public String detail;

    /**
     * 提交图片数据
     */
    public List<String> images;

    /**
     * 远程图片数据
     */
    @Ignore
    public List<ImageData> imageDatas;

    public String applicant_name;

    public String phone;

    public int district_id;

    public int location_id;

    public String address;

    public long appointment;

    public DataVersion data_version;

    public RepairApplyRequest() {
    }

    protected RepairApplyRequest(Parcel in) {
        uploadType = UploadType.formatValue(in.readString());
        form_id = in.readLong();
        type = in.readInt();
        detail = in.readString();
        images = in.createStringArrayList();
        imageDatas = in.createTypedArrayList(ImageData.CREATOR);
        applicant_name = in.readString();
        phone = in.readString();
        district_id = in.readInt();
        location_id = in.readInt();
        address = in.readString();
        appointment = in.readLong();
        data_version = in.readParcelable(DataVersion.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uploadType != null ? uploadType.getValue() : null);
        dest.writeLong(form_id);
        dest.writeInt(type);
        dest.writeString(detail);
        dest.writeStringList(images);
        dest.writeTypedList(imageDatas);
        dest.writeString(applicant_name);
        dest.writeString(phone);
        dest.writeInt(district_id);
        dest.writeInt(location_id);
        dest.writeString(address);
        dest.writeLong(appointment);
        dest.writeParcelable(data_version, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RepairApplyRequest> CREATOR = new Creator<RepairApplyRequest>() {
        @Override
        public RepairApplyRequest createFromParcel(Parcel in) {
            return new RepairApplyRequest(in);
        }

        @Override
        public RepairApplyRequest[] newArray(int size) {
            return new RepairApplyRequest[size];
        }
    };


    public static class DataVersion implements BaseJsonData, Parcelable {
        public int campus_subarea;
        public int maintenance_item;

        public DataVersion() {
        }

        protected DataVersion(Parcel in) {
            campus_subarea = in.readInt();
            maintenance_item = in.readInt();
        }

        public static final Creator<DataVersion> CREATOR = new Creator<DataVersion>() {
            @Override
            public DataVersion createFromParcel(Parcel in) {
                return new DataVersion(in);
            }

            @Override
            public DataVersion[] newArray(int size) {
                return new DataVersion[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(campus_subarea);
            dest.writeInt(maintenance_item);
        }
    }

    @Override
    public List<String> getUploadFiles() {
        return images;
    }

    @Override
    public void setUploadFiles(List<String> uploadFiles) {
        this.images = uploadFiles;
    }

    @Override
    public UploadType getUploadType() {
        return uploadType;
    }
}
