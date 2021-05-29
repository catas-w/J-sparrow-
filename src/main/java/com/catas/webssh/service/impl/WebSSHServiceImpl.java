package com.catas.webssh.service.impl;

import com.catas.audit.service.IBindhostService;
import com.catas.webssh.constant.ConstantPool;
import com.catas.webssh.pojo.SSHConnectInfo;
import com.catas.webssh.pojo.WebSSHData;
import com.catas.webssh.service.WebSSHService;
import com.catas.webssh.utils.LogUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @Description: WebSSH业务逻辑实现
*/
@Service
public class WebSSHServiceImpl implements WebSSHService {

    @Autowired
    private IBindhostService bindhostService;

    @Autowired
    private LogUtil logUtil;

    //存放ssh连接信息的map
    private static final Map<String, Object> sshMap = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);

    //线程池
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * @Description: 初始化连接
     */
    @Override
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setJSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
        String uuid = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        //将这个ssh连接信息放入map中
        sshMap.put(uuid, sshConnectInfo);
    }

    /**
     * @Description: 处理客户端发送的数据
     */
    @Override
    public void recvHandle(String buffer, WebSocketSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        WebSSHData webSSHData = null;
        try {
            webSSHData = objectMapper.readValue(buffer, WebSSHData.class);
        } catch (IOException e) {
            logger.error("Json转换异常");
            logger.error("异常信息:{}", e.getMessage());
            return;
        }
        String uuId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));


        if (ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(uuId);
            //启动线程异步处理
            WebSSHData finalWebSSHData = webSSHData;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectToSSH(sshConnectInfo, finalWebSSHData, session, uuId);
                        if (sshConnectInfo.getChannel().isClosed()) {
                            close(session);
                        }
                    } catch (JSchException | IOException e) {
                        logger.error("webssh连接异常");
                        logger.error("异常信息:{}", e.getMessage());
                        try {
                            //发送错误信息
                            sendMessage(sshConnectInfo, session, ("ERROR : "+e.getMessage()).getBytes());
                        } catch (IOException ex) {
                            logger.error("消息发送失败");
                            logger.error("异常信息:{}", ex.getMessage());
                        }
                        close(session);
                    }
                }
            });
        } else if (ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
            String command = webSSHData.getCommand();
            if (command.length() == 0) {
                return;
            }
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(uuId);
            if (sshConnectInfo != null) {
                try {
                    ChannelShell channel = (ChannelShell) sshConnectInfo.getChannel();
                    if (channel != null) {
                        channel.setPtySize(webSSHData.getCols(),webSSHData.getRows(),webSSHData.getWidth(),webSSHData.getHeight());
                        transToSSH(sshConnectInfo, command);
                        if (channel.isClosed()) {
                            close(session);
                        }
                    }
                } catch (IOException e) {
                    logger.error("webssh连接异常");
                    logger.error("异常信息:{}", e.getMessage());
                    try {
                        //发送错误信息
                        sendMessage(sshConnectInfo, session, ("ERROR : "+e.getMessage()).getBytes());
                    } catch (IOException ex) {
                        logger.error("消息发送失败");
                        logger.error("异常信息:{}", ex.getMessage());
                    }
                    close(session);
                }
            }
        } else if (ConstantPool.WEBSSH_OPERATE_HEARTBEAT.equals(webSSHData.getOperate())) {
            //检查心跳
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(uuId);
            if (sshConnectInfo != null) {
                try {
                    //处于连接状态则发送健康数据，不能为空，空则断开连接。
                    if (sshConnectInfo.getChannel().isConnected())
                        sendMessage(sshConnectInfo, session, "Heartbeat healthy".getBytes());
                } catch (IOException e) {
                    logger.error("消息发送失败");
                    logger.error("异常信息:{}", e.getMessage());
                }
            }
        } else {
            logger.error("不支持的操作");
            close(session);
        }
    }

    @Override
    public void sendMessage(SSHConnectInfo sshConnectInfo, WebSocketSession session, byte[] buffer) throws IOException {
        if (sshConnectInfo.getTabFlag()) {
            // 上一次按了tab
            sshConnectInfo.getTemp().add(new String(buffer, StandardCharsets.UTF_8));
            sshConnectInfo.setTabFlag(false);
        }
        session.sendMessage(new TextMessage(buffer));
    }

    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) sshConnectInfo.getChannel().disconnect();
            //map中移除
            sshMap.remove(userId);
        }
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 使用jsch连接终端
     */
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession, String uuId) throws JSchException, IOException {

        // 验证bindHost
        String[] splits = uuId.split("-");
        Integer userId = Integer.valueOf(splits[splits.length - 1]);

        Set<Integer> relatedHostIds = bindhostService.queryAllRelatedHostIds(userId);
        Integer bindHostId = webSSHData.getBindHostId();
        if (!relatedHostIds.contains(bindHostId)) {
            String msg = "用户没有访问权限!";
            throw new IOException(msg);
        }
        // 获取当前主机登录信息
        Map<String, String> loginInfo = bindhostService.getHostLoginInfo(bindHostId);
        webSSHData.setPassword(loginInfo.get("password"));
        webSSHData.setIpAddress(loginInfo.get("ip_address"));
        webSSHData.setPort(Integer.valueOf(loginInfo.get("port")));
        webSSHData.setUsername(loginInfo.get("username"));


        Session session = null;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        session = sshConnectInfo.getJSch().getSession(webSSHData.getUsername(), webSSHData.getIpAddress(), webSSHData.getPort());
        session.setConfig(config);
        //设置密码
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(30000);

        // 验证成功后开启日志
        sshConnectInfo.setUserId(userId);
        sshConnectInfo.setBindHostId(webSSHData.getBindHostId());
        sshConnectInfo.setLogUtil(logUtil);
        sshConnectInfo.initLog(uuId);

        //开启shell通道
        Channel channels = session.openChannel("shell");
        ChannelShell channel = (ChannelShell) channels;
        channel.setPtySize(webSSHData.getCols(),webSSHData.getRows(),webSSHData.getWidth(),webSSHData.getHeight());

        //通道连接 超时时间3s
        channel.connect(3000);

        //设置channel
        sshConnectInfo.setChannel(channel);

        // 转发消息
        transToSSH(sshConnectInfo, "\r");

        //读取终端返回的信息流
        InputStream inputStream = channel.getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(sshConnectInfo, webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            //断开连接后关闭会话
            session.disconnect();
            channel.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    /**
     * @Description: 将消息转发到终端
     */
    private void transToSSH(SSHConnectInfo sshConnectInfo, String command) throws IOException {
        // 记录命令
        if (command.equals("\t")) {
            sshConnectInfo.setTabFlag(true);
        }else {
            sshConnectInfo.getTemp().add(command);
        }
        if (command.equals("\r") || command.equals("\n")) {
            String cmd = StringUtils.join(sshConnectInfo.getTemp(), "");
            // sshConnectInfo.getLogUtil().log(cmd);
            sshConnectInfo.logMsg(cmd);
            sshConnectInfo.getTemp().clear();
        }

        Channel channel = sshConnectInfo.getChannel();
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
