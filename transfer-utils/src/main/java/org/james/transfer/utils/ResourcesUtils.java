package org.james.transfer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * Resources Utils
 *
 * 查找资源文件
 *
 * @author James Gosl
 * @since 2023/03/12 18:45
 */
public class ResourcesUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesUtils.class);

    /**
     * 查找Properties 文件
     *
     * @param name 文件名/全限定名
     * @return Properties 包装类
     */
    public static Properties getProperties(String name) {
        Properties properties = new Properties();
        try {
            properties.load(getResourceAsStream(name));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            e.printStackTrace();
        }

        return properties;
    }

    public static URL getResource(String name) {
        // 以当前类所在位置查找 所以需要加上绝对位置
//        ResourcesUtils.class.getResource(“/” + name);

        // 以项目所在位置进行查找
        URL resource = ResourcesUtils.class.getClassLoader().getResource(name);
        return checkNull(resource, name);
    }

    public static InputStream getResourceAsStream(String name) {
        String configPath = "";
        try {
            // 适配 jar 和ide
            URL resource = ResourcesUtils.class.getClassLoader().getResource(name);
            String protocol = resource.getProtocol();
            if (protocol.equals("file")) {
                configPath = URLDecoder.decode(resource.getPath(), "UTF-8");
            } else {
                configPath = new File(name).getAbsolutePath();
            }

            return checkNull(new FileInputStream(configPath), name);
        } catch (Exception e) {
            throw new RuntimeException("配置文件未找到");
        }
    }

    public static <T> T checkNull(T obj, String name) {
        return obj != null ? obj : error(name + " is null !!!");
    }

    private static <T> T error(String message) {
        throw new RuntimeException(message);
    }
}
