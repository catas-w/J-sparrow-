package com.catas.audit.service.impl;

import com.catas.audit.entity.Host;
import com.catas.audit.mapper.HostMapper;
import com.catas.audit.service.IHostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catas.audit.vo.HostVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
@Service
public class HostServiceImpl extends ServiceImpl<HostMapper, Host> implements IHostService {

    @Override
    public List<Map<String, Object>> getAllHostInfo(HostVo hostVo) {
        return this.getBaseMapper().getAllHostInfo(hostVo);
    }
}
