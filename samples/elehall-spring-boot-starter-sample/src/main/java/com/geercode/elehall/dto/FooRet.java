package com.geercode.elehall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description : </p>
 * <p>Created on  : 2019-12-27 16:12:51</p>
 *
 * @author jerryniu
 */
@ApiModel
@Data
@Accessors(chain = true)
public class FooRet {
    @ApiModelProperty
    private long id;

    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private String nickname;
}
