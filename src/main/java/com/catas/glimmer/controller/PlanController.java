package com.catas.glimmer.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.entity.Sessionlog;
import com.catas.glimmer.entity.Job;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.entity.PlanBindHost;
import com.catas.glimmer.entity.ScheduleLog;
import com.catas.glimmer.job.SchedulePlan;
import com.catas.glimmer.service.IJobService;
import com.catas.glimmer.service.IPlanBindHostService;
import com.catas.glimmer.service.IPlanService;
import com.catas.glimmer.service.IScheduleLogService;
import com.catas.glimmer.vo.JobVo;
import com.catas.glimmer.vo.PlanVo;
import com.catas.glimmer.vo.ScheduleLogVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.util.bcel.Const;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/glimmer")
public class PlanController {

    private final Logger logger = LoggerFactory.getLogger(PlanController.class);

    @Autowired
    private IPlanService planService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IPlanBindHostService planBindHostService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private IScheduleLogService scheduleLogService;

    @Value("${ssh.schedule-log-path:#{null}}")
    private String scheduleLogPath;

    // @PostConstruct
    // public void initPlan() {
    //     synchronized (log) {
    //         try {
    //             planService.restartAllPlan();
    //         } catch (SchedulerException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    /**
     * @Description: 获取所有计划
     */
    @RequestMapping("/get-all-plan")
    public DataGridView getAllPlan(PlanVo planVo){
        logger.info("get all plan...");
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

    // 添加计划
    @RequestMapping("/add")
    public ResultObj addPlan(PlanVo planVo) {
        Plan plan = new Plan();
        plan.setName(planVo.getName());
        plan.setEnabled(0);
        plan.setStatus(Constant.PLAN_STATUS_CLOSED);
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        plan.setUserId(activeUser.getUserInfo().getId());
        try {
            planService.save(plan);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    // 修改计划
    @RequestMapping("/update")
    public ResultObj updatePlan(PlanVo planVo) {
        if (!CronExpression.isValidExpression(planVo.getCron())) {
            return new ResultObj(Constant.ERROR, "Cron表达式不合法");
        }

        try {
            if (planVo.getEnabled() == 0) {
                // 终止任务
                Plan plan = planService.getById(planVo.getId());
                plan.setStatus(Constant.PLAN_STATUS_CLOSED);
                planService.stopPlan(plan);
            }
            planService.updateBindHosts(planVo.getId(), planVo.getBHostIds());
            planService.updateById(planVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    // 删除计划
    @RequestMapping("/delete/{id}")
    public ResultObj deletePlan(@PathVariable("id") Integer planId) {
        Plan plan = planService.getById(planId);
        if (plan == null) {
            return new ResultObj(Constant.ERROR, "计划不存在");
        }
        try {
            planService.stopPlan(plan);
            planService.removeById(planId);
            return ResultObj.DELETE_SUCCESS;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }

    /**
     * @Description: 获取计划对应任务
     */
    @RequestMapping("/get-job-list")
    public DataGridView getJobList(Integer planId) {
        logger.info("get jobs");
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        queryWrapper.orderByAsc("`order`");
        List<Job> jobs = jobService.list(queryWrapper);
        ArrayList<JobVo> res = new ArrayList<>();
        for (Job job : jobs) {
            JobVo jobVo = new JobVo();
            jobVo.setId(job.getId());
            jobVo.setTaskType(job.getTaskType());
            jobVo.setCommand(job.getCommand());
            jobVo.setDescription(job.getDescription());
            jobVo.setName(job.getName());
            jobVo.setEnabled(job.getEnabled());
            res.add(jobVo);
        }
        return new DataGridView((long) res.size(), res);
    }

    // 添加子任务
    @RequestMapping("/detail/{pId}/job/add")
    public ResultObj addJob(@PathVariable("pId") Integer planId, JobVo jobVo) {
        try {
            jobVo.setPlanId(planId);
            jobService.save(jobVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    // 修改子任务
    @RequestMapping("/detail/{pId}/job/update")
    public ResultObj updateJob(@PathVariable("pId") Integer planId, JobVo jobVo) {
        try {
            jobVo.setPlanId(planId);
            jobService.updateById(jobVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    // 删除子任务
    @RequestMapping("/detail/{pId}/job/delete/{jId}")
    public ResultObj deleteJob(@PathVariable("pId") Integer planId, @PathVariable("jId") Integer jobId) {
        try {
            jobService.removeById(jobId);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }

    // 修改子任务顺序
    @RequestMapping("/detail/{pId}/job/reorder/")
    public ResultObj reOrder(@PathVariable("pId") Integer planId, PlanVo planVo) {
        List<Integer> orders = planVo.getOrders();
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        List<Job> jobs = jobService.list(queryWrapper);
        if (jobs.size() != orders.size()) {
            // 序号数量不对
            return ResultObj.UPDATE_FAILED;
        }
        for (Job job : jobs) {
            // 任务的新次序
            int index = orders.indexOf(job.getId());
            job.setOrder(index+1);
        }
        try {
            jobService.updateBatchById(jobs);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
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
            // 状态改为 running
            plan.setStatus(Constant.PLAN_STATUS_RUNNING);
            planService.saveOrUpdate(plan);
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
            // 状态改为 running
            plan.setStatus(Constant.PLAN_STATUS_RUNNING);
            planService.saveOrUpdate(plan);
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
            // 更改状态
            plan.setStatus(Constant.PLAN_STATUS_PAUSED);
            planService.saveOrUpdate(plan);
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
            // 状态改为 running
            plan.setStatus(Constant.PLAN_STATUS_RUNNING);
            planService.saveOrUpdate(plan);
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
            // 状态改为 running
            plan.setStatus(Constant.PLAN_STATUS_CLOSED);
            planService.saveOrUpdate(plan);
            return new ResultObj(Constant.OK, "任务已终止");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "Unknown Error!");
        }
    }

    // 获取日志列表
    @RequestMapping("/logs/get-all")
    public DataGridView getLogsList(ScheduleLogVo logVo) {
        Page<ScheduleLogVo> page = new Page<>(logVo.getPage(), logVo.getLimit());
        List<ScheduleLogVo> list = scheduleLogService.getLogsList(page, logVo);
        return new DataGridView(page.getTotal(), list);
    }

    // 下载日志文件
    @RequestMapping("/logs/download/{id}")
    public void downloadSSHLog(@PathVariable("id") Integer logId, HttpServletResponse response) {
        ScheduleLog scheduleLog = scheduleLogService.getById(logId);
        String tag = scheduleLog.getLogFilePath();
        File logFile = Path.of(this.scheduleLogPath, tag).toFile();
        if (!logFile.exists()) {
            return;
        }
        try{
            FileInputStream inputStream = new FileInputStream(logFile);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String diskfilename = "log_" + logId + ".txt";
            response.setContentType("txt/plain");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + (data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            OutputStream os = response.getOutputStream();

            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
