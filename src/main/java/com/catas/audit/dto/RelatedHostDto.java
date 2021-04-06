package com.catas.audit.dto;


import com.catas.audit.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedHostDto {

    // bindHost id
    private Integer id;

    private String hostName;

    private String ipAddress;

    private String idc;

    private Integer port;

    private Integer enabled;

    private String userName;

    private Integer authType;

    private String description;

    public String getDescription() {
        return String.format("%s@%s", hostName, ipAddress);
    }

    public String getEnabled() {
        return enabled == 0 ? "否" : "是";
    }

    public String getAuthType() {
        return authType.equals(Constant.AUTH_PASSWORD) ? "密码" : "SSL";
    }
}
