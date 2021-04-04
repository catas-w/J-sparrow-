package com.catas.glimmer.controller;


import com.catas.audit.common.Constant;
import com.catas.audit.common.ResultObj;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.service.IPlanService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;


@Slf4j
@RestController
@RequestMapping("/")
public class PlanController {

    @Autowired
    private IPlanService planService;

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
