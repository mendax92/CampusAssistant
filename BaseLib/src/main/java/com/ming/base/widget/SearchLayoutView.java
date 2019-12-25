package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ming.base.R;

/**
 * Created by ming on 2016/10/23.
 * 自定义SearchView
 */
public class SearchLayoutView extends SearchView {

    private SearchView.SearchAutoComplete searchAutoComplete;

    private ImageView searchIcon;

    private LinearLayout searchFrame;

    private ImageView closeBtn;

    public SearchLayoutView(Context context) {
        super(context);
    }

    public SearchLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SearchLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        searchAutoComplete = (SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchIcon = (ImageView) findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchFrame = (LinearLayout) findViewById(android.support.v7.appcompat.R.id.search_edit_frame);
        closeBtn = (ImageView) findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.search_layout_view);

        int iconMarginLeft = 0;
        int iconMarginRight = 0;
        int editMarginLeft = 0;
        int editMarginRight = 0;
        int editPaddingLeft = 0;
        int editPaddingRight = 0;
        boolean isActionbar = false;
        if (typedArray != null) {
            int iconMargin = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_icon_margin, 0);
            iconMarginLeft = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_icon_margin_left, iconMargin);
            iconMarginRight = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_icon_margin_right, iconMargin);

            int editMargin = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_edit_margin, 0);
            editMarginLeft = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_edit_margin_left, editMargin);
            editMarginRight = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_edit_margin_right, editMargin);

            int editPadding = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_edit_padding, 0);
            editPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_edit_padding_left, editPadding);
            editPaddingRight = typedArray.getDimensionPixelSize(R.styleable.search_layout_view_search_edit_padding_right, editPadding);

            isActionbar = typedArray.getBoolean(R.styleable.search_layout_view_isActionbar, true);
            typedArray.recycle();
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchIcon.getLayoutParams();
        params.leftMargin = iconMarginLeft;
        params.rightMargin = iconMarginRight;
        searchIcon.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) searchAutoComplete.getLayoutParams();
        params.leftMargin = editMarginLeft;
        params.rightMargin = editMarginRight;
        searchAutoComplete.setPadding(editPaddingLeft, 0, editPaddingRight, 0);
        searchAutoComplete.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) searchFrame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        searchFrame.setPadding(0, 0, 0, 0);
        searchFrame.setLayoutParams(params);
        if (!isActionbar) {
            closeBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            setIconified(true);
                            break;
                    }
                    return false;
                }
            });
        }
    }
}
