package com.tuling.common.mybatis.param;

import com.tuling.common.core.param.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpressionQueryDto<E extends BaseEntity> extends BasePage {


    private Map<String, Map<ExpressionEnum, String>> query = new HashMap<>();


    private Map<Integer, SortOrder> sort = new HashMap<>();

    private boolean needCustom = false;

    @Data
    public static class SortOrder {
        private String field; // 排序字段
        private ExpressionEnum direction; // 排序方向
    }


    public enum ExpressionEnum {

        IN,
        LIKE,
        EQ,
        GE,
        LE,
        BETWEEN,
        DESC,
        ASC


    }

}
