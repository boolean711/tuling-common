package com.tuling.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.satoken.manager.SaTokenHolderManager;
import com.tuling.common.core.properties.TenantProperties;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.MyBatisSystemException;

import java.util.Date;

/**
 * MP注入处理器
 *
 * @author Lion Li
 * @date 2021/4/25
 */
@Slf4j
public class InjectionMetaObjectHandler implements MetaObjectHandler {


    private TenantProperties tenantProperties;

    public InjectionMetaObjectHandler(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    @Override
    public void insertFill(MetaObject metaObject) {


        log.info("新增开始自动填充=========");

        LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();
        log.error("TokenHolderManager.getToken()：{}", SaTokenHolderManager.getToken());
        if (SaTokenHolderManager.getToken()==null){
            printCurrentStackTrace();
        }
        if (loginUser == null) {
            throw new MyBatisSystemException(new Exception("未知用户，插入元数据失败，请联系管理员"));
        }
        Date now = new Date();
        insertFillCreate(metaObject, loginUser, now);
        fillUpdate(metaObject, loginUser, now);

        fillTenant(metaObject, loginUser);


    }

    private void fillTenant(MetaObject metaObject, LoginUserDetails loginUser) {
        if (tenantProperties.isEnable()) {

            Class<?> aClass = metaObject.getOriginalObject().getClass();

            TableInfo tableInfo = TableInfoHelper.getTableInfo(aClass);

            String tableName = tableInfo.getTableName();

            if (CollectionUtils.isNotEmpty(tenantProperties.getTableName())) {
                if (tenantProperties.getTableName().contains(tableName)) {


                    if (metaObject.hasSetter("tenantId") && getFieldValByName("tenantId", metaObject) == null) {
                        if (loginUser.isAdmin()) {
                            this.strictInsertFill(metaObject, "tenantId", Long.class, -1L);
                        } else {
                            this.strictInsertFill(metaObject, "tenantId", Long.class, loginUser.getTenantId());
                        }

                    }
                }
            }
        }
    }

    private void fillUpdate(MetaObject metaObject, LoginUserDetails loginUser, Date now) {
        if (metaObject.hasSetter("updateTime") && getFieldValByName("updateTime", metaObject) == null) {
            this.strictInsertFill(metaObject, "updateTime", Date.class, now);
        }
        if (metaObject.hasSetter("updateName") && getFieldValByName("updateName", metaObject) == null) {
            this.strictInsertFill(metaObject, "updateName", String.class, loginUser.getNickName());
        }
        if (metaObject.hasSetter("updateId") && getFieldValByName("updateId", metaObject) == null) {
            this.strictInsertFill(metaObject, "updateId", Long.class, loginUser.getId());
        }
    }

    private void insertFillCreate(MetaObject metaObject, LoginUserDetails loginUser, Date now) {

        if (metaObject.hasSetter("createTime") && getFieldValByName("createTime", metaObject) == null) {
            this.strictInsertFill(metaObject, "createTime", Date.class, now);
        }
        if (metaObject.hasSetter("createName") && getFieldValByName("createName", metaObject) == null) {
            this.strictInsertFill(metaObject, "createName", String.class, loginUser.getNickName());
        }
        if (metaObject.hasSetter("createId") && getFieldValByName("createId", metaObject) == null) {
            this.strictInsertFill(metaObject, "createId", Long.class, loginUser.getId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        log.info("修改开始自动填充=========");

        LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();
        log.error("TokenHolderManager.getToken()：{}", SaTokenHolderManager.getToken());
        if (SaTokenHolderManager.getToken()==null){
            printCurrentStackTrace();
        }
        if (loginUser == null) {
            throw new MyBatisSystemException(new Exception("未知用户，插入元数据失败，请联系管理员"));
        }

        fillUpdate(metaObject, loginUser, new Date());


    }
    private  void printCurrentStackTrace() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            log.info("element:{}",element.toString());
        }
    }

}
