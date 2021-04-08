package com.catas.glimmer.vo;

import com.catas.glimmer.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class JobVo extends Job {

    private Integer page = 1;
    private Integer limit = 10;

    private String taskTypeDesc;

    public String getTaskTypeDesc() {
        return super.getTaskType() == 0 ? "执行命令": "传输文件";
    }
}
