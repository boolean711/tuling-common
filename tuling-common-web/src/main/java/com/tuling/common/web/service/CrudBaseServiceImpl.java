package com.tuling.common.web.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.*;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.builder.QueryBuilder;

import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.web.mapper.CrudBaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
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
        return Collections.emptyMap();
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


    @Override
    public <T extends BaseTreeVo> List<T> buildTree(Class<T > prototypeClass) {
        ExpressionQueryDto<BaseEntity> queryDto = new ExpressionQueryDto<>();
        queryDto.setNeedPage(false);

        IPage<VO> voiPage = this.pageListByExpression((ExpressionQueryDto<E>) queryDto);
        List<VO> records = voiPage.getRecords();

        if (CollectionUtil.isNotEmpty(records) && isTreeNode(records.get(0))) {
            List<TreeNode> treeNodes = records.stream()
                    .map(record -> (TreeNode) record)
                    .collect(Collectors.toList());

            treeNodes = buildTree(treeNodes);

            return treeNodes.stream().map(item -> BaseTreeVo.createInstance(prototypeClass, item)).collect(Collectors.toList());

        }

        return Collections.emptyList();


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


    private List<TreeNode> buildTree(List<TreeNode> voList) {

        Class<? extends TreeNode> voClass = voList.get(0).getClass();


        Field parentIdField = ReflectUtil.getField(voClass, "parentId");
        Field childrenField = ReflectUtil.getField(voClass, "children");

        if (parentIdField == null) {
            throw new ServiceException("Vo class must have a 'parentId' field.");
        }
        if (childrenField == null) {
            throw new ServiceException("Vo class must have a 'children' field.");
        }

        // 创建一个Map，用于根据id快速查找节点
        Map<Long, TreeNode> nodeMap = new HashMap<>();

        // 遍历列表，将每个节点放入Map中
        for (TreeNode vo : voList) {
            // 使用反射设置id字段的值
            parentIdField.setAccessible(true);
            nodeMap.put(vo.getId(), vo);
        }

        // 创建根节点列表
        List<TreeNode> rootNodes = new ArrayList<>();

        // 再次遍历列表，根据parentId将节点添加到对应的父节点下
        for (TreeNode vo : voList) {
            try {
                // 使用反射获取parentId字段的值
                parentIdField.setAccessible(true);
                Long parentId = (Long) parentIdField.get(vo);
                if (parentId == 0L) {
                    // 如果parentId为0，说明是根节点
                    rootNodes.add(vo);
                } else {
                    // 根据parentId找到父节点
                    TreeNode parent = nodeMap.get(parentId);
                    if (parent != null) {
                        // 使用反射获取children字段的值
                        childrenField.setAccessible(true);
                        List<TreeNode> childrenList = (List<TreeNode>) childrenField.get(parent);
                        if (childrenList != null) {
                            // 将当前节点添加到父节点的children列表中
                            childrenList.add(vo);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new ServiceException("Failed to access fields.", e);
            }
        }

        // 返回根节点列表，即树的结构
        return rootNodes;
    }


    private  boolean isTreeNode(Object obj) {

        Class<?> clazz = obj.getClass();
        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface == TreeNode.class) {
                return true;
            }
        }
        return false;
    }
}