package com.zhiqi.campusassistant.ui.bedroom.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.Contents;
import com.google.zxing.client.Intents;
import com.google.zxing.client.encode.QRCodeEncoder;
import com.ming.base.rx2.SimpleObserver;
import com.ming.base.util.AppUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.bedroom.entity.BedRoomDetail;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ming on 2017/8/27.
 * 床位选择详情
 */

public class BedChosenDetailActivity extends BaseLoadActivity<Bitmap> {

    @BindView(R.id.bedroom)
    TextView bedRoomTxt;

    @BindView(R.id.location)
    TextView locationTxt;

    @BindView(R.id.bedroom_type)
    TextView typeNameTxt;

    @BindView(R.id.bedroom_img)
    ImageView roomImg;

    @BindView(R.id.student_no)
    TextView studentNoTxt;

    @BindView(R.id.qr_code)
    ImageView qrCodeImg;

    private BedRoomDetail detail;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_bed_chosen_detail;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initView();
    }

    public void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            detail = intent.getParcelableExtra(AppConstant.EXTRA_BEDROOM_DETAIL);
        }
        if (detail == null) {
            finish();
            return;
        }
        refresh();
    }

    @Override
    protected void onRefresh() {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                try {
                    int dimension = getResources().getDimensionPixelSize(R.dimen.bed_qr_code);
                    Intent intent = new Intent(Intents.Encode.ACTION);
                    intent.putExtra(Intents.Encode.DATA, detail.qr_code);
                    intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
                    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
                    QRCodeEncoder encoder = new QRCodeEncoder(BedChosenDetailActivity.this, intent, dimension, false);
                    emitter.onNext(encoder.encodeAsBitmap());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    emitter.onError(ex);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SimpleObserver.create(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        onLoadData(bitmap);
                    }
                }));
    }

    @Override
    public void onLoadData(Bitmap data) {
        showEmptyView(false);
        GlideApp.with(this).load(detail.image).placeholder(R.drawable.ic_img_rectangle_default).into(roomImg);
        bedRoomTxt.setText(getString(R.string.common_two_string, detail.room, detail.bed_name));
        locationTxt.setText(detail.location);
        typeNameTxt.setText(detail.type_name);
        studentNoTxt.setText(getString(R.string.bedroom_student_no, detail.student_no));

        if (data != null) {
            qrCodeImg.setImageBitmap(data);
        }
    }

    @OnClick(R.id.call)
    public void onClick() {
        if (detail != null && !TextUtils.isEmpty(detail.service_tel)) {
            requestPermissions(false, Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        AppUtil.callTel(this, detail.service_tel);
    }
}
