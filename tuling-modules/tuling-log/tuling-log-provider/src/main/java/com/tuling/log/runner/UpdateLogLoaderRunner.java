package com.tuling.log.runner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.log.domain.entity.SysUpdateLog;
import com.tuling.log.domain.vo.SysUpdateLogVo;
import com.tuling.log.service.SysUpdateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class UpdateLogLoaderRunner implements CommandLineRunner {

    @Autowired
    private SysUpdateLogService updateLogService;

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("update/updateLog.xml");
        try (InputStream inputStream = classPathResource.getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // 解析XML文件并写入数据库
            NodeList updateNodes = document.getElementsByTagName("update");
            List<SysUpdateLog> updateLogs = new ArrayList<>();
            for (int i = 0; i < updateNodes.getLength(); i++) {
                Node updateNode = updateNodes.item(i);
                if (updateNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element updateElement = (Element) updateNode;

                    Integer orderNum = Integer.parseInt(updateElement.getElementsByTagName("orderNum").item(0).getTextContent());
                    String version = updateElement.getElementsByTagName("version").item(0).getTextContent();
                    String content = updateElement.getElementsByTagName("content").item(0).getTextContent();
                    SysUpdateLog sysUpdateLog = new SysUpdateLog();
                    sysUpdateLog.setOrderNum(orderNum);
                    sysUpdateLog.setVersion(version);
                    sysUpdateLog.setContent(content);
                    sysUpdateLog.setNeedInsertMetaData(false);
                    updateLogs.add(sysUpdateLog);
                }
            }
            List<SysUpdateLogVo> listByVersion = updateLogService.getListByVersion(updateLogs.get(0).getVersion());

            if (CollectionUtils.isNotEmpty(listByVersion)) {
                for (SysUpdateLogVo updateLogVo : listByVersion) {
                    SysUpdateLog sysUpdateLog = BeanUtil.toBean(updateLogVo, SysUpdateLog.class);
                    updateLogs.remove(sysUpdateLog);
                }
            }

            if (CollectionUtils.isNotEmpty(updateLogs)) {
                updateLogService.saveBatch(updateLogs);
            }
            log.info("插入更新日志成功：{}", JSONUtil.toJsonStr(updateLogs));

        }
    }
}
