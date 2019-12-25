package com.zhiqi.campusassistant.core.login.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;
import com.zhiqi.campusassistant.core.user.entity.Gender;
import com.zhiqi.campusassistant.core.user.entity.GenderConverter;
import com.zhiqi.campusassistant.core.user.entity.UserRole;
import com.zhiqi.campusassistant.core.user.entity.UserRoleConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Edmin on 2016/11/5 0005.
 * 登录用户
 */
@Entity
public class LoginUser implements BaseJsonData {

    @Id
    private long user_id;

    private String token;

    @Convert(converter = UserRoleConverter.class, columnType = Integer.class)
    private UserRole role_type;

    private String user_no;

    /**
     * 用户真实姓名，与之相对应使用的是真实头像
     */
    private String real_name;

    /**
     * 用户真实头像，主要用于一些比较正式的场景，比如：请假、报修等等
     */
    private String head;

    private int faculty_id;

    private String faculty;

    private String position;

    private String phone;

    @Convert(converter = GenderConverter.class, columnType = Integer.class)
    private Gender gender;

    private String school_badge;

    private String school_name;

    /**
     * 虚拟头像，主要用于一些带社交属性的场景，比如：失物招领、同学圈等等
     */
    private String avatar;

    private String nickname;

    @Generated(hash = 963831827)
    public LoginUser(long user_id, String token, UserRole role_type, String user_no,
            String real_name, String head, int faculty_id, String faculty,
            String position, String phone, Gender gender, String school_badge,
            String school_name, String avatar, String nickname) {
        this.user_id = user_id;
        this.token = token;
        this.role_type = role_type;
        this.user_no = user_no;
        this.real_name = real_name;
        this.head = head;
        this.faculty_id = faculty_id;
        this.faculty = faculty;
        this.position = position;
        this.phone = phone;
        this.gender = gender;
        this.school_badge = school_badge;
        this.school_name = school_name;
        this.avatar = avatar;
        this.nickname = nickname;
    }

    @Generated(hash = 1159929338)
    public LoginUser() {
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRole getRole_type() {
        return this.role_type;
    }

    public void setRole_type(UserRole role_type) {
        this.role_type = role_type;
    }

    public String getUser_no() {
        return this.user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getReal_name() {
        return this.real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getHead() {
        return this.head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getFaculty_id() {
        return this.faculty_id;
    }

    public void setFaculty_id(int faculty_id) {
        this.faculty_id = faculty_id;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSchool_badge() {
        return this.school_badge;
    }

    public void setSchool_badge(String school_badge) {
        this.school_badge = school_badge;
    }

    public String getSchool_name() {
        return this.school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
