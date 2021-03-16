package com.catas.audit.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.vo.RelatedHostVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyPage<T> extends Page<T> {

    public RelatedHostVo relatedHostVo;

    public Integer test = 1;

    public MyPage(long current, long size) {
        super(current, size, 0);
    }
}
