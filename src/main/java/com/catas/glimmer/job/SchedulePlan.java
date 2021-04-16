package com.catas.glimmer.job;


import com.catas.audit.common.Constant;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.service.IPlanService;
import com.catas.glimmer.util.SSHUtil;
import com.catas.webssh.utils.LogUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时任务计划
 */
@DisallowConcurrentExecution
@Component
public class SchedulePlan implements Job {

    private final Logger logger = LoggerFactory.getLogger(SchedulePlan.class);

    public final Object lock = new Object();

    @Autowired
    private IPlanService planService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private SSHUtil sshUtil;

    @Autowired
    private LogUtil logUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        File logFile = (File) dataMap.get("logFile");
        String planName = dataMap.getString("name");
        logUtil.log(">>>>>>>>> Start executing Plan: " + planName, logFile);
        logger.info(">>>>>>>>> Running start >>>>>>>>>>>");
        try {
            runSSHJob(dataMap);
            // printJobMsg(dataMap);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logUtil.log("Error occurred when executing current plan!", logFile);
            logUtil.log(e.getMessage(), logFile);
        }
        finally {
            logger.info("<<<<<<<<< Plan finished <<<<<<<<<<<");
            logUtil.log("<<<<<<<<< Plan "+planName +" finished", logFile);
        }
    }

    // print message of jobs, for test purpose
    public void printJobMsg(JobDataMap dataMap) throws InterruptedException {
        logger.info(dataMap.toString());
        logger.info(dataMap.getString("name"));
        logger.info(dataMap.get("tasks").toString());
        logger.info(dataMap.get("bindHosts").toString());
    }

    /**
     * @Description: 串行执行每一个task
     * @param dataMap data map
     */
    public void runSSHJob(JobDataMap dataMap) throws InterruptedException {
        File logFile = (File) dataMap.get("logFile");
        List<Map<String, Object>> tasks = (List<Map<String, Object>>) dataMap.get("tasks");
        List<Map<String, Object>> bindHosts = (List<Map<String, Object>>) dataMap.get("bindHosts");
        for (Map<String, Object> taskInfo : tasks) {
            // 串行执行每个Task
            Integer task_type = (Integer) taskInfo.get("task_type");
            logUtil.log("Running schedule task: " + taskInfo.get("name"), logFile);
            for (Map<String, Object> bindHost : bindHosts) {
                if (Constant.EXEC_COMMAND_TASK.equals(task_type)) {
                    // 执行命令任务
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            execSSHCommand(taskInfo, bindHost, logFile);
                        }
                    });
                    thread.start();
                    thread.join();
                } else {
                    // TODO: 执行文件SCP
                }
            }
        }
    }

    /**
     * @Description: 执行命令
     */
    public void execSSHCommand(Map<String, Object> taskInfo, Map<String, Object> bindHost, File logFile) {

        String command = (String) taskInfo.get("command");
        String userName = (String) bindHost.get("userName");
        String password = (String) bindHost.get("password");
        String ipAddress = (String) bindHost.get("ipAddress");
        Long port = (Long) bindHost.get("port");
        Map<String, String>  result = sshUtil.execCommand(command, ipAddress, Math.toIntExact(port), userName, password);
        synchronized (lock) {
            // 结果写入日志
            logUtil.log(result.get("msg"), logFile);
        }
    }

    /**
     * @Description: execute file transport
     */
    public void execSCP(Map<String, Object> taskInfo, Map<String, Object> bindHost) {

    }
}
