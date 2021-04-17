 package com.catas.glimmer.util;

import com.catas.audit.common.Constant;
import com.catas.webssh.utils.LogUtil;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SSHUtil {

    /**
     * @Description: 执行ssh command
     */
    public Map<String, String> execCommand(String cmd, String host, int port, String username, String password) {
        SSHTaskInfo cmdSSHInfo = new SSHTaskInfo(host, port, username, password, cmd);
        return this.execSSHTask(cmdSSHInfo, 10*1000);
    }

    /**
     * @Description: 执行sftp
     */
    public Map<String, String> execSFTP(String host, int port, String username, String password,List<String> files, String remotePath) {
        SSHTaskInfo taskInfo = new SSHTaskInfo(host, port, username, password, files, remotePath);
        return this.execSSHTask(taskInfo, 10*1000);
    }

    public Map<String, String> execSSHTask(SSHTaskInfo sshInfo, int timeout) {
        // 结果
        Map<String, String> res = new HashMap<>();
        res.put("status", "0");
        res.put("msg", "");
        // 连接信息
        String host = sshInfo.getIpAddr();
        String password = sshInfo.getPassword();
        Integer port = sshInfo.getPort();
        String username = sshInfo.getUserName();

        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        StringBuilder errLog = new StringBuilder("");
        runLog.append("Trying to connect to host: ").append(username).append("@").append(host).append("\n");
        try {
            // 1. 获取 ssh session
            session = jSch.getSession(username, host, port);
            session.setPassword(password);
            session.setTimeout(timeout);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();  // 获取到 ssh session

            runLog.append("Connect success 连接成功.\n");
            if (sshInfo.getTaskType().equals(Constant.MULTI_TASK_CMD)) {
                // 执行命令
                String cmd = sshInfo.getCmd();
                runLog.append("Executing command: ").append(cmd).append("\n");
                // 通过 exec 方式执行 shell 命令
                channelExec = (ChannelExec) session.openChannel("exec");
                channelExec.setCommand(cmd);
                channelExec.connect();  // 执行命令
                // 获取标准输入流
                inputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
                // 获取标准错误输入流
                errInputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));
                // 记录命令执行 log
                String line = null;
                while ((line = inputStreamReader.readLine()) != null) {
                    runLog.append(line).append("\n");
                }
                // 记录命令执行错误 log
                String errLine = null;
                while ((errLine = errInputStreamReader.readLine()) != null) {
                    runLog.append(errLine).append("\n");
                }

            } else if (sshInfo.getTaskType().equals(Constant.MULTI_TASK_SCP)){
                // SFTP
                List<String> files = sshInfo.getFiles();
                String remotePath = sshInfo.getRemotePath();
                runLog.append("开始发送文件...\n");

                ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect();
                for (String fName : files) {
                    File file = new File(fName);
                    boolean exists = file.exists();
                    sftpChannel.put(fName, remotePath);
                    runLog.append("文件[").append(file.getName()).append("]上传成功\n");
                }
            }

            res.put("status", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            runLog.append("Error occurred, 执行任务中出错!").append(e.getMessage()).append("\n");
            res.put("status", "FAILED");
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (errInputStreamReader != null) {
                    errInputStreamReader.close();
                }

                if (channelExec != null) {
                    channelExec.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        res.put("msg", runLog.toString());
        return res;
    }
}
