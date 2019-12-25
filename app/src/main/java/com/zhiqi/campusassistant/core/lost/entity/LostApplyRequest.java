package com.zhiqi.campusassistant.core.lost.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.gson.Ignore;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.entity.UploadType;

import java.util.List;

/**
 * Created by ming on 2017/5/9.
 * 失物发布信息
 */

public class LostApplyRequest extends BaseUploadBean implements Parcelable {

    @Ignore
    public UploadType uploadType = UploadType.LOST;

    public String phone;

    public LostApplyType publish_type;

    public int lost_type;

    public String card_num;

    public String content;

    public List<String> images;

    public LostDataVersion data_version;

    public LostApplyRequest() {
    }

    protected LostApplyRequest(Parcel in) {
        uploadType = UploadType.formatValue(in.readString());
        phone = in.readString();
        publish_type = LostApplyType.formatValue(in.readInt());
        lost_type = in.readInt();
        card_num = in.readString();
        content = in.readString();
        images = in.createStringArrayList();
        data_version = new LostDataVersion(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uploadType.getValue());
        dest.writeString(phone);
        dest.writeInt(publish_type.getValue());
        dest.writeInt(lost_type);
        dest.writeString(card_num);
        dest.writeString(content);
        dest.writeStringList(images);
        dest.writeInt(data_version.lost_type);
    }

    public static final Creator<LostApplyRequest> CREATOR = new Creator<LostApplyRequest>() {
        @Override
        public LostApplyRequest createFromParcel(Parcel in) {
            return new LostApplyRequest(in);
        }

        @Override
        public LostApplyRequest[] newArray(int size) {
            return new LostApplyRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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

    public static class LostDataVersion implements BaseJsonData {

        public LostDataVersion(int lost_type) {
            this.lost_type = lost_type;
        }

        public int lost_type;
    }

}
