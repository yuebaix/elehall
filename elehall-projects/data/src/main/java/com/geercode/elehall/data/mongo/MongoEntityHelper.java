package com.geercode.elehall.data.mongo;

import com.geercode.elehall.common.exception.ElehallRuntimeException;
import com.geercode.elehall.common.util.ClassUtil;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>Description : 实体类帮助工具</p>
 * <p>Created on  : 2019-11-11 15:11:57</p>
 *
 * @author jerryniu
 */
public final class MongoEntityHelper {
    private MongoEntityHelper() {}

    /**
     * <p>Description : 获取entity的主键字段名</p>
     * <p>Created on  : 2019-12-02 19:18:51</p>
     *
     * @author jerryniu
     */
    public static Field getTablePkFieldOrPanic(Object entity) {
        Class clazz = entity.getClass();
        List<Field> fields = ClassUtil.getAllFields(clazz);
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Id) {
                    return field;
                }
            }
        }
        throw new ElehallRuntimeException("主键不存在");
    }

    /**
     * <p>Description : 获取数据库主键字段名</p>
     * <p>Created on  : 2019-12-02 19:10:32</p>
     *
     * @author jerryniu
     */
    public static String getPkNameOrPanic(Object entity) {
        Class clazz = entity.getClass();
        List<Field> fields = ClassUtil.getAllFields(clazz);
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Id) {
                    Annotation[] annos = field.getDeclaredAnnotations();
                    for (Annotation anno : annos) {
                        if (anno instanceof org.springframework.data.mongodb.core.mapping.Field) {
                            org.springframework.data.mongodb.core.mapping.Field f = (org.springframework.data.mongodb.core.mapping.Field) anno;
                            return f.value();
                        }
                    }
                }
            }
        }
        throw new ElehallRuntimeException("主键不存在");
    }

    /**
     * <p>Description : 获取数据主键值的字符串表示</p>
     * <p>Created on  : 2019-12-02 19:10:45</p>
     *
     * @author jerryniu
     */
    @SneakyThrows
    public static String getPkStrValueOrPanic(Object entity) {
        String pkName = getTablePkFieldOrPanic(entity).getName();
        Class clazz = entity.getClass();
        Field pkField = clazz.getDeclaredField(pkName);
        if (!pkField.isAccessible()) {
            pkField.setAccessible(true);
        }
        Object pkObj = pkField.get(entity);
        if (pkObj != null) {
            return pkObj.toString();
        } else {
            throw new ElehallRuntimeException("主键内容不存在");
        }
    }
}
