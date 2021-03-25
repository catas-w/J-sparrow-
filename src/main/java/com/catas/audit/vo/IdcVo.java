package com.catas.audit.vo;


import com.catas.audit.entity.Idc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IdcVo extends Idc {

    private Integer page = 1;
    private Integer limit = 10;

}
