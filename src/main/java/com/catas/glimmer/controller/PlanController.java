package com.catas.glimmer.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.entity.PlanBindHost;
import com.catas.glimmer.service.IJobService;
import com.catas.glimmer.service.IPlanBindHostService;
import com.catas.glimmer.service.IPlanService;
import com.catas.glimmer.vo.PlanVo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/glimmer")
public class PlanController {

    @Autowired
    private IPlanService planService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IPlanBindHostService planBindHostService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void initPlan() {
        synchronized (log) {
            try {
                planService.restartAllPlan();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 获取所有计划
     */
    @RequestMapping("/get-all-plan")
    public DataGridView getAllPlan(PlanVo planVo){

        IPage<PlanVo> page = new Page<>(planVo.getPage(), planVo.getLimit());
        List<PlanVo> allPlanInfo = planService.getAllPlanInfo(page, planVo.getUserId());

        return new DataGridView((long) allPlanInfo.size(), allPlanInfo);
    }

    /**
     * @Description: 获取计划信息
     */
    @RequestMapping("/get-plan-detail/{id}")
    public DataGridView getPlanInfo(@PathVariable("id") Integer planId) {
        Plan plan = planService.getById(planId);
        // 关联主机id
        QueryWrapper<PlanBindHost> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("bind_host_id").eq("plan_id", planId);
        List<PlanBindHost> list = planBindHostService.list(queryWrapper);
        List<Integer> bHostIds = new ArrayList<>();
        for (PlanBindHost planBindHost : list) {
            bHostIds.add(planBindHost.getBindHostId());
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("id", plan.getId());
        res.put("cron", plan.getCron());
        res.put("enabled", plan.getEnabled());
        res.put("description", plan.getDescription());
        res.put("name", plan.getName());
        res.put("bHostIds", bHostIds);
        return new DataGridView(res);
    }

    /**
     * @Description: 获取计划对应任务
     */
    @RequestMapping("/get-job-list")
    public DataGridView getJobList(Integer planId) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        // queryWrapper.orderByAsc("order");
        List<Job> jobs = jobService.list(queryWrapper);
        return new DataGridView((long) jobs.size(), jobs);
    }

    /**
     * @Description: start a schedule plan
     * @param planId plan id
     * @return result
     */
    @RequestMapping("/start/{id}")
    public ResultObj startPlan(@PathVariable("id") Integer planId) {
        Plan plan = planService.getById(planId);
        if (plan == null || plan.getEnabled() == 0) {
            return ResultObj.PLAN_UNABLE;
        }
        try {
            planService.startPlan(plan);
            return ResultObj.PLAN_START_SUCCESS;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResultObj.PLAN_START_FAILED;
        }
    }

    @RequestMapping("/refresh/{id}")
    public ResultObj refreshPlan(@PathVariable("id") Integer planId) {
        Plan plan = planService.getById(planId);
        if (plan == null || plan.getEnabled() == 0) {
            return ResultObj.PLAN_UNABLE;
        }
        try {
            planService.refreshPlan(plan);
            return ResultObj.PLAN_START_SUCCESS;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResultObj.PLAN_START_FAILED;
        }
    }

    @RequestMapping("/pause/{id}")
    public ResultObj pausePlan(@PathVariable("id") Integer planId ) {
        Plan plan = planService.getById(planId);
        if (plan == null || plan.getEnabled() == 0) {
            return ResultObj.PLAN_UNABLE;
        }
        try {
            planService.pausePlan(plan);
            return new ResultObj(Constant.OK, "任务已暂停");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "任务错误!");
        }
    }

    @RequestMapping("/resume/{id}")
    public ResultObj resumePlan(@PathVariable("id") Integer planId) {
        Plan plan = planService.getById(planId);
        if (plan == null || plan.getEnabled() == 0) {
            return ResultObj.PLAN_UNABLE;
        }
        try {
            planService.resumePlan(plan);
            return ResultObj.PLAN_START_SUCCESS;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResultObj.PLAN_START_FAILED;
        }
    }

    @RequestMapping("/stop/{id}")
    public ResultObj stopPlan(@PathVariable("id") Integer planId) {
        Plan plan = planService.getById(planId);
        if (plan == null || plan.getEnabled() == 0) {
            return ResultObj.PLAN_UNABLE;
        }
        try {
            planService.stopPlan(plan);
            return new ResultObj(Constant.OK, "任务已终止");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "Unknown Error!");
        }
    }
}
