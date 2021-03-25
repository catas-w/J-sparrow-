package com.catas.audit.service.impl;

import com.catas.audit.entity.Idc;
import com.catas.audit.mapper.IdcMapper;
import com.catas.audit.service.IIdcService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catas.audit.vo.IdcVo;
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
public class IdcServiceImpl extends ServiceImpl<IdcMapper, Idc> implements IIdcService {

    @Override
    public List<Map<String, Object>> getAllIdcInfo(IdcVo idcVo) {
        return this.getBaseMapper().getAllIdcInfo(idcVo.getName());
    }
}
