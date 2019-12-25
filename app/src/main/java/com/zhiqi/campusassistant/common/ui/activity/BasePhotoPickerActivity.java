package com.zhiqi.campusassistant.common.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ming.photopicker.PhotoPicker;
import com.ming.photopicker.PhotoPreview;
import com.ming.photopicker.utils.ImageCaptureManager;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by ming on 2017/2/25.
 * 图片选择器调用基础类
 */

public class BasePhotoPickerActivity extends BaseToolbarActivity {

    private static final String TAG = "BasePhotoPickerActivity";

    private ImageCaptureManager captureManager;

    private OnPhotoChooseListener listener;


    public void requestChoosePhoto(final int maxCount, final ArrayList<String> selectPath) {
        new MaterialDialog.Builder(this)
                .items(R.array.photo_source_choice)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (position == 0) {
                            openCamera();
                        } else {
                            openPhotoPicker(maxCount, selectPath);
                        }
                    }
                })
                .show();
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        requestPermissions(false, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 打开相册选择器
     *
     * @param count
     */
    public void openPhotoPicker(int count, ArrayList<String> paths) {
        PhotoPicker.builder()
                .setPhotoCount(count)
                .setShowCamera(false)
                .setSelected(paths)
                .start(BasePhotoPickerActivity.this);
    }

    /**
     * 打开预览
     *
     * @param photos
     * @param position
     * @param delete
     */
    public void openPreview(ArrayList<String> photos, ArrayList<String> thumbnails, int position, boolean delete) {
        PhotoPreview.builder()
                .setPhotos(photos)
                .setThumbnails(thumbnails)
                .setCurrentItem(position)
                .setShowDeleteButton(delete)
                .start(this);
    }

    private void openCameraOnRight() {
        if (captureManager == null) {
            captureManager = new ImageCaptureManager(this);
        }
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO) {
                    if (captureManager == null) {
                        captureManager = new ImageCaptureManager(this);
                    }
                    captureManager.galleryAddPic();
                    String path = captureManager.getCurrentPhotoPath();
                    if (!TextUtils.isEmpty(path) && listener != null) {
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(path);
                        listener.onPhotoChoose(requestCode, paths);
                    }
                } else if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) {
                    ArrayList<String> photos = null;
                    if (data != null) {
                        photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    }
                    if (photos != null && listener != null) {
                        listener.onPhotoChoose(requestCode, photos);
                    }

                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtil.show(this, requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO ? R.string.photo_camera_error : R.string.photo_choose_error);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (captureManager != null) {
            captureManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (captureManager != null) {
            captureManager.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        openCameraOnRight();
    }

    public void setOnPhotoChooseListener(OnPhotoChooseListener listener) {
        this.listener = listener;
    }

    public interface OnPhotoChooseListener {
        void onPhotoChoose(int requestCode, ArrayList<String> photos);
    }
}
