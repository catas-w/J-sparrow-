package com.catas.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.audit.vo.RelatedHostVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
public interface IBindhostService extends IService<Bindhost> {

    IPage<RelatedHostDto> queryRelatedHosts(Page<RelatedHostDto> page, RelatedHostVo relatedHostVo);
}
