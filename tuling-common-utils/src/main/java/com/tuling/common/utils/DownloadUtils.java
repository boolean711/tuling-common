package com.tuling.common.utils;

import cn.hutool.core.io.IoUtil;
import com.tuling.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Slf4j
public class DownloadUtils {
    private static final String TEMPLATE_PATH = "template"+File.separator;

    /**
     * 下载classpath下的文件
     *
     * @param response HttpServletResponse
     * @param fileName 要下载的文件名
     */
    public static void downloadFromClasspath(HttpServletResponse response, String fileName) {
        try {
            // 构造文件完整路径
            String filePath = TEMPLATE_PATH + fileName;
            ClassPathResource classPathResource = new ClassPathResource(filePath);

            if (!classPathResource.exists()) {
                throw new ServiceException("文件不存在，请联系管理员");
            }

            // 设置响应类型
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            // 设置文件名编码，防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");

            try (InputStream inputStream = classPathResource.getInputStream();
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                IoUtil.copy(bufferedInputStream, response.getOutputStream());
            }
        } catch (IOException e) {
            log.error("下载文件时发生异常", e);
            throw new ServiceException("下载文件时发生异常，请联系管理员", e);
        }
    }
}
