package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.zhiqi.campusassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by ming on 17-8-4.
 * 支付选择view
 */

public class PayCheckGroupView extends LinearLayoutCompat {

    public static final int CHECK_WECHAT = 0;

    public static final int CHECK_ALI_PAY = 1;

    @BindView(R.id.pay_wechat)
    RadioButton payWechat;
    @BindView(R.id.pay_ali)
    RadioButton payAli;

    @BindView(R.id.pay_ali_view)
    View payAliLayout;
    @BindView(R.id.pay_wechat_view)
    View payWechatLayout;

    private OnCheckedChangeListener onCheckedChangeListener;

    public PayCheckGroupView(Context context) {
        this(context, null);
    }

    public PayCheckGroupView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayCheckGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_pay_check_group, this, true);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pay_wechat_view, R.id.pay_ali_view})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_wechat_view:
                setChecked(CHECK_WECHAT);
                break;
            case R.id.pay_ali_view:
                setChecked(CHECK_ALI_PAY);
                break;
        }
    }

    @OnCheckedChanged({R.id.pay_ali, R.id.pay_wechat})
    void onCheckedChanged(CompoundButton view, boolean checked) {
        switch (view.getId()) {
            case R.id.pay_ali:
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(CHECK_ALI_PAY, checked);
                }
                break;
            case R.id.pay_wechat:
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(CHECK_WECHAT, checked);
                }
                break;
        }
    }

    /**
     * 设置选中
     * @param index {@link #CHECK_ALI_PAY}, {@link #CHECK_WECHAT}
     */
    public void setChecked(int index) {
        switch (index) {
            case CHECK_WECHAT:
                payWechat.setChecked(true);
                payAli.setChecked(false);
                break;
            case CHECK_ALI_PAY:
                payAli.setChecked(true);
                payWechat.setChecked(false);
                break;
        }
    }

    public int getCheckedPosition() {
        return payWechat.isChecked() ? CHECK_WECHAT : CHECK_ALI_PAY;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * 设置是否可见
     * @param position {@link #CHECK_ALI_PAY}, {@link #CHECK_WECHAT}
     * @param visible
     */
    public void setVisible(int position, boolean visible) {
        switch (position) {
            case CHECK_ALI_PAY:
                payAliLayout.setVisibility(visible ? VISIBLE : GONE);
                break;
            case CHECK_WECHAT:
                payWechatLayout.setVisibility(visible ? VISIBLE : GONE);
                break;
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int index, boolean checked);
    }
}
