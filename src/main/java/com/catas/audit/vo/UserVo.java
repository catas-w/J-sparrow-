package com.catas.audit.vo;

import com.catas.audit.entity.UserInfo;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class UserVo extends UserInfo {

    private Integer page = 1;
    private Integer limit = 10;

    private String code;

    private Boolean rememberMe = true;

    private List<Integer> bindHostIds;

    private List<Integer> bindGroupIds;

    private List<Integer> relatedGroupList;

}

