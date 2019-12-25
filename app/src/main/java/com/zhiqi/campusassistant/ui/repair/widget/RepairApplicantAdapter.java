package com.zhiqi.campusassistant.ui.repair.widget;

import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.widget.BaseRadioButtonAdapter;
import com.zhiqi.campusassistant.core.repair.entity.CampusDistrict;
import com.zhiqi.campusassistant.core.repair.entity.CampusSubArea;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplicantInfo;
import com.zhiqi.campusassistant.core.repair.entity.RepairDictionary;

/**
 * Created by ming on 2017/3/30.
 * 常用申请用户adapter
 */

public class RepairApplicantAdapter extends BaseRadioButtonAdapter<RepairApplicantInfo> {

    private RepairDictionary dictionary;

    public RepairApplicantAdapter() {
        super(R.layout.item_repair_applicant, true);
    }

    public void setRepairDictionary(RepairDictionary dictionary) {
        this.dictionary = dictionary;
        if (mData != null && !mData.isEmpty()) {
            notifyDataSetChanged();
        }
    }

    @Override
    protected void convertContent(BaseViewHolder helper, RepairApplicantInfo item, int position) {
        if (item != null) {
            helper.setText(R.id.applicant_name, item.applicant_name);
            helper.setText(R.id.phone_number, item.phone);
            StringBuffer address = new StringBuffer();
            if (dictionary != null) {
                CampusSubArea subArea = dictionary.campus_subarea;
                if (subArea != null && subArea.items != null) {
                    if (item.district_id > 0) {
                        for (CampusDistrict district : subArea.items) {
                            if (item.district_id == district.district_id) {
                                address.append(district.value + " ");
                                if (item.location_id > 0 && district.subareas != null) {
                                    for (CampusDistrict.CampusLocation location : district.subareas) {
                                        if (item.location_id == location.location_id) {
                                            address.append(location.value + " ");
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            address.append(item.address);
            helper.setText(R.id.address, address);
        }
    }
}
