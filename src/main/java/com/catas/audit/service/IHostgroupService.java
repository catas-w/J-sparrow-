package com.catas.audit.service;

import com.catas.audit.entity.Hostgroup;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

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
public interface IHostgroupService extends IService<Hostgroup> {

    // 获取所有绑定主机组信息
    List<Map<String, Object>> getAllGroupInfo();

    // 获取所有绑定组的 id
    Set<Integer> queryRelatedGroupIds(Integer id);

    // 更新绑定主机组
    void updateRelatedGroups(Integer id, List<Integer> gIds);
}
