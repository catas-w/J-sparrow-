package com.catas.glimmer.mapper;

import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
public interface PlanMapper extends BaseMapper<Plan> {

    // 获取关联 job
    List<Job> getRelatedJobs(@Param("planId") Integer id);

    // 关联主机信息
    List<Map<String, Object>> getRelatedBindHosts(@Param("planId") Integer planId);

}
