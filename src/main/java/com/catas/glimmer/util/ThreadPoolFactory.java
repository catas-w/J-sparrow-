package com.catas.glimmer.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolFactory {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 50;
    private static final int KEEP_ALIVE = 60;
    private static final int BLOCK_QUEUE_CAPACITY = 100;

    private static final Map<String, ExecutorService> threadPoolMap = new ConcurrentHashMap<>();

    public static ExecutorService createDefaultThreadPool(String namePrefix) {
        return createDefaultThreadPool(namePrefix, false);
    }

    // 创建默认线程池
    public static ExecutorService createDefaultThreadPool(String namePrefix, boolean isDaemon) {
        ExecutorService pool = threadPoolMap.computeIfAbsent(namePrefix, k -> createThreadPool(namePrefix, isDaemon));
        if (pool.isShutdown() || pool.isTerminated()) {
            threadPoolMap.remove(namePrefix);
            pool = createThreadPool(namePrefix, isDaemon);
            threadPoolMap.put(namePrefix, pool);
        }
        return pool;
    }

    // 创建一个线程池
    public static ExecutorService createThreadPool(String namePrefix, boolean isDaemon) {
        ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(BLOCK_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(namePrefix, isDaemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, blockingQueue, threadFactory);
    }

    // 自定义线程工厂
    public static ThreadFactory createThreadFactory(String namePrefix, boolean isDaemon) {
        if (namePrefix != null) {
            return new ThreadFactoryBuilder().setNameFormat(namePrefix + "-%d").setDaemon(isDaemon).build();
        }
        return Executors.defaultThreadFactory();
    }

}
