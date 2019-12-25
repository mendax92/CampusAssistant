package com.zhiqi.campusassistant.common.ui.widget;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.ming.photopicker.PhotoPicker;
import com.ming.photopicker.utils.ImageCaptureManager;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.glide.GlideRequest;
import com.zhiqi.campusassistant.common.glide.GlideRequests;
import com.zhiqi.campusassistant.common.ui.activity.BasePhotoPickerActivity;
import com.zhiqi.campusassistant.config.AppConstant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ming on 2017/1/23.
 * 图片选择
 */

public class PictureChooseAdapter extends BaseQuickAdapter<ImageData> implements ListPreloader.PreloadModelProvider<ImageData> {

    private final ImageData DEFAULT_IMG = new ImageData("default_img", "default_img");

    private int maxSize = AppConstant.MAX_UPLOAD_PHOTO_SIZE;

    private BasePhotoPickerActivity pickerActivity;

    private boolean editable = true;

    private GlideRequests glide;
    private GlideRequest<Drawable> fullRequest;
    private GlideRequest<Drawable> thumbRequest;

    public PictureChooseAdapter(int maxSize, BasePhotoPickerActivity pickerActivity) {
        this(pickerActivity);
        this.maxSize = maxSize;
    }

    public PictureChooseAdapter(BasePhotoPickerActivity pickerActivity) {
        super(R.layout.item_image, null);
        List<ImageData> list = new ArrayList<>();
        list.add(DEFAULT_IMG);
        setNewData(list);
        this.pickerActivity = pickerActivity;
        pickerActivity.setOnPhotoChooseListener(photoChooseListener);

        this.glide = GlideApp.with(pickerActivity);
        int size = pickerActivity.getResources().getDimensionPixelSize(R.dimen.common_choose_img_size);
        fullRequest = glide.asDrawable()
                .centerCrop()
                .placeholder(R.drawable.ic_img_square_default);
        thumbRequest = glide.asDrawable()
                .override(size)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade());
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageData item, int position) {
        ImageView imageView = helper.getView(R.id.image);
        imageView.setTag(R.id.image, position);
        imageView.setOnClickListener(clickListener);
        if (DEFAULT_IMG == item) {
            imageView.setImageResource(R.drawable.img_pic_add);
        } else {
            fullRequest.load(item.origin)
                    .thumbnail(TextUtils.isEmpty(item.thumbnail) ? null : thumbRequest.load(item.thumbnail))
                    .into(imageView);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pickerActivity == null) {
                return;
            }
            int position = (int) v.getTag(R.id.image);
            Log.i(TAG, "click:" + position);
            ImageData item = getItem(position);
            if (item != null) {
                if (DEFAULT_IMG == item) {
                    int selectSize = 0;
                    ArrayList<String> paths = getOrigin();
                    if (paths != null) {
                        Iterator<String> iterator = paths.iterator();
                        while (iterator.hasNext()) {
                            String path = iterator.next();
                            if (URLUtil.isNetworkUrl(path)) {
                                iterator.remove();
                                selectSize++;
                            }
                        }
                    }
                    pickerActivity.requestChoosePhoto(maxSize - selectSize, paths);
                } else {
                    ArrayList<String> origins = getOrigin();
                    pickerActivity.openPreview(origins, getThumbnails(), position - (origins != null && origins.size() >= maxSize ? 0 : 1), editable);
                }
            }
        }
    };

    private BasePhotoPickerActivity.OnPhotoChooseListener photoChooseListener = new BasePhotoPickerActivity.OnPhotoChooseListener() {
        @Override
        public void onPhotoChoose(int requestCode, ArrayList<String> photos) {
            Log.i(TAG, "requestCode:" + requestCode + ", photos:" + photos);
            List<ImageData> list = new ArrayList<>();
            // 选择图库器不返回网络图片，因此要获得adapter中网络图片

            List<ImageData> data = getData();
            Log.i(TAG, "data:" + data);
            if (data != null) {
                if (PhotoPicker.REQUEST_CODE == requestCode) {
                    for (ImageData img : data) {
                        if (URLUtil.isNetworkUrl(img.origin)) {
                            list.add(img);
                        }
                    }
                } else if (ImageCaptureManager.REQUEST_TAKE_PHOTO == requestCode) {
                    for (ImageData img : data) {
                        list.add(img);
                    }
                }
            }

            if (photos == null || photos.isEmpty()) {
                setNewData(list);
                return;
            }
            Log.i(TAG, "before list:" + list);
            List<String> origins = getOrigin();
            List<String> thumbnails = getThumbnails();
            int position;
            for (String path : photos) {
                ImageData newItem = new ImageData();
                newItem.origin = path;
                if (origins != null && !TextUtils.isEmpty(path) && (position = origins.indexOf(path)) >= 0) {
                    newItem.thumbnail = thumbnails.get(position);
                    Log.i(TAG, "thumbnail:" + newItem.thumbnail);
                }
                list.add(newItem);
            }
            Log.i(TAG, "after list:" + list);

            setNewData(list);
        }
    };

    @Override
    public void setNewData(List<ImageData> data) {
        if (editable) {
            if (data == null || data.isEmpty()) {
                data = new ArrayList<>();
                data.add(DEFAULT_IMG);
            } else if (data.size() < maxSize && DEFAULT_IMG != data.get(0)) {
                data.add(0, DEFAULT_IMG);
            }
        }
        super.setNewData(data);
    }

    @Override
    public void addData(ImageData data) {
        if (editable) {
            if (mData != null && data != null) {
                mData.add(data);
            }
            setNewData(mData);
        } else {
            super.addData(data);
        }
    }

    @Override
    public void addData(List<ImageData> data) {
        if (editable) {
            if (mData != null && data != null) {
                mData.addAll(data);
            }
            setNewData(mData);
        } else {
            super.addData(data);
        }
    }

    @Override
    public List<ImageData> getData() {
        List<ImageData> data = super.getData();
        if (data != null && !data.isEmpty()) {
            data = new ArrayList<>(data);
            if (DEFAULT_IMG == data.get(0)) {
                data.remove(0);
            }
        }
        return data;
    }

    public ArrayList<String> getOrigin() {
        ArrayList<String> list = null;
        List<ImageData> data = getData();
        if (data != null && !data.isEmpty()) {
            list = new ArrayList<>();
            for (ImageData itemData : data) {
                if (!TextUtils.isEmpty(itemData.origin)) {
                    list.add(itemData.origin);
                }
            }
        }
        return list;
    }

    public ArrayList<String> getThumbnails() {
        ArrayList<String> list = null;
        List<ImageData> data = getData();
        if (data != null && !data.isEmpty()) {
            list = new ArrayList<>();
            for (ImageData itemData : data) {
                if (!TextUtils.isEmpty(itemData.origin)) {
                    list.add(itemData.thumbnail);
                }
            }
        }
        return list;
    }

    public int getDataSize() {
        List<ImageData> data = getData();
        return data == null ? 0 : data.size();
    }

    public void setEditable(boolean editable) {
        if (this.editable != editable) {
            this.editable = editable;
            if (!editable && mData != null) {
                mData.remove(mData.size() - 1);
                notifyDataSetChanged();
            }
        } else {
            this.editable = editable;
            setNewData(mData);
        }
    }

    @NonNull
    @Override
    public List<ImageData> getPreloadItems(int position) {
        return mData.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(ImageData item) {
        return fullRequest.load(item.origin)
                .thumbnail(TextUtils.isEmpty(item.thumbnail) ? null : thumbRequest.load(item.thumbnail));
    }
}
