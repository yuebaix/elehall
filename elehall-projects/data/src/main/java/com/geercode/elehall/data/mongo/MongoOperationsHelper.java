package com.geercode.elehall.data.mongo;

import com.geercode.elehall.common.util.ClassUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

public class MongoOperationsHelper {
    private static final String DOC_FIELD_ID_NAME = "_id";
    private static final String DOC_FIELD_STATUS_NAME = "status";
    private static final String UPDATE_TIME = "update_time";

    private MongoOperationsHelper() {}

    public static void restrictQueryFields(Query query, List<String> selectFields) {
        if (!CollectionUtils.isEmpty(selectFields)) {
            for (String field : selectFields) {
                query.fields().include(field);
            }
        }
    }

    public static <T> Query getDynamicQueryByObject(T entity, Class<T> clazz) {
        Query query = new Query();
        List<Field> fieldList = ClassUtil.getAllFields(clazz);
        Criteria criteria = new Criteria();
        for (Field field : fieldList) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object fieldValue = getField(field, entity);
            if (fieldValue != null && null == field.getAnnotation(Transient.class)) {
                String documentFieldName = getDocumentFieldName(field);
                criteria.and(documentFieldName).is(fieldValue);
            }
        }
        return query.addCriteria(criteria);
    }

    public static <T> Update getDynamicUpdateByObject(T entity, Class<T> clazz) {
        Update update = new Update();
        List<Field> fieldList = ClassUtil.getAllFields(clazz);
        for (Field field : fieldList) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object fieldValue = getField(field, entity);
            String documentFieldName = getDocumentFieldName(field);
            if (fieldValue != null && null == field.getAnnotation(Transient.class)) {
                if (!DOC_FIELD_ID_NAME.equals(documentFieldName)) {
                    update.set(documentFieldName, fieldValue);
                }
            }
            /** 审计时间的代码*/
            if (UPDATE_TIME.equals(documentFieldName) && Number.class.isAssignableFrom(field.getType()) && null == field.getAnnotation(Transient.class)) {
                update.set(documentFieldName, System.currentTimeMillis()/1000);
            }
        }
        return update;
    }

    public static String getDocumentFieldName(Field field) {
        org.springframework.data.mongodb.core.mapping.Field fieldAnnotation = AnnotationUtils.getAnnotation(field, org.springframework.data.mongodb.core.mapping.Field.class);

        if (fieldAnnotation != null) {
            return fieldAnnotation.value();
        } else {
            Id idAnnotation = AnnotationUtils.getAnnotation(field, Id.class);
            if (idAnnotation != null) {
                return DOC_FIELD_ID_NAME;
            } else {
                return field.getName();
            }
        }
    }

    public static ExampleMatcher getFuzzyExampleMatcher(Object entity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        List<Field> fieldList = ClassUtil.getAllFields(entity.getClass());

        for (Field field : fieldList) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object fieldValue = getField(field, entity);
            String documentFieldName = getDocumentFieldName(field);
            Class fieldClazz = field.getType();
            if (fieldValue != null && String.class.equals(fieldClazz)
                    && !DOC_FIELD_ID_NAME.equals(documentFieldName) && !DOC_FIELD_STATUS_NAME.equals(documentFieldName)) {
                exampleMatcher = exampleMatcher.withMatcher(documentFieldName, ExampleMatcher.GenericPropertyMatchers.contains());
            }
        }
        return exampleMatcher;
    }

    /**
     * 获取field的值
     *
     * @param field
     * @param target
     */
    private static Object getField(Field field, Object target) {
        return ReflectionUtils.getField(field, target);
    }
}
