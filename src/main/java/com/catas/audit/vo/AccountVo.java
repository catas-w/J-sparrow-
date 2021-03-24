package com.catas.audit.vo;


import com.catas.audit.entity.Hostuser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 主机登录账户
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountVo extends Hostuser{

    private Integer page = 1;

    private Integer limit = 10;

    private String authDescription;

    public String getAuthDescription() {
        return getAuthType() == 0 ? "密码": "证书";
    }
}
