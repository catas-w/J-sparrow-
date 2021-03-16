package com.catas.audit.service;

import com.catas.audit.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

}
