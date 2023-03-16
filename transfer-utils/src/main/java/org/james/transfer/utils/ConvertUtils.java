package org.james.transfer.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Supplier;

/**
 * Convert Utils
 *
 * @author James Gosl
 * @since 2023/03/12 19:39
 */
public class ConvertUtils {

    /**
     * 字符转换 File
     *
     * @param path 路径
     * @param defaultPath 如果路劲不正常 将采用默认值
     * @return File
     */
    public static File convertFile(String path, Supplier<String> defaultPath) {
        File file = new File(path);
        if (!file.exists()) {
            file = new File(defaultPath.get());
        }

        return file;
    }

    public static String convertFileName(File file, Supplier<String> defaultFile) {
        if (!file.exists()) {
            return defaultFile.get();
        }
        return file.getName();
    }

    /**
     * 字符转换 Integer
     *
     * @param value 数字
     * @param defaultValue 如果提供的数字不正常或者小于0 那么采用默认值
     * @return Integer
     */
    public static Integer convertInteger(String value, Supplier<Integer> defaultValue) {
        Integer result;
        try {
            result = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            result = defaultValue.get();
        }
        return result < 0 ? defaultValue.get() : result;
    }

    /**
     * 字符转换 Long
     *
     * @param value 数字
     * @param defaultValue 如果提供的数字不正常或者小于0 那么采用默认值
     * @return Long
     */
    public static Long convertLong(String value, Supplier<Long> defaultValue) {
        Long result;
        try {
            result = Long.valueOf(value);
        } catch (NumberFormatException e) {
            result = defaultValue.get();
        }
        return result < 0 ? defaultValue.get() : result;
    }

    /**
     * 字符转换 String
     * @param str 字符
     * @param defaultStr 如果字符不正常 那么将采用该默认值
     * @return String
     */
    public static String convertRemote(String str, Supplier<String> defaultStr) {
        try {
            // 检测该IP 是否可以Ping
            if (null != str && !"".equals(str)
                    && InetAddress.getByName(str).isReachable(3000)) {
                return str;
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return defaultStr.get();
    }

    /**
     * 效验对象是否空
     * @param generic 通用类型
     * @param defaultGeneric 默认
     * @param <T> Type
     * @return T
     */
    public static <T> T convertGeneric(T generic, Supplier<T> defaultGeneric) {
        return generic == null ? defaultGeneric.get() : generic;
    }
}
