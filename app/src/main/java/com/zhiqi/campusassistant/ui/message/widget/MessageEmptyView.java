package com.zhiqi.campusassistant.ui.message.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ming on 2017/1/8.
 */

public class MessageEmptyView extends FrameLayout {

    @BindView(R.id.empty_tip)
    TextView emptyTip;

    public MessageEmptyView(Context context) {
        this(context, null);
    }

    public MessageEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_message_empty, this, true);
        ButterKnife.bind(rootView);
    }

    public void setEmptyTip(int tipRes) {
        emptyTip.setText(tipRes);
    }

    public void setEmptyTip(CharSequence tip) {
        emptyTip.setText(tip);
    }
}
