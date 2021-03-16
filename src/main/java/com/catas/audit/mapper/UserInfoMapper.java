package com.catas.audit.mapper;

import com.catas.audit.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-03-14
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    // 根据用户 id 获取其主机组
    List<Integer> queryGroupIdsByUserId(@Param("id") Integer id);
}
