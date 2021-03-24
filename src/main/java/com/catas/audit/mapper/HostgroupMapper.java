package com.catas.audit.mapper;

import com.catas.audit.entity.Hostgroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.HostGroupVo;
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
public interface HostgroupMapper extends BaseMapper<Hostgroup> {

    // 获取所有绑定主机组信息
    List<Map<String, Object>> getAllGroupInfo(HostGroupVo hostGroupVo);

    // 获取所有绑定组的 id
    Set<Integer> queryRelatedGroupIds(@Param("userId") Integer id);

    // 删除用户所有绑定主机组
    void deleteRelatedGroupByUserId(@Param("userId") Integer id);

    // 新增用户绑定主机组
    void saveRelatedGroups(@Param("userId") Integer uId, @Param("hostGroupList") List<Integer> gIds);

    // 获取当前组所有绑定的 bind-host id
    Set<Integer> getAllRelatedBindHostIds(@Param("groupId") Integer groupId);

    // 删除当前组所有的 bindhost
    void deleteAllRelatedBindHosts(@Param("groupId") Integer groupId);

    // 新租绑定 bind-host 关系
    void saveRelatedBindHosts(@Param("groupId") Integer gId, @Param("bindHostIds") List<Integer> hIds);
}
