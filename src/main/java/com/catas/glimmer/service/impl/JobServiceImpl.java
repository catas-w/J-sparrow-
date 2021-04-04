package com.catas.glimmer.service.impl;

import com.catas.glimmer.entity.Job;
import com.catas.glimmer.mapper.JobMapper;
import com.catas.glimmer.service.IJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

}
