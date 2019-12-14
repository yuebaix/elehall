package com.geercode.elehall.autoconfigure.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * <p>Description : 响应码属性</p>
 * <p>Created on  : 2019-12-11 17:12:45</p>
 *
 * @author jerryniu
 */
@ConfigurationProperties(prefix = "com.geercode.elehall.web.common.responsecode")
@Data
public class ResponseCodeProperties {
    /**
     * 是否开启
     */
    private boolean enable = true;
    /**
     * 规则
     */
    private Map<String, String> rules;
}
