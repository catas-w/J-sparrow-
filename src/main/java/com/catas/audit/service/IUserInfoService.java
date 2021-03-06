package com.catas.audit.service;

import com.catas.audit.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-03-14
 */
public interface IUserInfoService extends IService<UserInfo> {

    List<Integer> queryGroupIdsByUserId(Integer id);

    /**
     * @Description: 获取用户所有权限信息
     */
    List<String> getPermissionsById(@Param("userId") Integer id);
}
