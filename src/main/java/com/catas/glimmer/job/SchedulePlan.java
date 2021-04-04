package com.catas.glimmer.job;


import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.service.IPlanService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时任务计划
 */
@DisallowConcurrentExecution
@Component
public class SchedulePlan implements Job {

    private final Logger logger = LoggerFactory.getLogger(SchedulePlan.class);

    @Autowired
    private IPlanService planService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        logger.info(">>>>>>>>> Running start >>>>>>>>>>>");
        printJobMsg(dataMap);
        logger.info("<<<<<<<<< task finished <<<<<<<<<<<");
    }

    // print message of jobs, for test purpose
    public void printJobMsg(JobDataMap dataMap) {
        logger.info(dataMap.toString());
        logger.info(dataMap.getString("name"));
        logger.info(dataMap.get("tasks").toString());
        logger.info(dataMap.get("bindHosts").toString());
    }

    // run ssh tasks
    public void runSSHJob(JobDataMap dataMap) {
        List<com.catas.glimmer.entity.Job> tasks = (List<com.catas.glimmer.entity.Job>) dataMap.get("tasks");

    }
}
