package com.catas.glimmer.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleLogVo {

    private Integer page = 1;
    private Integer limit = 2;

    private Integer id;

    private Integer planId;

    private String planName;

    private String userName;

    private String description;

    private String statusLevel;

    private Integer status;

    public String getStatusLevel() {
        Integer code = getStatus();
        String level = "";
        switch (code) {
            case 0: level = "未启动"; break;
            case 1: level = "运行中"; break;
            case 2: level = "已停止"; break;
        }
        return level;
    }
}
