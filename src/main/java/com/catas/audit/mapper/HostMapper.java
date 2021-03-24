package com.catas.audit.mapper;

import com.catas.audit.entity.Host;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.HostVo;

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
public interface HostMapper extends BaseMapper<Host> {

    // 获取所有主机信息
    List<Map<String, Object>> getAllHostInfo(HostVo hostVo);

}
