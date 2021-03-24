package com.catas.audit.vo;

import com.catas.audit.entity.Bindhost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BindHostVo extends Bindhost {

    private Integer page = 1;

    private Integer limit = 10;

    private String hostName;

    private String userName;
}
