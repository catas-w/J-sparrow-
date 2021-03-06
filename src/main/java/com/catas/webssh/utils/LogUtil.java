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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private static final BlockingQueue<Object[]> messageQueue;

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    static {
        messageQueue = new LinkedBlockingQueue<>();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Object[] task = messageQueue.take();
                        writeToLog(task);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * write specific message to log file
     */
    private static void writeToLog(Object[] task) {
        String time = (String) task[0];
        String msg = (String) task[1];
        File file = (File) task[2];
        try (PrintStream printStream = new PrintStream(new FileOutputStream(file, true))) {
            System.setOut(printStream);
            System.out.printf("[%s]: %s\n", time, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: ????????????
     * @param msg ??????
     * @param logFile ??????
     */
    public void log(String msg, File logFile) {
        Date now = new Date();
        String strNow = timeFormat.format(now);
        try {
            // ??????????????? blocking queue
            messageQueue.put(new Object[]{strNow, msg, logFile});
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description: SSH ????????????
     * @return log file
     */
    public File init(String uuid, Integer bindHostId, Integer userId) throws IOException {
        // ??????????????????
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

        // ??????????????????
        String tag = Path.of(dateStr, uuid).toString();
        Sessionlog sessionlog = new Sessionlog();
        sessionlog.setSessionTag(tag);
        sessionlog.setBindHostId(bindHostId);
        sessionlog.setUserId(userId);
        sessionlogService.save(sessionlog);

        return logFile;
    }

    /**
     * @Description: init ??????????????????
     * @return ????????????
     */
    public File initScheduleLog(Plan plan)  {
        System.out.println(this.scheduleLogPath);
        QueryWrapper<ScheduleLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", plan.getId());
        queryWrapper.orderByDesc("start_date");
        List<ScheduleLog> scheduleLogs = scheduleLogService.list(queryWrapper);

        File logFile = null;
        if (!scheduleLogs.isEmpty()) {
            // ??????????????????
            Path logPath = Path.of(this.scheduleLogPath, scheduleLogs.get(0).getLogFilePath());
            logFile = logPath.toFile();
        }else {
            // ???????????????. ?????????
            String fileName = "Plan-" + plan.getId() + ".log";
            Path logPath = Path.of(this.scheduleLogPath, fileName);
            logFile = logPath.toFile();
            // ??????????????????
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

}
