package com.zhiqi.campusassistant.ui.bedroom.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.bedroom.entity.BedInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ming on 2017/8/27.
 * 床位view
 */

public class BedSingleView extends RelativeLayout {

    public static final int STATUS_NORMAL = 0;

    public static final int STATUS_SELECTED = 1;

    public static final int STATUS_DISABLE = 2;

    @BindView(R.id.bed_name)
    TextView bedNameTxt;

    @BindView(R.id.bed_img)
    ImageView bedImg;

    @BindView(R.id.bed_layout)
    View layout;

    private BedInfo bedInfo;

    private boolean isChecked;

    private OnCheckedListener onCheckedListener;


    public BedSingleView(Context context) {
        this(context, null);
    }

    public BedSingleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BedSingleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bed_single, this, true);
        ButterKnife.bind(this);
    }

    public void setBedInfo(BedInfo bedInfo) {
        this.bedInfo = bedInfo;
        setBedInfo(bedInfo.selected ? BedSingleView.STATUS_DISABLE : BedSingleView.STATUS_NORMAL, bedInfo.bed_name);
    }

    private void setBedInfo(int status, String bedName) {
        switch (status) {
            case STATUS_NORMAL:
                bedImg.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.img_bed_choose_normal));
                bedNameTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray_dark_color));
                layout.setEnabled(true);
                break;
            case STATUS_SELECTED:
                bedImg.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.img_bed_choose_checked));
                bedNameTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                layout.setEnabled(true);
                break;
            case STATUS_DISABLE:
                bedImg.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.img_bed_choose_disable));
                bedNameTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                layout.setEnabled(false);
                break;
        }
        bedNameTxt.setText(bedName);
    }

    @OnClick(R.id.bed_layout)
    void onClick(View view) {
        setChecked(!isChecked);
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        setBedInfo(checked ? STATUS_SELECTED : STATUS_NORMAL, bedInfo.bed_name);
        if (onCheckedListener != null) {
            onCheckedListener.onChecked(this, checked);
        }
    }

    public BedInfo getBedInfo() {
        return bedInfo;
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public interface OnCheckedListener {
        void onChecked(BedSingleView view, boolean checked);
    }

}
