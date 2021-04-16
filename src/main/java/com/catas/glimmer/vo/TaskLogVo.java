package com.catas.glimmer.vo;


import com.catas.audit.common.Constant;
import com.catas.glimmer.entity.TaskLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskLogVo extends TaskLog implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer page = 1;

    private Integer limit = 10;

    private String curTaskType = "";

    private List<Integer> bindHostIds;

    private Integer delay = 0;

}
