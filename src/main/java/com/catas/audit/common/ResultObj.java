package com.catas.audit.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {

    private Integer code;
    private String msg;

    public static final ResultObj LOGIN_SUCCESS = new ResultObj(Constant.OK,"登陆成功");
    public static final ResultObj LOGIN_ERROR_PASS = new ResultObj(Constant.ERROR,"用户名或密码错误");
    public static final ResultObj LOGIN_ERROR_CODE = new ResultObj(Constant.ERROR,"验证码错误");
    public static final ResultObj LOGIN_UNKNOW_USER = new ResultObj(Constant.ERROR,"用户不存在");


}
