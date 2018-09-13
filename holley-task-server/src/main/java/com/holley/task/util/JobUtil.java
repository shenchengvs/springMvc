package com.holley.task.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class JobUtil {

    private final static Logger    logger            = Logger.getLogger(JobUtil.class);
    private static ExecutorService CACHE_THREAD_POOL = null;

    private synchronized static ExecutorService getCacheTreadPool() {
        if (CACHE_THREAD_POOL == null) {
            CACHE_THREAD_POOL = Executors.newCachedThreadPool();
        }
        return CACHE_THREAD_POOL;
    }

    public static <T> T call(Callable<T> c) {
        try {
            return getCacheTreadPool().submit(c).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T call(Callable<T> c, long timeout) {
        try {
            return getCacheTreadPool().submit(c).get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void execute(Runnable r) {
        getCacheTreadPool().execute(r);
    }

    /**
     * 停止Cache线程池
     */
    public static void stopCachePool() {
        if (CACHE_THREAD_POOL != null) {
            CACHE_THREAD_POOL.shutdown();
        }
    }

}
