package com.zhiqi.campusassistant.ui.contacts.activity;

import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.ming.base.util.KeyBoardUtils;
import com.ming.base.widget.AppEditText;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.widget.BaseEmptyView;

/**
 * Created by ming on 17-11-1.
 * 通讯录搜索
 */

public class SearchContactsActivity extends BaseContactsActivity {

    private AppEditText searchView;
    private String keyWord;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initView();
        refresh();
    }

    private void initView() {
        View searchActionBar = setActionbarLayout(R.layout.view_contacts_search);
        searchView = searchActionBar.findViewById(R.id.search_view);
        searchView.addTextChangedListener(onTextChange);
        searchActionBar.findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(searchView, SearchContactsActivity.this);
                search();
            }
        });
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(searchView, this);
        super.finish();
        if (Build.VERSION.SDK_INT < 21) {
            overridePendingTransition(0, R.anim.search_slide_exit);
        }
    }

    private TextWatcher onTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            search();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void search() {
        String text = searchView.getValidText().toString();
        if (text.equals(keyWord)) {
            return;
        }
        keyWord = text;
        mEmptyView.setEmptyStatus(BaseEmptyView.STATUS_LOADING);
        refresh();
    }

    @Override
    protected void onRefresh() {
        if (TextUtils.isEmpty(keyWord)) {
            mRecyclerView.setVisibility(View.GONE);
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setEnabled(true);
            mRecyclerView.setVisibility(View.VISIBLE);
            mPresenter.searchUsers(keyWord, this);
        }
    }
}
