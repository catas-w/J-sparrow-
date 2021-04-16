package com.catas.glimmer.util;

import com.catas.webssh.utils.LogUtil;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SSHUtil {

    public Map<String, String>  execCommand(String cmd, String host, int port, String username, String password) {
        return this.execCommand(cmd, host, port, username, password, 15 * 1000);
    }

    public Map<String, String>  execCommand(String cmd, String host, int port,String username, String password, int timeout) {
        Map<String, String> res = new HashMap<>();
        res.put("status", "0");
        res.put("msg", "");

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

            runLog.append("Connect success.\n");
            runLog.append("Executing command: ").append(cmd).append("\n");
            // 2. 通过 exec 方式执行 shell 命令
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.connect();  // 执行命令

            // 3. 获取标准输入流
            inputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            // 4. 获取标准错误输入流
            errInputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));

            // 5. 记录命令执行 log
            String line = null;
            while ((line = inputStreamReader.readLine()) != null) {
                runLog.append(line).append("\n");
            }

            // 6. 记录命令执行错误 log
            String errLine = null;
            while ((errLine = errInputStreamReader.readLine()) != null) {
                runLog.append(errLine).append("\n");
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
