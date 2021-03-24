package com.catas.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.audit.vo.BindHostVo;
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

    // 查询用户绑定组中所有主机信息
    IPage<RelatedHostDto> queryBindHostsByUserGroup(Page<RelatedHostDto> page, RelatedHostVo relatedHostVo);

    // // 根据用户id获取直接关联的 bindHost id
    // List<Integer> queryRelatedHostIds(Integer userId);
    //
    // // 根据用户id获取通过主机组关联的 bindHost id
    // List<Integer> queryRelatedHostIdsFromGroup(Integer userId, Integer groupId);

    // 获取用户全部关联 bindHost id
    Set<Integer> queryAllRelatedHostIds(Integer userId, Integer groupId);

    Set<Integer> queryAllRelatedHostIds(Integer userId);

    Map<String, String> getHostLoginInfo(Integer bindHostId);

    // 获取所有绑定主机信息 -- 个人信息
    List<Map<String, Object>> getAllHostInfo();

    // 获取所有bind host -- bindHost 管理
    List<Map<String, Object>> getAllBindHost(BindHostVo bindHostVo);

    // 更新用户绑定的主机
    void updateUserBindHosts(Integer uId, List<Integer> bIds);
}
