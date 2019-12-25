package com.zhiqi.campusassistant.core.user.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.user.entity.ContactsList;
import com.zhiqi.campusassistant.core.user.entity.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2016/11/30.
 * 联系人API
 */

public interface UserInfoApiService {


    /**
     * 查询联系人列表
     *
     * @param departmentId 部门id
     * @return
     */
    @GET(HttpUrlConstant.QUERY_CONTACTS_LIST)
    Observable<BaseResultData<ContactsList>> queryContactsList(@Query("parent_id") Integer departmentId);

    /**
     * 查询用户个人详细信息
     *
     * @param userId   用户id
     * @param userRole 角色id
     * @return
     */
    @GET(HttpUrlConstant.QUERY_USER_DETAILS)
    Observable<BaseResultData<UserInfo>> queryUserDetails(@Query("contacts_id") long userId, @Query("user_role") int userRole);

    /**
     * 搜索联系人
     *
     * @return
     */
    @GET(HttpUrlConstant.SEARCH_CONTACTS)
    Observable<BaseResultData<ContactsList>> searchUsers(@Query("key_word") String keyWord);
}
