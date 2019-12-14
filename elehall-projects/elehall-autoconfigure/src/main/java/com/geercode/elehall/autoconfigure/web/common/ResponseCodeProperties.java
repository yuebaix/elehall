package com.geercode.elehall.autoconfigure.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description : 响应码属性</p>
 * <p>Created on  : 2019-12-11 17:12:45</p>
 *
 * @author jerryniu
 */
@ConfigurationProperties(prefix = "geercode.elehall.web.common.responsecode")
@Data
public class ResponseCodeProperties {
    /**
     * 是否开启
     */
    private boolean enable = true;
    /**
     * 三位服务模块编码前缀
     */
    String svcModCode;
}
