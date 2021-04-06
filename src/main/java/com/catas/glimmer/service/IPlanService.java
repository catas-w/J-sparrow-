package com.catas.glimmer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.glimmer.vo.PlanVo;
import org.apache.ibatis.annotations.Param;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
public interface IPlanService extends IService<Plan> {

    // 获取所有计划信息
    List<PlanVo> getAllPlanInfo(IPage<PlanVo> page, @Param("userId") Integer id);

    // 获取关联 job
    List<Map<String, Object>> getRelatedJobs(Plan plan);

    // 获取 dataMap
    JobDataMap getJobDataMap(Plan plan);

    /**
     * @param plan entity
     * @Description: 获取计划关联主机
     */
    List<Map<String, Object>> getRelatedBindHosts(Plan plan);

    // 获取jobDetail
    JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap dataMap);

    // 触发器
    Trigger getTrigger(Plan plan);

    // 获取 job key
    JobKey getJobKey(Plan plan);

    // 重启所有任务
    void restartAllPlan() throws SchedulerException;

    // 修改任务
    void modifyPlan() throws SchedulerException;

    // 启动 任务
    void startPlan(Plan plan) throws SchedulerException;

    // 重启任务
    void refreshPlan(Plan plan) throws SchedulerException;

    // 暂停任务
    void pausePlan(Plan plan) throws SchedulerException;

    // 继续任务
    void resumePlan(Plan plan) throws SchedulerException;

    // 终止任务
    void stopPlan(Plan plan) throws SchedulerException;
}
