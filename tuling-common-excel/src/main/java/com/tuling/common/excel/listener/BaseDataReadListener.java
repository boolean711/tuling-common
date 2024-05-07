package com.tuling.common.excel.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.tuling.common.core.param.BaseExcelReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class BaseDataReadListener<READ extends BaseExcelReadDto> implements ReadListener<READ> {


    protected Object[] services;

    public BaseDataReadListener(Object... services) {
        this.services = services;
    }

    private List<READ> invokeList = new ArrayList<>();


    private Map<Integer, String> invokeResult = new HashMap<>();


    private int successNum = 0;

    @Override
    public void onException(Exception exception, AnalysisContext context) {


        if (exception instanceof ExcelDataConvertException){
            invokeResult.put(getRowIndex(context), "日期格式不正确");
        }else{
            invokeResult.put(getRowIndex(context), exception.getMessage());
        }

    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {

        ReadListener.super.invokeHead(headMap, context);

    }

    @Override
    public void invoke(READ data, AnalysisContext context) {
        String s = checkData(data);
        // 获取当前行号，注意行号从1开始

        if (StrUtil.isBlank(s)) {

            invokeList.add(data);
            successNum++;
        } else {
            invokeResult.put(getRowIndex(context), s);
        }

    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        invokeResult.put(0, successNum + "");
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }

    protected abstract String checkData(READ data);

    private Integer getRowIndex(AnalysisContext context) {
        return context.readRowHolder().getRowIndex() + 1;
    }


}
