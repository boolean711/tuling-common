package com.tuling.common.web.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.builder.QueryBuilder;

import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import com.tuling.common.core.param.BaseVo;
import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.web.mapper.CrudBaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CrudBaseServiceImpl
        <E extends BaseEntity, VO extends BaseVo,
                SAVE extends BaseSaveDto,
                M extends CrudBaseMapper<E>
                >
        extends ServiceImpl<M, E>
        implements CrudBaseIService<E, VO, SAVE> {


    @Override
    public Map<Long, VO> getIdVoMap(Predicate<E> predicate) {

        List<E> list = this.list();

        if (CollectionUtil.isNotEmpty(list)) {
            Stream<E> stream = list.stream();

            if (predicate != null) {
                stream = stream.filter(predicate);
            }

            return stream.collect(Collectors.toMap(
                    BaseEntity::getId,
                    entity -> BeanUtil.toBean(entity, getVoClass()),
                    (k1, k2) -> k1
            ));

        }
        return new HashMap<>(0);
    }

    @Override
    public IPage<VO> pageListByExpression(ExpressionQueryDto<E> queryDto) {

        Class<E> entityClass = getEntityClass();
        QueryWrapper<E> eQueryWrapper = QueryBuilder.buildQueryWrapper(entityClass, queryDto);
        if (queryDto.isNeedCustom()) {
            afterBuildQueryWrapper(eQueryWrapper);
        }

        List records = null;
        IPage page = null;
        if (queryDto.isNeedPage()) {
            page = this.baseMapper.selectPage(queryDto.popPage(), eQueryWrapper);
            records = page.getRecords();
        } else {
            page = new Page();

            records = this.baseMapper.selectList(eQueryWrapper);

        }


        List<VO> vos = (List<VO>) BeanListUtils.copyList(records, getVoClass());


        if (CollectionUtil.isNotEmpty(vos)) {

            afterPageListByExpression(vos);

        }


        return page.setRecords(vos);
    }

    @Override
    @Transactional
    public Long saveOrUpdate(SAVE dto) {

        beforeSave(dto);

        E entityInstance = createEntityInstance();
        BeanUtils.copyProperties(dto, entityInstance);

        saveOrUpdate(entityInstance);

        afterSave(dto, entityInstance);

        return entityInstance.getId();
    }

    @Override
    public VO getInfoById(Long id) {
        E e = this.baseMapper.selectById(id);


        VO voInstance = createVOInstance();

        if (e != null) {
            BeanUtils.copyProperties(e, voInstance);
            afterGetInfoById(voInstance);

            return voInstance;
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean removeByIds(List<Long> ids) {

        List<E> es = listByIds(ids);

        beforeRemove(es);
        this.baseMapper.deleteBatchIds(ids);
        afterRemove(es);
        return true;
    }

    @SafeVarargs
    protected final LambdaQueryWrapper<E> queryDestinedCol(SFunction<E, ?>... columns) {
        LambdaQueryWrapper<E> lqw = new LambdaQueryWrapper<>();

        lqw.select(columns);
        return lqw;


    }

    private E createEntityInstance() {
        try {
            return getEntityClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            throw new RuntimeException("Failed to create entity instance", e);
        }
    }

    private VO createVOInstance() {
        try {

            return getVoClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            throw new RuntimeException("Failed to create entity instance", e);
        }
    }

    private Class<VO> getVoClass() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<VO>) genericSuperclass.getActualTypeArguments()[1];
    }

    public void beforeSave(SAVE dto) {
    }


    public void afterSave(SAVE dto, E entity) {
    }


    public void beforeRemove(List<E> entityList) {
    }


    public void afterRemove(List<E> entityList) {
    }


    public void afterGetInfoById(VO vo) {
    }

    public void afterPageListByExpression(List<VO> records) {
    }

    public void afterBuildQueryWrapper(QueryWrapper<E> eQueryWrapper) {

    }


}