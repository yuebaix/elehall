package com.geercode.elehall.web.common.exception;

/**
 * <p>Description : 自定义业务异常类</p>
 *
 * @author jerryniu
 */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
