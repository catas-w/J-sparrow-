package com.catas.audit.mapper;

import com.catas.audit.entity.Idc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catas.audit.vo.IdcVo;
import org.apache.ibatis.annotations.Param;

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
public interface IdcMapper extends BaseMapper<Idc> {

    // 查询所有 IDC 信息
    List<Map<String, Object>> getAllIdcInfo(@Param("name") String name);
}
