package com.catas.audit.vo;

import com.catas.audit.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends UserInfo {

    private Integer page = 1;
    private Integer limit = 10;

    private String code;

    private Boolean rememberMe = true;
}

