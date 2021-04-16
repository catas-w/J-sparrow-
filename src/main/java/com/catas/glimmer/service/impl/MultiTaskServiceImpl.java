package com.catas.glimmer.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.catas.glimmer.service.IMultiTaskService;
import com.catas.glimmer.service.ITaskLogDetailService;
import com.catas.glimmer.service.ITaskLogService;
import com.catas.glimmer.vo.TaskLogDetailVo;
import com.catas.glimmer.vo.TaskLogVo;
import com.github.sonus21.rqueue.core.RqueueMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MultiTaskServiceImpl implements IMultiTaskService {

    @Autowired
    private RqueueMessageSender rqueueMessageSender;

    @Value("${multiTask.queue.name}")
    private String multiTaskQueue;

    @Autowired
    private ITaskLogService taskLogService;

    @Autowired
    private ITaskLogDetailService taskDetailService;

    @Override
    public int createTask(TaskLogVo taskLogVo) {
        String uuid = IdUtil.randomUUID();
        taskLogVo.setTaskId(uuid);
        taskLogVo.setId(null);
        taskLogService.save(taskLogVo);
        List<Integer> bindHostIds = taskLogVo.getBindHostIds();
        for (Integer bindHostId : bindHostIds) {
            // 新建子任务并导入队列
            TaskLogDetailVo detailVo = new TaskLogDetailVo();
            detailVo.setParentTask(taskLogVo);
            detailVo.setBindHostId(bindHostId);
            detailVo.setParentTaskId(taskLogVo.getId());
            taskDetailService.save(detailVo);
            rqueueMessageSender.enqueueIn(multiTaskQueue, detailVo, taskLogVo.getDelay()*1000);
        }

        return taskLogVo.getId();
    }
}
