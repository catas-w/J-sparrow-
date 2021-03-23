package com.catas.webssh.utils;

import com.catas.audit.entity.Sessionlog;
import com.catas.audit.service.ISessionlogService;
import com.catas.webssh.constant.ConstantPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Component
public class LogUtil {

    @Autowired
    private ISessionlogService sessionlogService;

    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${ssh.log-path:#{null}}")
    private String logPath;

    public File init(String uuid, Integer bindHostId, Integer userId) throws IOException {
        // 日志所在目录
        Date nowDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(nowDate);
        Path logPath = Path.of(this.logPath, dateStr, uuid+".log");
        File logFile = logPath.toFile();
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        // 保存到数据库
        String tag = Path.of(dateStr, uuid).toString();
        Sessionlog sessionlog = new Sessionlog();
        sessionlog.setSessionTag(tag);
        sessionlog.setBindHostId(bindHostId);
        sessionlog.setUserId(userId);
        sessionlogService.save(sessionlog);

        return logFile;
    }

    public void log(String msg, File logFile) throws IOException {
        PrintStream printStream = new PrintStream(new FileOutputStream(logFile, true));
        System.setOut(printStream);
        Date now = new Date();
        String strNow = timeFormat.format(now);
        System.out.printf("[%s]: %s\n", strNow, msg);
        printStream.close();
    }

}
