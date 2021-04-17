package com.catas.glimmer.util;


import com.catas.audit.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SSHTaskInfo {

    private String ipAddr;

    private String userName;

    private Integer port;

    private String cmd;

    private String password;

    private List<String> files;

    private String remotePath;

    private Integer taskType;

    public SSHTaskInfo(String ipAddr, Integer port, String userName, String password) {
        this.ipAddr = ipAddr;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public SSHTaskInfo(String ipAddr, Integer port, String userName, String password, String cmd) {
        this(ipAddr, port, userName, password);
        this.cmd = cmd;
        this.taskType = Constant.MULTI_TASK_CMD;
    }

    public SSHTaskInfo(String ipAddr, Integer port, String userName, String password, List<String> files, String remotePath) {
        this(ipAddr, port, userName, password);
        this.files = files;
        this.remotePath = remotePath;
        this.taskType = Constant.MULTI_TASK_SCP;
    }

}
