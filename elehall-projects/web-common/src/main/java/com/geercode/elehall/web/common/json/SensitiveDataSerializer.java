package com.geercode.elehall.web.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.geercode.elehall.common.util.StringUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * <p>Description : 敏感数据json序列化器</p>
 * <p>Created on  : 2019-10-18 18:10:40</p>
 *
 * @author jerryniu
 */
public class SensitiveDataSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveDataType type;

    public SensitiveDataSerializer() {
    }

    public SensitiveDataSerializer(final SensitiveDataType type) {
        this.type = type;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.type) {
            case CHINESE_NAME: {
                jsonGenerator.writeString(StringUtil.muteChineseName(s));
                break;
            }
            case ID_CARD: {
                jsonGenerator.writeString(StringUtil.muteIdNo(s));
                break;
            }
            case FIXED_PHONE: {
                jsonGenerator.writeString(StringUtil.muteFixedPhone(s));
                break;
            }
            case MOBILE_PHONE: {
                jsonGenerator.writeString(StringUtil.muteMobilePhone(s));
                break;
            }
            case ADDRESS: {
                jsonGenerator.writeString(StringUtil.muteAddr(s, 4));
                break;
            }
            case EMAIL: {
                jsonGenerator.writeString(StringUtil.muteEmail(s));
                break;
            }
            case BANK_CARD: {
                jsonGenerator.writeString(StringUtil.muteBankCard(s));
                break;
            }
            case CNAPS_CODE: {
                jsonGenerator.writeString(StringUtil.muteCnapsCode(s));
                break;
            }
            default:
                jsonGenerator.writeString(StringUtil.muteAll(s));
                break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual (SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {//为空直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {//非 String 类直接跳过
                Sensitive sensitiveInfo = beanProperty.getAnnotation(Sensitive.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (sensitiveInfo != null) {//如果能得到注解，就将注解的 value 传入 SensitiveDataSerializer
                    return new SensitiveDataSerializer(sensitiveInfo.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
