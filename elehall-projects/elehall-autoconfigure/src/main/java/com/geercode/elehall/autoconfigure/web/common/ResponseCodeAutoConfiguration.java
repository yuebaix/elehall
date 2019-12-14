package com.geercode.elehall.autoconfigure.web.common;

import com.geercode.elehall.common.exception.ElehallFrameworkException;
import com.geercode.elehall.web.common.base.BaseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * <p>Description : 响应码自动配置类</p>
 * <p>Created on  : 2019-12-11 17:12:36</p>
 *
 * @author jerryniu
 */
@ConditionalOnProperty(value = "geercode.elehall.web.common.responsecode.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(BaseCode.class)
@EnableConfigurationProperties(ResponseCodeProperties.class)
@Configuration
public class ResponseCodeAutoConfiguration {
    //系统级回复编码前缀
    private static final String CODE_LEVEL_SYS = "1";
    //服务模块级回复编码前缀
    private static final String CODE_LEVEL_SVC_MOD = "5";

    @Autowired
    private ResponseCodeProperties responseCodeProperties;

    @Bean
    public SysResponseCode sysResponseCode() {
        String svcModCode = responseCodeProperties.getSvcModCode();
        if (StringUtils.isEmpty(svcModCode)) {
            throw new ElehallFrameworkException("需要配置三位服务模块编码前缀");
        }
        int CODE_PREFIX_SYS = Integer.parseInt(CODE_LEVEL_SYS + svcModCode) * 10000;
        int CODE_PREFIX_SVC_MOD = Integer.parseInt(CODE_LEVEL_SVC_MOD + svcModCode) * 10000;

        BaseCode.Builder ARG_EMPTY = BaseCode.createCode(CODE_PREFIX_SYS + 1, "请求参数为空");
        BaseCode.Builder ARG_ILLEGAL = BaseCode.createCode(CODE_PREFIX_SYS + 2, "请求参数非法");
        BaseCode.Builder ARG_JSON_FAIL = BaseCode.createCode(CODE_PREFIX_SYS + 3, "JSON转换失败");
        BaseCode.Builder REQUEST_REPEAT = BaseCode.createCode(CODE_PREFIX_SYS + 4, "重复提交,接口幂等");
        BaseCode.Builder DB_EXCEPTION = BaseCode.createCode(CODE_PREFIX_SYS + 5, "数据库异常");
        BaseCode.Builder LOGIN_FAIL = BaseCode.createCode(CODE_PREFIX_SYS + 6, "登录失败");
        BaseCode.Builder USER_ILLEGAL = BaseCode.createCode(CODE_PREFIX_SYS + 7, "非法用户");
        BaseCode.Builder SIGN_VERIFY_FAIL = BaseCode.createCode(CODE_PREFIX_SYS + 8, "签名验证错误");
        BaseCode.Builder ERROR = BaseCode.createCode(CODE_PREFIX_SYS + 9999, "未知异常");

        return new SysResponseCode(CODE_PREFIX_SYS,
                CODE_PREFIX_SVC_MOD,
                ARG_EMPTY,
                ARG_ILLEGAL,
                ARG_JSON_FAIL,
                REQUEST_REPEAT,
                DB_EXCEPTION,
                LOGIN_FAIL,
                USER_ILLEGAL,
                SIGN_VERIFY_FAIL,
                ERROR);
    }
}
