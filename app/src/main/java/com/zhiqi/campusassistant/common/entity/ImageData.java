package com.zhiqi.campusassistant.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ming on 2017/2/5.
 * 图片信息
 */

public class ImageData implements Parcelable {

    public String origin;

    public String thumbnail;

    public ImageData() {
    }

    public ImageData(String origin, String thumbnail) {
        this.origin = origin;
        this.thumbnail = thumbnail;
    }

    protected ImageData(Parcel in) {
        origin = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(origin);
        dest.writeString(thumbnail);
    }
}
