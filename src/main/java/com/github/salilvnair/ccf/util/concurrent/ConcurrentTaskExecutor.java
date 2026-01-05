package com.github.salilvnair.ccf.util.concurrent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentTaskExecutor {

    public static <T> Future<T> execute(CallableContextTask<T> contextTask) throws InterruptedException {
        return execute(null, contextTask);
    }

    public static <T> Future<T> execute(ExecutorService executor, CallableContextTask<T> contextTask) throws InterruptedException {
        executor = executor == null ? Executors.newCachedThreadPool() : executor;
        Future<T> future = executor.submit(contextTask);
        executor.shutdown();
        return future;
    }


    public static <T> List<Future<T>> execute(List<? extends CallableContextTask<T>> contextTask) throws InterruptedException {
        return execute(null, contextTask);
    }

    public static <T> List<Future<T>> execute(ExecutorService executor, List<? extends CallableContextTask<T>> contextTasks) throws InterruptedException {
        executor = executor == null ? Executors.newCachedThreadPool() : executor;
        List<Future<T>> futures = executor.invokeAll(contextTasks);
        executor.shutdown();
        return futures;
    }

    public static <T> Future<List<T>> executeListTask(CallableListTask<T> callableListTask) throws InterruptedException {
        return executeListTask(null, callableListTask);
    }

    public static <T> Future<List<T>> executeListTask(ExecutorService executor, CallableListTask<T> callableListTask) throws InterruptedException {
        executor = executor == null ? Executors.newCachedThreadPool() : executor;
        Future<List<T>> future = executor.submit(callableListTask);
        executor.shutdown();
        return future;
    }


    public static <T> List<Future<List<T>>> executeListTasks(List<? extends CallableListTask<T>> listTasks) throws InterruptedException {
        return executeListTasks(null, listTasks);
    }

    public static <T> List<Future<List<T>>> executeListTasks(ExecutorService executor, List<? extends CallableListTask<T>> listTasks) throws InterruptedException {
        executor = executor == null ? Executors.newCachedThreadPool() : executor;
        List<Future<List<T>>> futures = executor.invokeAll(listTasks);
        executor.shutdown();
        return futures;
    }

    public static <T> Future<Map<String, List<T>>> executeMapTask(CallableMapTask<T> callableMapTask) throws InterruptedException {
        return executeMapTask(null, callableMapTask);
    }

    public static <T> Future<Map<String, List<T>>> executeMapTask(ExecutorService executor, CallableMapTask<T> callableMapTask) throws InterruptedException {
        executor = executor == null ? Executors.newCachedThreadPool() : executor;
        Future<Map<String, List<T>>> future = executor.submit(callableMapTask);
        executor.shutdown();
        return future;
    }

    public static <T> List<Future<Map<String, List<T>>>> executeMapTasks(List<? extends CallableMapTask<T>> mapTasks) throws InterruptedException {
        return executeMapTasks(null, mapTasks);
    }

    public static <T> List<Future<Map<String, List<T>>>> executeMapTasks(ExecutorService executor, List<? extends CallableMapTask<T>> mapTasks) throws InterruptedException {
        executor = executor == null ? Executors.newCachedThreadPool() : executor;
        List<Future<Map<String, List<T>>>> futures = executor.invokeAll(mapTasks);
        executor.shutdown();
        return futures;
    }
}
