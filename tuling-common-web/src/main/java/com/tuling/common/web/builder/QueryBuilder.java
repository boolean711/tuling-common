package com.tuling.common.web.builder;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.mybatis.param.ExpressionQueryDto;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class QueryBuilder {

    public static <E extends BaseEntity> QueryWrapper<E> buildQueryWrapper(Class<E> clazz, ExpressionQueryDto<E> queryDto) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();

        buildQuery(clazz, queryDto, queryWrapper);

        buildSort(clazz, queryDto, queryWrapper);

        return queryWrapper;

    }

    private static <E extends BaseEntity> void buildSort(Class<E> clazz, ExpressionQueryDto<E> queryDto, QueryWrapper<E> queryWrapper) {
        Map<Integer, ExpressionQueryDto.SortOrder> orderedSortMap = queryDto.getSort();
        if (orderedSortMap != null) {
            // 确保按照键（代表顺序）的自然顺序遍历
            Map<Integer, ExpressionQueryDto.SortOrder> sortedMap = new TreeMap<>(orderedSortMap);
            for (ExpressionQueryDto.SortOrder sortOrder : sortedMap.values()) {
                String fieldName = sortOrder.getField(); // 获取排序字段名
                ExpressionQueryDto.ExpressionEnum direction = sortOrder.getDirection(); // 获取排序方向

                // 需要确保字段名是有效的，防止SQL注入
                if (!isFieldPresentInClass(clazz, fieldName)) {
                    throw new RuntimeException("非法参数");
                }
                fieldName = camelToSnake(fieldName);

                switch (direction) {
                    case ASC:
                        queryWrapper.orderByAsc(fieldName);
                        break;
                    case DESC:
                        queryWrapper.orderByDesc(fieldName);
                        break;
                    default:
                        throw new RuntimeException("非法参数");
                }
            }
        }
    }

    private static <E extends BaseEntity> void buildQuery(Class<E> clazz, ExpressionQueryDto<E> queryDto, QueryWrapper<E> queryWrapper) {
        Map<String, Map<ExpressionQueryDto.ExpressionEnum, String>> query = queryDto.getQuery();
        // 遍历最外层Map
        for (Map.Entry<String, Map<ExpressionQueryDto.ExpressionEnum, String>> fieldEntry : query.entrySet()) {
            String fieldName = fieldEntry.getKey(); // 获取字段名

            // 需要确保字段名是有效的，防止SQL注入
            if (!isFieldPresentInClass(clazz, fieldName)) {
                throw new RuntimeException("非法参数");
            }
            fieldName = camelToSnake(fieldName);
            Map<ExpressionQueryDto.ExpressionEnum, String> expressions = fieldEntry.getValue(); // 获取表达式Map

            // 遍历内层Map
            for (Map.Entry<ExpressionQueryDto.ExpressionEnum, String> expressionEntry : expressions.entrySet()) {
                ExpressionQueryDto.ExpressionEnum expressionType = expressionEntry.getKey(); // 获取比较类型
                String value = expressionEntry.getValue(); // 获取字段值

                // 根据不同的比较类型添加条件
                switch (expressionType) {
                    case EQ:

                        if ("true".equalsIgnoreCase(value)) {
                            queryWrapper.eq(fieldName, true);
                        } else if ("false".equalsIgnoreCase(value)) {
                            queryWrapper.eq(fieldName, false);
                        } else {
                            queryWrapper.eq(fieldName, value);
                        }
                        break;
                    case LIKE:
                        queryWrapper.like(fieldName, value);
                        break;
                    case IN:
                        List<String> split = StrUtil.split(value, ",");

                        if (split != null && split.size() > 0) {
                            if (split.stream().allMatch(item -> item.equals("true") || item.equals("false"))) {
                                List<Boolean> collect = split.stream().map(Boolean::parseBoolean).collect(Collectors.toList());
                                queryWrapper.in(fieldName, collect);
                            } else {
                                queryWrapper.in(fieldName, split);
                            }

                        }

                        break;
                    case LE:
                        queryWrapper.le(fieldName, value);
                        break;
                    case GE:
                        queryWrapper.ge(fieldName, value);
                        break;
                    case BETWEEN:
                        // BETWEEN操作需要两个值，假设value是由"和"分隔的两个值
                        if (StrUtil.isNotBlank(value)) {
                            List<String> betweenValues = StrUtil.split(value, ",");
                            if (betweenValues != null && betweenValues.size() == 2) {
                                queryWrapper.between(fieldName, betweenValues.get(0), betweenValues.get(1));
                            } else {
                                throw new RuntimeException("非法参数");
                            }
                        }

                        break;
                    // ... 其他操作
                    default:
                        // 如果遇到未知的比较类型，可以选择忽略或抛出异常
                        throw new RuntimeException("非法参数");
                }
            }
        }
    }

    private static String camelToSnake(String str) {
        // 用正则表达式找到大写字母并在前面加上下划线，然后转换为小写
        return str.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    private static <E> boolean isFieldPresentInClass(Class<E> entityClass, String fieldName) {
        // 存储实体类及其所有父类的字段名
        Set<String> fieldNames = new HashSet<>();

        // 当前检查的类
        Class<?> currentClass = entityClass;

        // 遍历实体类及其所有父类
        while (currentClass != null && !currentClass.equals(Object.class)) {
            // 获取当前类的所有字段，包括私有字段
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                // 将字段名添加到集合中
                fieldNames.add(field.getName());
            }
            // 移动到父类进行检查
            currentClass = currentClass.getSuperclass();
        }

        // 检查字段名是否存在于集合中
        return fieldNames.contains(fieldName);
    }
}
