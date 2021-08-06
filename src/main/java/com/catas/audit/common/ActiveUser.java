package com.catas.audit.common;

import com.catas.audit.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ActiveUser implements Serializable {

    private final static long serialVersionUID = 1L;

    private Integer id;

    private UserInfo userInfo;

    private List<String> permissions;

    public Integer getId() {
        return userInfo.getId();
    }

    public void setId() {
        this.id = userInfo.getId();
    }
}
