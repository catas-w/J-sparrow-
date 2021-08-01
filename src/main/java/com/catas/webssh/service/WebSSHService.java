package com.catas.webssh.service;

import com.catas.webssh.pojo.SSHConnectInfo;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @Description: WebSSH的业务逻辑
 */
public interface WebSSHService {
    /**
     * @Description: 初始化ssh连接
     */
    public void initConnection(WebSocketSession session);

    /**
     * @Description: 处理客户段发的数据
     */
    public void recvHandle(String buffer, WebSocketSession session);

    /**
     * @Description: 数据写回前端 for websocket
     */
    public void sendMessage(SSHConnectInfo sshConnectInfo, WebSocketSession session, byte[] buffer) throws IOException;

    /**
     * @Description: 关闭连接
     */
    public void close(WebSocketSession session);
}
