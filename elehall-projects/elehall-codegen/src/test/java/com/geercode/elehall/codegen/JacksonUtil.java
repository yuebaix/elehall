package com.geercode.elehall.codegen;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JacksonUtil {
    private volatile static ObjectMapper objectMapperHolder;

    private JacksonUtil() {
    }

    public static ObjectMapper getObjectMapperHolder() {
        if (objectMapperHolder == null) {
            synchronized (JacksonUtil.class) {
                if (objectMapperHolder == null) {
                    ObjectMapper ob = new ObjectMapper();
                    //允许序列化空POJO
                    ob.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                    //把时间按照字符串输出
                    ob.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    //POJO中的null值不输出
                    ob.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    //在遇到未知属性的时候不抛出异常
                    ob.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    objectMapperHolder = ob;
                }
            }
        }
        return objectMapperHolder;
    }
}
