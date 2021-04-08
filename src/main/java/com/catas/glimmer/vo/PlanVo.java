 package com.catas.glimmer.vo;

import com.catas.audit.common.Constant;
import com.catas.glimmer.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlanVo extends Plan {

    private Integer page = 1;
    private Integer limit = 10;

    private String userName;

    private String statusLevel;

    private Boolean enableLevel;

    private List<Integer> bHostIds;

    private List<Integer> orders;

    public String getName() {
        return super.getName();
    }

    public String getEnableLevel() {
        return super.getEnabled() == 1 ? "是": "否";
    }

    public String getCron() {
        return super.getCron();
    }

    public Integer getStatus() {
        return super.getStatus();
    }

    public Date getCreateDate() {
        return super.getCreateDate();
    }

    public Integer getUserId() {
        return super.getUserId();
    }

    public String getDescription() {
        return super.getDescription();
    }

    public String getStatusLevel() {
        Integer code = this.getStatus();
        String level = "";
        switch (code) {
            case 0: level = "未启动"; break;
            case 1: level = "运行中"; break;
            case 2: level = "已停止"; break;
        }
        return level;
    }
}
