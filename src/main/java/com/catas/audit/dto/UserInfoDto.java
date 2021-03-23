package com.catas.audit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Integer id;

    private String name;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;

    private Integer isActive;

    private Integer isAdmin;

    public Boolean getIsActive() {
        return isActive == 1;
    }

    public Boolean getIsAdmin() {
        return isAdmin == 1;
    }
}
