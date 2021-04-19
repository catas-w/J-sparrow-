package com.catas.audit.service.impl;

import com.catas.audit.entity.AuthGroup;
import com.catas.audit.mapper.AuthGroupMapper;
import com.catas.audit.service.IAuthGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-04-18
 */
@Service
public class AuthGroupServiceImpl extends ServiceImpl<AuthGroupMapper, AuthGroup> implements IAuthGroupService {

    @Override
    public void updateGroupPerms(Integer groupId, List<Integer> permIdList) {
        this.getBaseMapper().clearGroupPerms(groupId);
        if (permIdList != null && !permIdList.isEmpty())
            this.getBaseMapper().addGroupPerms(groupId, permIdList);
    }
}
