package com.zhiqi.campusassistant.core.upload.model;

/**
 * Created by ming on 2017/2/27.
 */

public enum UploadStatus {

    Waiting(0),

    Uploading(1),

    Success(2),

    Failed(3);

    private int value;

    UploadStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final UploadStatus formatValue(int value) {
        switch (value) {
            case 1:
                return Uploading;
            case 2:
                return Success;
            case 3:
                return Failed;
            default:
                return Waiting;
        }
    }
}
