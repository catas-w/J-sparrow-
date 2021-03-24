package com.catas.audit.vo;

import com.catas.audit.entity.Hostgroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class HostGroupVo extends Hostgroup {

    private Integer page = 1;
    private Integer limit = 10;

    private String groupName;

    private ArrayList<Integer> bindHostIds;

    @Override
    public String getName() {
        return groupName;
    }
}
