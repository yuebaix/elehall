package com.geercode.elehall.web.common.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Description : jackson敏感数据注解</p>
 * <p>Created on  : 2019-10-18 18:10:46</p>
 *
 * 注意：与@JsonIgnore不能混用
 *
 * @author jerryniu
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveDataSerializer.class)
public @interface Sensitive {
    SensitiveDataType value() default SensitiveDataType.ALL;
}
