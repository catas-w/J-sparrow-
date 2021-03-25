package com.catas.audit.service;

import com.catas.audit.entity.Idc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catas.audit.vo.IdcVo;
import org.apache.ibatis.annotations.Param;

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
public interface IIdcService extends IService<Idc> {

    // 查询所有 IDC 信息
    List<Map<String, Object>> getAllIdcInfo(IdcVo idcVo);
}
