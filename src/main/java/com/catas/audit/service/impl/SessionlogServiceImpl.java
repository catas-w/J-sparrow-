package com.catas.audit.service.impl;

import com.catas.audit.entity.Sessionlog;
import com.catas.audit.mapper.SessionlogMapper;
import com.catas.audit.service.ISessionlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
@Service
public class SessionlogServiceImpl extends ServiceImpl<SessionlogMapper, Sessionlog> implements ISessionlogService {

}
