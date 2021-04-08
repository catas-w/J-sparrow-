package com.catas.glimmer.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.glimmer.vo.PlanVo;
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
    List<Map<String, Object>> getRelatedJobs(@Param("planId") Integer id);

    // 关联主机信息
    List<Map<String, Object>> getRelatedBindHosts(@Param("planId") Integer planId);

    // 获取所有计划信息
    List<PlanVo> getAllPlanInfo(IPage<PlanVo> page, @Param("userId") Integer id);

    // 删除所有绑定主机
    void clearBindHosts();

    // 新建绑定主机
    void addBindHosts(@Param("bHostIds") List<Integer> bHostIds);
}
