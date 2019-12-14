package com.geercode.elehall.web.common.base;

import com.geercode.elehall.common.exception.ElehallFrameworkException;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>description : 基础回复编码</p>
 *
 * @author jerryniu
 */
public final class BaseCode {
    private static final Map<Integer, Builder> codeRegistry = new HashMap();

    public static final Builder SUCCESS = new Builder(0, "成功");
    public static final Builder FAIL = new Builder(1, "失败");
    public static final Builder ERROR = new Builder(-100, "未知异常");
    public static final Builder REVIEW = new Builder(-200, "校验异常");

    /**
     * <p>description : 用回复码返回对应枚举类型</p>
     */
    public static final BaseCode.Builder getResponseCodeByCode(int code) {
        for (Map.Entry<Integer, Builder> entry : codeRegistry.entrySet()) {
            if (entry.getKey().equals(code)) {
                return  entry.getValue();
            }
        }
        return null;
    }

    public static final Builder createCode(Integer code, String msg) {
        if (codeRegistry.get(code) != null) {
            throw new ElehallFrameworkException("ResponseCode Already Exist");
        }
        return new Builder(code, msg);
    }

    public static final class Builder {
        /**回复码*/
        private Integer code;
        /**回复信息*/
        private String msg;

        private Builder(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
            codeRegistry.put(code, this);
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}