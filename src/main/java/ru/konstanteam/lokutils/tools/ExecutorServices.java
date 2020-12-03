package ru.konstanteam.lokutils.tools;

import java.util.concurrent.*;

public class ExecutorServices {
    private static ExecutorService cachedThreadPool;
    private static ExecutorService stealingThreadPool;
    private static ScheduledExecutorService scheduledExecutorService;

    public static ExecutorService getCachedService() {
        if (cachedThreadPool == null)
            cachedThreadPool = new ThreadPoolExecutor(0, 2147483647, 5, TimeUnit.SECONDS, new SynchronousQueue());

        return cachedThreadPool;
    }

    public static ExecutorService getStealingService() {
        if (stealingThreadPool == null)
            stealingThreadPool = Executors.newWorkStealingPool();

        return stealingThreadPool;
    }

    public static ScheduledExecutorService getScheduledService() {
        if (scheduledExecutorService == null)
            scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        return scheduledExecutorService;
    }
}
