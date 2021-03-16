package com.catas.audit.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.catas.audit.mapper.BindhostMapper;
import com.catas.audit.service.IBindhostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catas.audit.vo.RelatedHostVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
@Service
public class BindhostServiceImpl extends ServiceImpl<BindhostMapper, Bindhost> implements IBindhostService {


    @Override
    public IPage<RelatedHostDto> queryRelatedHosts(Page<RelatedHostDto> page, RelatedHostVo relatedHostVo) {
        return this.getBaseMapper().queryBindHostsByUserId(page, relatedHostVo.getUserId(), relatedHostVo.getHostName(), relatedHostVo.getIdcId());
    }
}
