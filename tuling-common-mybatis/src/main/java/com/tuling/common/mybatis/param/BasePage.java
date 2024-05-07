package com.tuling.common.mybatis.param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


@Data
public class BasePage {


    private int size = 10;

    private int page = 1;

    private boolean needPage = true;


    public IPage popPage() {

        return new Page<>(page, size);
    }


}
