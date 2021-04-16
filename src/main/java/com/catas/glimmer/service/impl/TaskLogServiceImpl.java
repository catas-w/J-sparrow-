package com.catas.glimmer.service.impl;

import com.catas.glimmer.dto.TaskDetailDto;
import com.catas.glimmer.entity.TaskLog;
import com.catas.glimmer.mapper.TaskLogMapper;
import com.catas.glimmer.service.ITaskLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-04-14
 */
@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements ITaskLogService {

    @Override
    public List<TaskDetailDto> getRelatedTaskDetail(Integer id) {
        return this.getBaseMapper().getRelatedTaskDetail(id);
    }

}
