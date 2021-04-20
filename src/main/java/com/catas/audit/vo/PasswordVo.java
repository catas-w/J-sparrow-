package com.catas.audit.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVo {

    @NotNull(message = "密码不能为空")
    private String oldPassword;

    @Length(min = 6, message = "密码不能少于6位")
    @NotNull(message = "密码不能为空")
    private String newPassword;

    @NotNull(message = "密码不能为空")
    private String againPassword;
}
