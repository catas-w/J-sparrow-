package com.catas.glimmer.dto;


import com.catas.audit.common.Constant;
import com.catas.glimmer.entity.TaskLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskLogDto implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    private String curTaskType;

    private String description;

    private Integer taskType;

    private Integer status;

    private Integer taskCount;

    public String getCurTaskType() {
        return this.taskType.equals(Constant.MULTI_TASK_CMD) ? "执行命令": "文件分发";
    }
}
