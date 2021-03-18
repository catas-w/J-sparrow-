package com.catas.audit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.RelatedHostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
@Mapper
public interface BindhostMapper extends BaseMapper<Bindhost> {

    IPage<RelatedHostDto> queryBindHostsByUserId(
            Page<RelatedHostDto> page,
         @Param("userId") Integer userId,
         @Param("hostName") String hostName,
         @Param("idc") String idc
            );

    // 根据用户id获取直接关联的 bindHost id
    List<Integer> queryRelatedHostIds(@Param("userId")  Integer userId);

    // 根据用户id获取通过主机组关联的 bindHost id
    List<Integer> queryRelatedHostIdsFromGroup(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    // 获取用户全部关联 bindHost id
    Set<Integer> queryAllRelatedHostIds(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    // 获取主机登录信息
    Map<String, String> getHostLoginInfo(@Param("id") Integer bindHostId);
}
