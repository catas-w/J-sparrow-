package com.catas.audit.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.catas.audit.mapper.BindhostMapper;
import com.catas.audit.service.IBindhostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catas.audit.vo.RelatedHostVo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
public class BindhostServiceImpl extends ServiceImpl<BindhostMapper, Bindhost> implements IBindhostService {


    @Override
    public IPage<RelatedHostDto> queryRelatedHosts(Page<RelatedHostDto> page, RelatedHostVo relatedHostVo) {
        return this.getBaseMapper().queryBindHostsByUserId(page, relatedHostVo.getUserId(),
                relatedHostVo.getHostName(), relatedHostVo.getIdc());
    }

    @Override
    public IPage<RelatedHostDto> queryBindHostsByUserGroup(Page<RelatedHostDto> page, RelatedHostVo relatedHostVo) {
        BindhostMapper baseMapper = this.getBaseMapper();
        return baseMapper.queryBindHostsByUserGroup(page, relatedHostVo.getUserId(),
                relatedHostVo.getGroupId(), relatedHostVo.getHostName(), relatedHostVo.getIdc());
    }

    @Override
    public List<Integer> queryRelatedHostIds(Integer userId) {
        return this.getBaseMapper().queryRelatedHostIds(userId);
    }

    @Override
    public List<Integer> queryRelatedHostIdsFromGroup(Integer userId, Integer groupId) {
        return this.getBaseMapper().queryRelatedHostIdsFromGroup(userId, groupId);
    }

    @Override
    public Set<Integer> queryAllRelatedHostIds(Integer userId, Integer groupId) {
        return this.getBaseMapper().queryAllRelatedHostIds(userId, groupId);
    }

    @Override
    public Set<Integer> queryAllRelatedHostIds(Integer userId) {
        return this.queryAllRelatedHostIds(userId, null);
    }

    @Override
    public Map<String, String> getHostLoginInfo(Integer bindHostId) {
        return this.getBaseMapper().getHostLoginInfo(bindHostId);
    }
}
