package com.catas.glimmer.service;

import com.catas.audit.common.DataGridView;
import com.catas.glimmer.vo.TaskLogVo;

public interface IMultiTaskService {

    /**
     * @Description: 新建多任务
     */
    int createTask(TaskLogVo taskLogVo);

}
