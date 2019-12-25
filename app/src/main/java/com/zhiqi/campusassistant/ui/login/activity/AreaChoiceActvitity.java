package com.zhiqi.campusassistant.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.usercenter.entity.AreaInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ming on 2016/11/24.
 * 区域选择
 */

public class AreaChoiceActvitity extends BaseToolbarActivity {

    @BindView(R.id.area_list)
    RecyclerView mRecyclerView;

    private AreaInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_choice);
        init();
    }

    private void init() {
        List<AreaInfo> areaInfos = getAreadInfo();
        mAdapter = new AreaInfoAdapter(areaInfos);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(AppConstant.EXTRA_AREA_INFO, mAdapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private List<AreaInfo> getAreadInfo() {
        List<AreaInfo> areaInfos = new ArrayList<>();
        String[] areaList = getResources().getStringArray(R.array.area_list);
        for (String anAreaList : areaList) {
            String[] areas = anAreaList.split(",");
            areaInfos.add(new AreaInfo(areas[0], areas[1]));
        }
        return areaInfos;
    }


    class AreaInfoAdapter extends BaseQuickAdapter<AreaInfo> {

        public AreaInfoAdapter(List<AreaInfo> data) {
            super(R.layout.item_area_info, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AreaInfo item, int position) {
            helper.setText(R.id.area_code, item.areaCode)
                    .setText(R.id.area_state, item.areaState);
        }
    }

}
