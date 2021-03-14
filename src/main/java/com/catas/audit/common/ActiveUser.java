package com.catas.audit.common;

import com.catas.audit.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUser implements Serializable {

    private final static long serialVersionUID = 1L;

    private UserInfo userInfo;

    private List<String> permissions;

}
