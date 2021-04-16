package com.catas.glimmer.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.ActiveUser;
import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.glimmer.dto.TaskDetailDto;
import com.catas.glimmer.dto.TaskLogDto;
import com.catas.glimmer.entity.TaskLog;
import com.catas.glimmer.service.IMultiTaskService;
import com.catas.glimmer.service.ITaskLogService;
import com.catas.glimmer.vo.TaskLogVo;
import com.github.sonus21.rqueue.core.RqueueMessageSender;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/glimmer/multi")
public class MultiTaskController {

    @Autowired
    private RqueueMessageSender rqueueMessageSender;

    @Value("${email.queue.name}")
    private String emailQueueName;

    @Autowired
    private IMultiTaskService multiTaskService;

    @Autowired
    private ITaskLogService taskLogService;

    @RequestMapping("/email")
    public String sendEmail(@RequestParam String email) {
        log.info("Sending email");
        rqueueMessageSender.enqueue(emailQueueName, email);
        rqueueMessageSender.enqueue(emailQueueName, "MY emal2...");
        rqueueMessageSender.enqueue(emailQueueName, "MY emal3...");
        rqueueMessageSender.enqueue(emailQueueName, "MY emal4...");
        rqueueMessageSender.enqueue(emailQueueName, "MY emal5...");
        rqueueMessageSender.enqueue(emailQueueName, "MY emal6...");
        // rqueueMessageSender.enqueueIn()
        return "Please check your inbox!";
    }

    /**
     *
     * @Description: 新建批量任务
     * @return: 任务id
     */
    @RequestMapping("/cmd/create")
    public DataGridView createTask(TaskLogVo taskLogVo) {
        List<Integer> bindHostIds = taskLogVo.getBindHostIds();
        if (bindHostIds == null || bindHostIds.size() == 0) {
            return new DataGridView(Constant.ERROR, "参数不正确");
        }

        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();
        taskLogVo.setUserId(userId);
        int taskId = multiTaskService.createTask(taskLogVo);
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("id", taskId);
        return new DataGridView(hashMap);
    }

    /**
     * @return 当前用户任务日志
     */
    @RequestMapping("/logs/all")
    public DataGridView getLogs(TaskLogVo taskLogVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Integer userId = activeUser.getUserInfo().getId();

        Page<TaskLog> logPage = new Page<>(taskLogVo.getPage(), taskLogVo.getLimit());
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        taskLogService.page(logPage, queryWrapper);

        logPage.convert(taskLog -> {
            TaskLogDto logDto = new TaskLogDto();
            BeanUtils.copyProperties(taskLog, logDto);
            return logDto;
        });
        return new DataGridView(logPage.getTotal(), logPage.getRecords());
    }

    /**
     * @return 多任务日志详情
     */
    @RequestMapping("/logs/detail/{id}")
    public DataGridView getLogDetail(@PathVariable("id") Integer taskLogId) {
        List<TaskDetailDto> taskDetails = taskLogService.getRelatedTaskDetail(taskLogId);
        return new DataGridView(taskDetails);
    }
}
