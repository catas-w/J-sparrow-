package com.catas.glimmer.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.glimmer.entity.ScheduleLog;
import com.catas.glimmer.mapper.ScheduleLogMapper;
import com.catas.glimmer.service.IScheduleLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catas.glimmer.vo.ScheduleLogVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
@Service
public class ScheduleLogServiceImpl extends ServiceImpl<ScheduleLogMapper, ScheduleLog> implements IScheduleLogService {

    @Override
    public List<ScheduleLogVo> getLogsList(Page<ScheduleLogVo> page, ScheduleLogVo logVo) {
        return this.getBaseMapper().getLogsList(page, logVo);
    }
}
