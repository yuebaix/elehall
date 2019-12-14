package com.geercode.elehall.autoconfigure.web.common;

import com.geercode.elehall.web.common.base.BaseCode;
import com.geercode.elehall.web.common.base.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description : 响应码自动配置类</p>
 * <p>Created on  : 2019-12-11 17:12:36</p>
 *
 * @author jerryniu
 */
@ConditionalOnProperty(value = "com.geercode.elehall.web.common.responsecode.enable", havingValue = "true")
@ConditionalOnClass(BaseResp.class)
@EnableConfigurationProperties(ResponseCodeProperties.class)
@Configuration
public class ResponseCodeAutoConfiguration {
    @Autowired
    private ResponseCodeProperties responseCodeProperties;
    @Bean
    public BaseCode instance(ResponseCodeProperties responseCodeProperties) {
        System.out.println("");
        return null;
    }
}
