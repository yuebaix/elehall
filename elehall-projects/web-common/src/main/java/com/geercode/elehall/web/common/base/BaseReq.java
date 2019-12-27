package com.geercode.elehall.web.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description : 基础请求类</p>
 * <p>Created on  : 2019-12-27 15:12:51</p>
 *
 * @author jerryniu
 */
@ApiModel("请求基类")
@Data
public class BaseReq<T> {
    @ApiModelProperty("内容")
    private T cnt;

    /**
     * <p>description : 构造请求类</p>
     */
    public static <T> BaseReq<T> of(T cnt) {
        BaseReq<T> req = new BaseReq();
        req.setCnt(cnt);
        return req;
    }
}
