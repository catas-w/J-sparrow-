package com.catas.glimmer.dto;

import com.catas.audit.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailDto implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String bHostInfo;

    private String hostName;

    private String ipAddr;

    private String hostUser;

    private Date startTime;

    private String result;

    private Integer status;

    private String statusDesc;

    private String getStatusDesc() {
        if (this.status.equals(Constant.MULTI_TASK_RUNNING)) {
            return "运行中";
        }else if (this.status.equals(Constant.MULTI_TASK_SUCCESS)) {
            return "已完成";
        }else {
            return "出错";
        }
    }

}
