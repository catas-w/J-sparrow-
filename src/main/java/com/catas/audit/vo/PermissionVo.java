package com.catas.audit.vo;

import com.catas.audit.entity.AuthPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVo extends AuthPermission {

    private Integer page = 1;

    private Integer limit = 10;

    private Integer groupId;
}
