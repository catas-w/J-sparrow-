package com.catas.audit.service;

import com.catas.audit.entity.AuthGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-04-18
 */
public interface IAuthGroupService extends IService<AuthGroup> {

    void updateGroupPerms(Integer groupId, List<Integer> permIdList);
}
