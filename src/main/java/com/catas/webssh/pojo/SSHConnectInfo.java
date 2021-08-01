package com.catas.webssh.pojo;

import com.catas.webssh.utils.LogUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: ssh连接信息
*/
@Data
public class SSHConnectInfo {

    private WebSocketSession webSocketSession;

    private JSch jSch;

    private Channel channel;

    private List<String> temp;

    private Boolean tabFlag;

    private LogUtil logUtil;

    private File logFile;

    private Integer bindHostId;

    private Integer userId;

    public SSHConnectInfo() {
        this.tabFlag = false;
        this.temp = new ArrayList<>();
    }

    public void initLog(String uuid) throws IOException {
        // this.logUtil = new LogUtil();
        logFile = this.logUtil.init(uuid, bindHostId, userId);
    }

    public void logMsg(String msg) {
        this.logUtil.log(msg, this.logFile);
    }
}
