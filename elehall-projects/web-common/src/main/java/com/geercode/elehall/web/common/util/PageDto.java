package com.geercode.elehall.web.common.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * <p>Description : 分页数据体</p>
 * <p>Created on  : 2019-10-12 18:10:53</p>
 *
 * @author jerryniu
 */
@ApiModel(value = "分页数据体")
@Data
@Accessors(chain = true)
public final class PageDto<T> {
    @ApiModelProperty(value = "页号>从1开始 (1..N)", example = "1")
    private int page;
    @ApiModelProperty(value = "页容量", example = "10")
    private int size;
    @ApiModelProperty(value = "总页数", example = "10")
    private long totolPage;
    @ApiModelProperty(value = "总数据量", example = "100")
    private long totolItem;
    @ApiModelProperty("数据列表")
    private Collection<T> data;
}
