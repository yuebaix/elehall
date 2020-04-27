package com.geercode.elehall.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Description : 类工具</p>
 * <p>Created on  : 2018-11-02 15:59</p>
 *
 * @author jerryniu
 */
public final class ClassUtil {
    private ClassUtil() {
    }

    /**
     * <p>description : 获取是否有对应的类被加载</p>
     * <p>create   on : 2018-11-02 16:26:12</p>
     *
     * @author jerryniu
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        if (className == null || "".equals(className)) {
            throw new IllegalArgumentException("ClassUtil ---> className不能为空");
        }
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        try {
            Class clazz = classLoader == null ? Class.forName(className) : classLoader.loadClass(className);
            return clazz != null;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * <p>description : 获取默认的ClassLoader</p>
     * <p>create   on : 2018-11-02 16:26:31</p>
     *
     * @author jerryniu
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            cl = ClassUtil.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                }
            }
        }
        return cl;
    }

    public static String getShortClassName(String className) {
        if (className == null) {
            return null;
        }
        String[] ss = className.split("\\.");
        StringBuilder sb = new StringBuilder(className.length());
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            if (i != ss.length - 1) {
                sb.append(s.charAt(0)).append('.');
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * <p>Description : 获取类中的所有字段</p>
     * <p>Created on  : 2019-10-18 17:28:31</p>
     *
     * @author jerryniu
     */
    public static List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            Class superClass = clazz.getSuperclass();
            if (superClass != null) {
                fields.addAll(getAllFields(clazz.getSuperclass()));
            }
        }
        return fields;
    }
}
