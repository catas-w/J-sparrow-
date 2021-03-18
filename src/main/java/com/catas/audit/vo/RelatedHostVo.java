package com.catas.audit.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedHostVo {

    private Integer page = 1;
    private Integer limit = 10;


    private String idc;

    private String hostName;

    private Integer userId;

    private Integer groupId;

}
