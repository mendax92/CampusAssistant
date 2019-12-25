package com.zhiqi.campusassistant.common.ui.widget;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;

import java.util.List;

/**
 * Created by ming on 2017/3/30.
 * 基础选择adapter
 */

public abstract class BaseRadioButtonAdapter<T> extends BaseQuickAdapter<T> {

    private int mContentLayoutResId;

    private LayoutInflater inflater;

    private int checkedPosition = -1;

    private boolean triggerWholeItem = false;

    private OnCheckedChangeListener onCheckedChangeListener;

    public BaseRadioButtonAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, true);
    }

    public BaseRadioButtonAdapter(@LayoutRes int layoutResId, boolean triggerWholeItem) {
        this(layoutResId, triggerWholeItem, null);
    }

    public BaseRadioButtonAdapter(@LayoutRes int layoutResId, boolean triggerWholeItem, List<T> data) {
        super(R.layout.item_common_radio_btn, data);
        this.triggerWholeItem = triggerWholeItem;
        mContentLayoutResId = layoutResId;
    }

    @Override
    protected void convert(BaseViewHolder helper, T item, int position) {
        ViewGroup convertView = (ViewGroup) helper.getConvertView();
        View view = convertView.getChildAt(1);
        if (view == null) {
            if (inflater == null) {
                inflater = LayoutInflater.from(mContext);
            }
            view = inflater.inflate(mContentLayoutResId, convertView, true);
        }
        RadioButton button = helper.getView(R.id.radio_btn);
        button.setChecked(checkedPosition == position);
        convertContent(helper, item, position);
        View layout;
        if (triggerWholeItem) {
            layout = convertView;
        } else {
            layout = helper.getView(R.id.radio_layout);
        }
        layout.setTag(R.id.radio_layout, position);
        layout.setOnClickListener(radioListener);
    }

    private View.OnClickListener radioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.radio_layout);
            if (tag != null && tag instanceof Integer) {
                int position = (int) tag;
                checkItem(position);
            }
        }
    };

    public void checkItem(int position) {
        if (checkedPosition != position) {
            int lastChecked = checkedPosition;
            this.checkedPosition = position;
            notifyItemChanged(lastChecked);
            notifyItemChanged(position);
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onChecked(checkedPosition);
            }
        }
    }


    protected abstract void convertContent(BaseViewHolder helper, T item, int position);

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onChecked(int position);
    }
}
