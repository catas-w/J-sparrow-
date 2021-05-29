package com.catas.webssh.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catas.audit.common.Constant;
import com.catas.audit.entity.Sessionlog;
import com.catas.audit.service.ISessionlogService;
import com.catas.glimmer.entity.Plan;
import com.catas.glimmer.entity.ScheduleLog;
import com.catas.glimmer.service.IScheduleLogService;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@Component
@ConfigurationProperties(prefix = "ssh")
public class LogUtil {

    @Autowired
    private ISessionlogService sessionlogService;

    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String logPath;

    @Autowired
    private IScheduleLogService scheduleLogService;

    private String scheduleLogPath;

    private Integer maxRows;

    private final BlockingQueue<String[]> messageQueue;



    /**
     * @Description: SSH 日志记录
     * @return log file
     */
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

    /**
     * @Description: init 定时任务日志
     * @return 日志文件
     */
    public File initScheduleLog(Plan plan)  {
        System.out.println(this.scheduleLogPath);
        QueryWrapper<ScheduleLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", plan.getId());
        queryWrapper.orderByDesc("start_date");
        List<ScheduleLog> scheduleLogs = scheduleLogService.list(queryWrapper);

        File logFile = null;
        if (!scheduleLogs.isEmpty()) {
            // 数据库有日志
            Path logPath = Path.of(this.scheduleLogPath, scheduleLogs.get(0).getLogFilePath());
            logFile = logPath.toFile();
        }else {
            // 日志不存在. 则新建
            String fileName = "Plan-" + plan.getId() + ".log";
            Path logPath = Path.of(this.scheduleLogPath, fileName);
            logFile = logPath.toFile();
            // 保存到数据库
            ScheduleLog scheduleLog = new ScheduleLog();
            scheduleLog.setPlanId(plan.getId());
            scheduleLog.setLogFilePath(fileName);
            scheduleLog.setStatus(Constant.TASK_STATUS_RUNNING);
            scheduleLog.setStartDate(new Date());
            scheduleLogService.save(scheduleLog);
        }
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return logFile;
    }

    /**
     * @Description: 日志写入
     * @param msg 信息
     * @param logFile 文件
     */
    public void log(String msg, File logFile) {
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new FileOutputStream(logFile, true));
            System.setOut(printStream);
            Date now = new Date();
            String strNow = timeFormat.format(now);
            System.out.printf("[%s]: %s\n", strNow, msg);
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
