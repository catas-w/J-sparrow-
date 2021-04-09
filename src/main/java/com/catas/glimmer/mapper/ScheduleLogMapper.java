package com.catas.glimmer.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.glimmer.entity.ScheduleLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.glimmer.vo.ScheduleLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
public interface ScheduleLogMapper extends BaseMapper<ScheduleLog> {

    // 获取所有日志信息
    List<ScheduleLogVo> getLogsList(Page<ScheduleLogVo> page, @Param("logVo") ScheduleLogVo logVo);

}
