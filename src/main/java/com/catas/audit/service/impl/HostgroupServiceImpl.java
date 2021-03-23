package com.catas.audit.service.impl;

import com.catas.audit.entity.Hostgroup;
import com.catas.audit.mapper.HostgroupMapper;
import com.catas.audit.service.IHostgroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
@Service
public class HostgroupServiceImpl extends ServiceImpl<HostgroupMapper, Hostgroup> implements IHostgroupService {

    @Override
    public List<Map<String, Object>> getAllGroupInfo() {
        return this.baseMapper.getAllGroupInfo();
    }

    @Override
    public Set<Integer> queryRelatedGroupIds(Integer id) {
        return this.baseMapper.queryRelatedGroupIds(id);
    }

    @Override
    public void updateRelatedGroups(Integer id, List<Integer> gIds) {
        HostgroupMapper baseMapper = this.baseMapper;
        baseMapper.deleteRelatedGroupByUserId(id);
        baseMapper.saveRelatedGroups(id, gIds);
    }
}
