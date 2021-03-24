package com.catas.audit.service;

import com.catas.audit.entity.Host;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.audit.vo.HostVo;

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
public interface IHostService extends IService<Host> {

    // 获取所有主机信息
    List<Map<String, Object>> getAllHostInfo(HostVo hostVo);

}
