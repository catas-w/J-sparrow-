package com.catas.glimmer.service;

import com.catas.glimmer.dto.TaskDetailDto;
import com.catas.glimmer.entity.TaskLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-04-14
 */
public interface ITaskLogService extends IService<TaskLog> {

    // 获取关联任务详情
    List<TaskDetailDto> getRelatedTaskDetail(Integer id);
}
