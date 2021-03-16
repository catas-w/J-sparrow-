package com.catas.audit.service.impl;

import com.catas.audit.entity.UserInfo;
import com.catas.audit.mapper.UserInfoMapper;
import com.catas.audit.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-03-14
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Override
    public List<Integer> queryGroupIdsByUserId(Integer id) {
        return this.getBaseMapper().queryGroupIdsByUserId(id);
    }


}
