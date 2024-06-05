package com.tuling.common.excel.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.tuling.common.core.param.BaseExcelReadDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

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


    private boolean hasNext = true;

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
        this.hasNext = hasNext(context);
        CompletableFuture<Map<Integer, String>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String error = checkData(data);
            Map<Integer, String> map = new HashMap<>();
            map.put(getRowIndex(context), error);

            if(StrUtil.isBlank(error)){
                invokeList.add(data);
                if (!hasNext || invokeList.size() >= maxSize) {
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

        completableFuture.thenAcceptAsync(res -> {
            for (Map.Entry<Integer, String> entry : res.entrySet()) {
                boolean success = StrUtil.isBlank(entry.getValue());
                saveLog(new Log(entry.getKey(), entry.getValue(), success, new Date()));
            }
        }, executor);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }


    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("解析完所有数据===============");
    }

    protected abstract String checkData(READ data);

    private Integer getRowIndex(AnalysisContext context) {
        return context.readRowHolder().getRowIndex() + 1;
    }

    protected abstract void doInvoke(List<READ> data);

    private void saveLog(Log log) {
        logList.add(log);
        if (!hasNext || logList.size() >= maxSize) {
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
