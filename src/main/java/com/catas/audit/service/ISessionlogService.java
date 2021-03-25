package com.catas.audit.service;

import com.catas.audit.entity.Sessionlog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.audit.vo.SessionLogVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
public interface ISessionlogService extends IService<Sessionlog> {

    // 获取所有日志信息
    List<Map<String, Object>> getALlSessionLogInfo(SessionLogVo sessionlogVo);
}
