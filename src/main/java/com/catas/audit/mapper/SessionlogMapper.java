package com.catas.audit.mapper;

import com.catas.audit.entity.Sessionlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.SessionLogVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
public interface SessionlogMapper extends BaseMapper<Sessionlog> {

    // 获取所有日志信息
    List<Map<String, Object>> getALlSessionLogInfo(SessionLogVo sessionlogVo);

}
