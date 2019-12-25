package com.zhiqi.campusassistant.ui.user.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ming.base.util.Log;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.user.entity.UserRole;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ming on 2016/10/10.
 * 用户卡片view
 */

public class UserInfoCardView extends CardView {

    @BindView(R.id.user_name)
    TextView userNameText;

    @BindView(R.id.user_number_label)
    TextView numberLabel;

    @BindView(R.id.user_number_text)
    TextView numberText;

    @BindView(R.id.user_department_label)
    TextView departmentLabel;

    @BindView(R.id.user_department)
    TextView departmentName;

    @BindView(R.id.user_grade_class_label)
    TextView classLabel;

    @BindView(R.id.user_grade_class)
    TextView classNameText;

    @BindView(R.id.user_header)
    ImageView userHeader;

    @BindView(R.id.user_school_logo)
    ImageView schoolLogo;

    @BindView(R.id.user_school_name)
    TextView schoolName;

    private LoginUser user;

    public UserInfoCardView(Context context) {
        super(context);
        init();
    }

    public UserInfoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserInfoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setRadius(getContext().getResources().getDimension(R.dimen.user_card_radius));
        setContentPadding(0, 0, 0, 0);
        setPreventCornerOverlap(false);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_user_info_card, this);
        ButterKnife.bind(rootView);
    }

    private void initData() {
        if (user == null) {
            return;
        }
        schoolName.setText(user.getSchool_name());
        GlideApp.with(getContext()).load(user.getSchool_badge()).placeholder(R.drawable.ic_school_logo_1).into(schoolLogo);
        userNameText.setText(user.getReal_name());
        Log.i("UserInfoCardView", "role type:" + user.getRole_type());
        if (UserRole.Student == user.getRole_type()) {
            numberLabel.setText(R.string.user_student_number);
            departmentLabel.setText(R.string.user_department);
            classLabel.setText(R.string.user_grade_class);
        } else {
            numberLabel.setText(R.string.user_staff_number);
            departmentLabel.setText(R.string.user_staff_department);
            classLabel.setText(R.string.user_position);
        }
        numberText.setText(user.getUser_no());
        departmentName.setText(user.getFaculty());
        classNameText.setText(user.getPosition());
        GlideApp.with(getContext()).load(user.getHead()).placeholder(R.drawable.img_user_default_head)
                .into(userHeader);

    }

    public void setUser(LoginUser user) {
        this.user = user;
        initData();
    }
}
