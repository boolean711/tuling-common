package com.tuling.common.excel.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.excel.param.BaseExcelReadDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
public abstract class BaseDataReadListener<READ extends BaseExcelReadDto> implements ReadListener<READ> {

    protected List<Object> services = new ArrayList<>();
    private int maxSize = 500;
    private Executor executor;

    private List<READ> invokeList = new ArrayList<>();
    private List<Log> logList = new ArrayList<>();

    protected BaseDataReadListener(Builder<READ> builder) {
        this.maxSize = builder.maxSize;
        this.executor = builder.executor;
        this.services = builder.services;
    }

    public static class Builder<READ extends BaseExcelReadDto> {
        private int maxSize = 500;
        private Executor executor;
        private final List<Object> services = new ArrayList<>();
        private final Class<? extends BaseDataReadListener<READ>> listenerClass;

        public Builder(Class<? extends BaseDataReadListener<READ>> listenerClass) {
            this.listenerClass = listenerClass;
        }

        public Builder<READ> setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder<READ> setExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder<READ> addService(Object service) {
            this.services.add(service);
            return this;
        }

        public BaseDataReadListener<READ> build() {
            try {
                Constructor<? extends BaseDataReadListener<READ>> declaredConstructor = listenerClass.getDeclaredConstructor(Builder.class);
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance(this);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create listener instance", e);
            }
        }
    }

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
        if (executor == null) {
            throw new ServiceException("未指定线程池==========");
        }
        String error = checkData(data);
        if (StrUtil.isBlank(error)) {
            invokeList.add(data);
            if (invokeList.size() >= maxSize) {
                List<READ> readList=new ArrayList<>(invokeList);
                invokeList.clear();
                CompletableFuture.runAsync(() -> {
                    doInvoke(readList);

                }, executor).handle((res, ex) -> {
                    if (ex != null) {
                        saveLog(new Log(getRowIndex(context), ex.getMessage(), false, new Date()));
                    }
                    return res;
                });
            }
        }
        saveLog(new Log(getRowIndex(context), error, StrUtil.isBlank(error), new Date()));
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
            List<Log> saveLogList=new ArrayList<>(logList);
            logList.clear();
            CompletableFuture.runAsync(() -> {
                doSaveLog(saveLogList);

            }, executor).handle((res, ex) -> {
                if (ex != null) {
                  doSaveLog(Collections.singletonList(new Log(log.getRowNum(), ex.getMessage(), false, new Date())));
                }
                return res;
            });
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
