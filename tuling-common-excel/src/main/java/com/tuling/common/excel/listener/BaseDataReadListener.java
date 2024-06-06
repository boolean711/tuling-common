package com.tuling.common.excel.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.tuling.common.excel.param.BaseExcelReadDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

@Data
@Slf4j
public abstract class BaseDataReadListener<READ extends BaseExcelReadDto> implements ReadListener<READ> {

    protected Object[] services;
    private final int maxSize = 50;
    private Executor executor;

    public BaseDataReadListener(Executor executor, Object... services) {
        this.executor = executor;
        this.services = services;
    }

    private List<READ> invokeList = new CopyOnWriteArrayList<>();
    private List<Log> logList = new CopyOnWriteArrayList<>();
    private List<CompletableFuture< Map<Integer, String>>> futures = new ArrayList<>();

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        Log log;
        if (exception instanceof ExcelDataConvertException) {
            log = new Log(getRowIndex(context), "时间格式不正确", false, new Date());
        } else {
            log = new Log(getRowIndex(context), exception.getMessage(), false, new Date());
        }
        saveLog(log);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ReadListener.super.invokeHead(headMap, context);
    }

    @Override
    public void invoke(READ data, AnalysisContext context) {
        CompletableFuture< Map<Integer, String>> future = CompletableFuture.supplyAsync(() -> {
            String error = checkData(data);
            Map<Integer, String> map = new HashMap<>();
            map.put(getRowIndex(context), error);

            if (StrUtil.isBlank(error)) {
                invokeList.add(data);
                if (invokeList.size() >= maxSize) {
                    doInvoke(new ArrayList<>(invokeList));
                    invokeList.clear();
                }
            }
            return map;
        }, executor).handle((res, ex) -> {
            if (ex != null) {
                saveLog(new Log(getRowIndex(context), ex.getMessage(), false, new Date()));
                return Collections.emptyMap();
            } else {
                return res;
            }
        });
        future.thenAcceptAsync(res -> {
            if (CollectionUtil.isNotEmpty(res)) {
                for (Map.Entry<Integer, String> entry : res.entrySet()) {
                    boolean success = StrUtil.isBlank(entry.getValue());
                    saveLog(new Log(entry.getKey(), entry.getValue(), success, new Date()));
                }
            }
        }, executor);

        futures.add(future);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return true;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("解析完所有数据===============");

        // 等待所有异步操作完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.join();

        // 保存未达到阈值的数据
        if (CollectionUtil.isNotEmpty(invokeList)) {
            log.info("未达到阈值的数据===============");
            doInvoke(new ArrayList<>(invokeList));
            invokeList.clear();
        }

        // 保存未达到阈值的日志
        if (CollectionUtil.isNotEmpty(logList)) {
            log.info("未达到阈值的日志数据===============");
            doSaveLog(new ArrayList<>(logList));
            logList.clear();
        }
    }

    protected abstract String checkData(READ data);

    private Integer getRowIndex(AnalysisContext context) {
        return context.readRowHolder().getRowIndex() + 1;
    }

    protected abstract void doInvoke(List<READ> data);

    private void saveLog(Log log) {
        logList.add(log);
        if (logList.size() >= maxSize) {
            doSaveLog(new ArrayList<>(logList));
            logList.clear();
        }
    }

    protected abstract void doSaveLog(List<Log> logs);

    @Data
    protected static class Log {
        private int rowNum;
        private String message;
        private boolean success;
        private Date time;

        public Log(int rowNum, String message, boolean success, Date time) {
            this.rowNum = rowNum;
            this.message = message;
            this.success = success;
            this.time = time;
        }
    }
}
