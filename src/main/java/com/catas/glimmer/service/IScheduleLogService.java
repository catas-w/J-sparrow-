package com.catas.glimmer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.glimmer.entity.ScheduleLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.glimmer.vo.ScheduleLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
public interface IScheduleLogService extends IService<ScheduleLog> {

    // 获取所有日志信息
    List<ScheduleLogVo> getLogsList(Page<ScheduleLogVo> page, @Param("logVo") ScheduleLogVo logVo);
}
