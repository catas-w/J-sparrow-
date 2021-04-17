package com.catas.glimmer.listener;

import com.catas.audit.common.Constant;
import com.catas.audit.service.IBindhostService;
import com.catas.glimmer.entity.TaskLog;
import com.catas.glimmer.service.ITaskLogDetailService;
import com.catas.glimmer.util.SSHUtil;
import com.catas.glimmer.vo.TaskLogDetailVo;
import com.catas.glimmer.vo.TaskLogVo;
import com.github.sonus21.rqueue.annotation.RqueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class MessageListener {

    @Autowired
    private SSHUtil sshUtil;

    @Autowired
    private IBindhostService bindhostService;

    @Autowired
    private ITaskLogDetailService logDetailService;


    /**
     * @Description: 执行异步任务
     */
    @RqueueListener(value = "${multiTask.queue.name}", numRetries = "2", concurrency = "10-15")
    public void multiTask(TaskLogDetailVo taskDetail) {
        log.info(">>>>>>>>>>>>> Running Async Task >>>>>>>>>>>>>");
        try {
            runTask(taskDetail);
        } catch (Exception e) {
            e.printStackTrace();
            taskDetail.setStatus(Constant.MULTI_TASK_FAILED);
            taskDetail.setEndTime(new Date());
            taskDetail.setResult("执行出现异常");
        }
        logDetailService.updateById(taskDetail);

        // log.info(taskDetail.getId().toString());
        log.info("当前任务执行完毕");
        log.info("<<<<<<<<<<<< End Task <<<<<<<<<<<<<<<<<");
    }

    public void runTask(TaskLogDetailVo taskDetail) {
        Integer bindHostId = taskDetail.getBindHostId();
        Map<String, String> loginInfo = bindhostService.getHostLoginInfo(bindHostId);
        String ipAddress = loginInfo.get("ip_address");
        int port = Integer.parseInt(loginInfo.get("port"));
        String name = loginInfo.get("username");
        String password = loginInfo.get("password");

        TaskLogVo parentTask = taskDetail.getParentTask();
        Integer taskType = parentTask.getTaskType();

        if (taskType.equals(Constant.MULTI_TASK_CMD)) {
            // 命令
            Map<String, String> res = sshUtil.execCommand(parentTask.getCmd(), ipAddress, port, name, password);
            if (res.get("status").equals("SUCCESS")) {
                taskDetail.setStatus(Constant.MULTI_TASK_SUCCESS);
            }else {
                taskDetail.setStatus(Constant.MULTI_TASK_FAILED);
            }
            taskDetail.setResult(res.get("msg"));

        } else if (taskType.equals(Constant.MULTI_TASK_SCP)) {
            // sftp
            Map<String, String> res = sshUtil.execSFTP(ipAddress, port, name, password, parentTask.getFiles(), parentTask.getRemotePath());
            if (res.get("status").equals("SUCCESS")) {
                taskDetail.setStatus(Constant.MULTI_TASK_SUCCESS);
            }else {
                taskDetail.setStatus(Constant.MULTI_TASK_FAILED);
            }
            taskDetail.setResult(res.get("msg"));
        }
    }
}
