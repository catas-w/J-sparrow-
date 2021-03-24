package com.catas.audit.service.impl;

import com.catas.audit.entity.Hostgroup;
import com.catas.audit.mapper.HostgroupMapper;
import com.catas.audit.service.IHostgroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catas.audit.vo.HostGroupVo;
import org.apache.ibatis.annotations.Param;
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
    public List<Map<String, Object>> getAllGroupInfo(HostGroupVo hostGroupVo) {
        return this.baseMapper.getAllGroupInfo(hostGroupVo);
    }

    @Override
    public List<Map<String, Object>> getAllGroupInfo() {
        return getAllGroupInfo(null);
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

    @Override
    public Set<Integer> getAllRelatedBindHostIds(Integer groupId) {
        return this.getBaseMapper().getAllRelatedBindHostIds(groupId);
    }

    @Override
    public void updateRelatedBindHosts(HostGroupVo hostGroupVo) {
        HostgroupMapper baseMapper = this.getBaseMapper();
        baseMapper.deleteAllRelatedBindHosts(hostGroupVo.getId());
        baseMapper.saveRelatedBindHosts(hostGroupVo.getId(), hostGroupVo.getBindHostIds());
    }
}
