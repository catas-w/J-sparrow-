package com.catas.audit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.BindHostVo;
import com.catas.audit.vo.RelatedHostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.nio.file.Path;
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

    // 查询用户绑定组中所有主机信息
    IPage<RelatedHostDto> queryBindHostsByUserGroup(
            Page<RelatedHostDto> page,
            @Param("userId") Integer userId,
            @Param("groupId") Integer groupId,
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

    // 获取所有绑定主机信息 -- 个人信息
    List<Map<String, Object>> getAllHostInfo();

    // 获取所有bind host -- bindHost 管理
    List<Map<String, Object>> getAllBindHost(BindHostVo bindHostVo);

    // 删除用户所有绑定主机
    void deleteRelatedHostByUserId(@Param("userId") Integer id);

    // 新建用户绑定主机
    void saveRelatedHost(@Param("userId") Integer uId, @Param("bindHostIdList") List<Integer> bIds);
}
