package com.catas.glimmer.mapper;

import com.catas.glimmer.dto.TaskDetailDto;
import com.catas.glimmer.entity.TaskLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-04-14
 */
public interface TaskLogMapper extends BaseMapper<TaskLog> {

    // 获取关联任务详情
    List<TaskDetailDto> getRelatedTaskDetail(@Param("taskId") Integer id);

}
