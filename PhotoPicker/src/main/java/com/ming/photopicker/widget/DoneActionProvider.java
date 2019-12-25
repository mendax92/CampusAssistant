package com.ming.photopicker.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ming.photopicker.R;

/**
 * Created by ming on 2017/2/22.
 */

public class DoneActionProvider extends ActionProvider {

    private TextView menu;

    private CharSequence title;

    private boolean enabled = true;

    private OnMenuClickListener onMenuClickListener;

    public DoneActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.photo_picker_menu_view, null);
        menu = (TextView) rootView.findViewById(R.id.menu);
        menu.setText(title);
        menu.setEnabled(enabled);
        menu.setOnClickListener(onMenuClickListener);
        return rootView;
    }

    @Override
    public boolean onPerformDefaultAction() {
        return true;
    }

    public void setTitle(@StringRes int resId) {
        title = getContext().getString(resId);
        if (menu != null) {
            menu.setText(title);
        }
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        if (menu != null) {
            menu.setText(title);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (menu != null) {
            menu.setEnabled(enabled);
        }
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
        if (menu != null) {
            menu.setOnClickListener(onMenuClickListener);
        }
    }

    public interface OnMenuClickListener extends View.OnClickListener {
        void onClick(View v);
    }

    public TextView getMenu() {
        return menu;
    }
}
