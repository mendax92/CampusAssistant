package com.zhiqi.campusassistant.ui.bedroom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.bedroom.entity.BedChooseInfo;
import com.zhiqi.campusassistant.core.bedroom.entity.BedInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ming on 2017/8/27.
 * 床位选择布局
 */

public class BedChooseLayout extends ScrollView {

    @BindView(R.id.left_layout)
    LinearLayout leftLayout;

    @BindView(R.id.right_layout)
    LinearLayout rightLayout;

    private List<BedInfo> leftBeds;

    private List<BedInfo> rightBeds;

    private BedChooseInfo bedChoose;

    private BedSingleView checkedView;

    private OnCheckChangedListener onCheckChangedListener;

    public BedChooseLayout(Context context) {
        this(context, null);
    }

    public BedChooseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BedChooseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFillViewport(true);
        LayoutInflater.from(getContext()).inflate(R.layout.view_bed_choose_layout, this, true);
        ButterKnife.bind(this);
    }

    public void setData(BedChooseInfo bedChoose) {
        this.bedChoose = bedChoose;
        fillData();
    }

    /**
     * 填充数据
     */
    private void fillData() {
        leftLayout.removeAllViews();
        if (leftBeds != null) {
            leftBeds.clear();
        }
        rightLayout.removeAllViews();
        if (rightBeds != null) {
            rightBeds.clear();
        }
        List<BedInfo> beds = bedChoose.beds;
        if (beds != null) {
            leftBeds = new ArrayList<>();
            rightBeds = new ArrayList<>();

            // 计算床位总数量
            int bedSize;
            if (bedChoose.is_up_down) {
                // 如果是上下铺，床位数量 = 床铺数量 ／ 2
                bedSize = (int) Math.ceil((double) beds.size() / 2);
            } else {
                // 非上下铺，床位数量 = 床铺数量
                bedSize = beds.size();
            }
            // 计算左右两边床铺最大数量
            int bunkSize = (int) Math.ceil((double) bedSize / 2);
            leftBeds = getLeftBeds(beds, bedChoose.is_up_down, bunkSize);
            rightBeds = getRightBeds(beds, bedChoose.is_up_down, bunkSize);
            putBeds(leftBeds, bunkSize, bedChoose.is_up_down, leftLayout);
            putBeds(rightBeds, bunkSize, bedChoose.is_up_down, rightLayout);
        }
    }

    public void putBeds(List<BedInfo> beds, int bunkSize, boolean isDoubleBunk, LinearLayout singleLayout) {
        if (beds == null || beds.isEmpty()) {
            return;
        }
        for (int i = 0; i < beds.size(); i++) {
            BedInfo bed = beds.get(i);
            BedSingleView bedView = new BedSingleView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            bedView.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.bed_choose_min_height));
            if (i > 0 && isDoubleBunk && i % 2 == 0) {
                params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.common_margin_normal), 0, 0);
            }
            bedView.setBedInfo(bed);
            bedView.setOnCheckedListener(checkedListener);
            singleLayout.addView(bedView, params);
        }
        int bedSize = isDoubleBunk ? bunkSize * 2 : bunkSize;
        if (beds.size() < bedSize) {
            for (int i = beds.size(); i < bedSize; i++) {
                View emptyView = new View(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                emptyView.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.bed_choose_min_height));
                if (i > 0 && isDoubleBunk && i % 2 == 0) {
                    params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.common_margin_normal), 0, 0);
                }
                singleLayout.addView(emptyView, params);
            }
        }
    }

    private BedSingleView.OnCheckedListener checkedListener = new BedSingleView.OnCheckedListener() {
        @Override
        public void onChecked(BedSingleView view, boolean checked) {
            if (checkedView != null && view != checkedView && checked) {
                checkedView.setChecked(false);
            }
            checkedView = view;
            if (onCheckChangedListener != null) {
                onCheckChangedListener.onCheckChangedListener(checkedView.getBedInfo(), checked);
            }
        }
    };

    /**
     * 获取左边床位数量，以左边床位为准，为最大单边数量
     * 左边床位，排序为顺时针
     *
     * @param beds         总共床位信息
     * @param isDoubleBunk 是否为上下铺
     * @param bunkSize     单边床位数量
     * @return
     */
    public List<BedInfo> getLeftBeds(List<BedInfo> beds, boolean isDoubleBunk, int bunkSize) {
        List<BedInfo> bedInfos = new ArrayList<>();
        int index = beds.size() - (isDoubleBunk ? bunkSize * 2 : bunkSize);
        for (int i = 0; i < bunkSize; i++) {
            // 如果是上下铺，则多加一个床位
            if (isDoubleBunk) {
                bedInfos.add(beds.get(index++));
            }
            bedInfos.add(beds.get(index++));
        }
        return bedInfos;
    }

    /**
     * 获取右边床位数量，右边数量可能小于左边数量，空出右下角
     * 右边排序，非上下铺为顺时针，上下铺总体为顺时针，但先排上铺再排下铺
     *
     * @param beds         总共床位信息
     * @param isDoubleBunk 是否为上下铺
     * @param bunkSize     单边床位数量
     * @return
     */
    public List<BedInfo> getRightBeds(List<BedInfo> beds, boolean isDoubleBunk, int bunkSize) {
        List<BedInfo> bedInfos = new ArrayList<>();
        int index = beds.size() - (isDoubleBunk ? bunkSize * 2 : bunkSize) - 1;
        for (int i = 0; i < bunkSize; i++) {
            // 如果是上下铺，则多加一个床位
            if (isDoubleBunk) {
                if (index < 0) {
                    break;
                }
                bedInfos.add(beds.get(index--));
                if (index < 0) {
                    break;
                }
                bedInfos.add(bedInfos.size() - 1, beds.get(index--));
            } else {
                if (index < 0) {
                    break;
                }
                bedInfos.add(beds.get(index--));
            }
        }
        return bedInfos;
    }

    public void setOnCheckChangedListener(OnCheckChangedListener onCheckChangedListener) {
        this.onCheckChangedListener = onCheckChangedListener;
    }

    public interface OnCheckChangedListener {
        void onCheckChangedListener(BedInfo bedInfo, boolean checked);
    }
}
