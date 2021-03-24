package com.catas.audit.vo;


import com.catas.audit.entity.Host;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostVo extends Host {

    private Integer page = 1;

    private Integer limit = 10;

    private String idcName;

}
