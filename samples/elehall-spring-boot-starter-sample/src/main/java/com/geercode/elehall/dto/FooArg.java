package com.geercode.elehall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>Description : </p>
 * <p>Created on  : 2019-12-27 16:12:43</p>
 *
 * @author jerryniu
 */
@ApiModel
@Data
@Accessors(chain = true)
public class FooArg {
    @ApiModelProperty
    @NotNull
    private long id;

    @ApiModelProperty
    @NotNull
    private String name;
}
