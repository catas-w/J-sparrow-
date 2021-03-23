package com.catas.audit.mapper;

import com.catas.audit.entity.Hostgroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<Map<String, Object>> getAllGroupInfo();

    // 获取所有绑定组的 id
    Set<Integer> queryRelatedGroupIds(@Param("userId") Integer id);

    // 删除用户所有绑定主机组
    void deleteRelatedGroupByUserId(@Param("userId") Integer id);

    // 新增用户绑定主机组
    void saveRelatedGroups(@Param("userId") Integer uId, @Param("hostGroupList") List<Integer> gIds);

}
