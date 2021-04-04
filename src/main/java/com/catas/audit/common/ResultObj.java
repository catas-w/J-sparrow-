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
    public static final ResultObj LOGIN_UNKNOWN_USER = new ResultObj(Constant.ERROR,"用户不存在");

    public static final ResultObj EMAIL_EXISTS = new ResultObj(Constant.ERROR, "邮箱已存在");
    public static final ResultObj NAME_EXISTS = new ResultObj(Constant.ERROR, "用户名已存在");

    public static final ResultObj UPDATE_SUCCESS = new ResultObj(Constant.OK, "更新成功");
    public static final ResultObj UPDATE_FAILED = new ResultObj(Constant.ERROR, "更新失败");;

    public static final ResultObj ADD_SUCCESS = new ResultObj(Constant.OK, "添加成功");
    public static final ResultObj ADD_FAILED = new ResultObj(Constant.ERROR, "添加失败");

    public static final ResultObj DELETE_SUCCESS = new ResultObj(Constant.OK, "删除成功");
    public static final ResultObj DELETE_FAILED = new ResultObj(Constant.ERROR, "删除失败");

    //  ------------- glimmer -----------
    public static final ResultObj PLAN_UNABLE = new ResultObj(Constant.ERROR, "任务未激活");
    public static final ResultObj PLAN_START_SUCCESS = new ResultObj(Constant.OK, "任务已启动");
    public static final ResultObj PLAN_START_FAILED = new ResultObj(Constant.ERROR, "任务启动失败");


}
