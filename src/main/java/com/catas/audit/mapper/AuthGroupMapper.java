package com.catas.audit.mapper;

import com.catas.audit.entity.AuthGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-04-18
 */
public interface AuthGroupMapper extends BaseMapper<AuthGroup> {

    // 清除组关联权限
    void clearGroupPerms(@Param("groupId") Integer id);

    // 添加组关联权限
    void addGroupPerms(@Param("groupId") Integer id, @Param("permIdList") List<Integer> ids);
}
