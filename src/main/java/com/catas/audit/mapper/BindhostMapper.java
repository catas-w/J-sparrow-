package com.catas.audit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.dto.RelatedHostDto;
import com.catas.audit.entity.Bindhost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.RelatedHostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author catas
 * @since 2021-03-15
 */
@Mapper
public interface BindhostMapper extends BaseMapper<Bindhost> {

    IPage<RelatedHostDto> queryBindHostsByUserId(
            Page<RelatedHostDto> page,
         @Param("userId") Integer userId,
         @Param("hostName") String hostName,
         @Param("idcId") Integer idcId
            );
}
