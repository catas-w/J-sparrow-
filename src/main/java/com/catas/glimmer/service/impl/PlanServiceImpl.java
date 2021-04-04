package com.catas.glimmer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.job.SchedulePlan;
import com.catas.glimmer.mapper.PlanMapper;
import com.catas.glimmer.service.IJobBindHostService;
import com.catas.glimmer.service.IJobService;
import com.catas.glimmer.service.IPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements IPlanService {

    @Autowired
    private IJobBindHostService jobBindHostService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public List<Job> getRelatedJobs(Plan plan) {
        return this.getBaseMapper().getRelatedJobs(plan.getId());
    }

    /**
     *
     * @param plan entity
     * @Description: 获取计划关联主机
     */
    public List<Map<String, Object>> getRelatedBindHosts(Plan plan) {
        return this.getBaseMapper().getRelatedBindHosts(plan.getId());
    }

    @Override
    public JobDataMap getJobDataMap(Plan plan) {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("name", plan.getName());
        dataMap.put("enabled", plan.getEnabled());
        dataMap.put("plan", plan);
        dataMap.put("tasks", getRelatedJobs(plan));
        dataMap.put("bindHosts", getRelatedBindHosts(plan));
        return dataMap;
    }

    @Override
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap dataMap) {
        return JobBuilder.newJob(SchedulePlan.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(dataMap)
                .storeDurably()
                .build();
    }

    @Override
    public Trigger getTrigger(Plan plan) {
        return TriggerBuilder.newTrigger()
                .withIdentity(plan.getId() + plan.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(plan.getCron()))
                .build();
    }

    @Override
    public JobKey getJobKey(Plan plan) {
        return JobKey.jobKey(plan.getId() + plan.getName());
    }

    @Override
    public void restartAllPlan() throws SchedulerException {

    }

    @Override
    public void modifyPlan() throws SchedulerException {

    }

    @Override
    public void startPlan(Plan plan) throws SchedulerException {
        JobDataMap dataMap = this.getJobDataMap(plan);
        JobKey jobKey = this.getJobKey(plan);
        JobDetail jobDetail = this.getJobDetail(jobKey, "", dataMap);
        Trigger trigger = this.getTrigger(plan);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Date startDate = scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public void refreshPlan(Plan plan) throws SchedulerException {
        stopPlan(plan);
        startPlan(plan);
    }

    @Override
    public void pausePlan(Plan plan) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = this.getJobKey(plan);
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumePlan(Plan plan) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = this.getJobKey(plan);
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void stopPlan(Plan plan) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = this.getJobKey(plan);
        scheduler.pauseJob(jobKey);
        scheduler.unscheduleJob(TriggerKey.triggerKey(plan.getId()+plan.getName()));
        scheduler.deleteJob(jobKey);
    }


}
