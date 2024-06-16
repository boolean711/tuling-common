package com.tuling.wxmq.handler;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public abstract class AbstractHandler implements WxMpMessageHandler, InitializingBean {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() {
        logger.info("Initializing: " + this.getClass().getName());
    }
}
