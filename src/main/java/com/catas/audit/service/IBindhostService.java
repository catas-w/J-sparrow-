package com.catas.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.audit.vo.RelatedHostVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
public interface IBindhostService extends IService<Bindhost> {

    // 根据条件查询直接绑定的主机信息
    IPage<RelatedHostDto> queryRelatedHosts(Page<RelatedHostDto> page, RelatedHostVo relatedHostVo);

    // 根据用户id获取直接关联的 bindHost id
    List<Integer> queryRelatedHostIds(Integer userId);

    // 根据用户id获取通过主机组关联的 bindHost id
    List<Integer> queryRelatedHostIdsFromGroup(Integer userId, Integer groupId);

    // 获取用户全部关联 bindHost id
    Set<Integer> queryAllRelatedHostIds(Integer userId, Integer groupId);

    Set<Integer> queryAllRelatedHostIds(Integer userId);

    Map<String, String> getHostLoginInfo(Integer bindHostId);
}
