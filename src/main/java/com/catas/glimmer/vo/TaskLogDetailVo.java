package com.catas.glimmer.vo;

import com.catas.glimmer.entity.TaskLog;
import com.catas.glimmer.entity.TaskLogDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskLogDetailVo extends TaskLogDetail implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer page = 1;

    private Integer limit = 10;

    private TaskLog parentTask;
}
