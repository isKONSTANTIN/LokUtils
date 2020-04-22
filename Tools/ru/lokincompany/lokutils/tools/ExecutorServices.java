package ru.lokincompany.lokutils.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServices {
    private static ExecutorService cachedThreadPool;
    private static ExecutorService stealingThreadPool = Executors.newWorkStealingPool();

    public static ExecutorService getCachedThreadPool(){
        if (cachedThreadPool == null)
            cachedThreadPool = Executors.newCachedThreadPool();

        return cachedThreadPool;
    }

    public static ExecutorService getStealingThreadPool(){
        if (stealingThreadPool == null)
            stealingThreadPool = Executors.newWorkStealingPool();

        return stealingThreadPool;
    }
}
