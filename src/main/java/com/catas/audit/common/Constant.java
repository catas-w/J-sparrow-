package com.catas.audit.common;

public class Constant {

    public static final Integer OK = 200;
    public static final Integer ERROR = -1;

    public static final Integer USER_ACTIVE = 1;
    public static final Integer USER_LOCKED = 0;

    public static final Integer AUTH_PASSWORD = 0;
    public static final Integer AUTH_SSL = 1;

    public static final String DEFAULT_PWS = "123456";

    // ---------- glimmer ------------
    public static final Integer EXEC_COMMAND_TASK = 0;
    public static final Integer EXEC_SCP_TASK = 1;

    public static final Integer TASK_STATUS_CLOSED = 0;
    public static final Integer TASK_STATUS_RUNNING = 1;
    public static final Integer TASK_STATUS_PAUSED = 2;
    public static final Integer TASK_STATUS_FAILED = 3;

    public static final Integer PLAN_STATUS_CLOSED = 0;
    public static final Integer PLAN_STATUS_RUNNING = 1;
    public static final Integer PLAN_STATUS_PAUSED = 2;
    public static final Integer PLAN_STATUS_FAILED = 3;
}
