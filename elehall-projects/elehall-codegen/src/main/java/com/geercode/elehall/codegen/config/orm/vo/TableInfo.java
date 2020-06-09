package com.geercode.elehall.codegen.config.orm.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>Description : 表信息</p>
 * <p>Created on  : 2019-09-29 17:09:57</p>
 *
 * @author jerryniu
 */
@Data
public class TableInfo {
    private String name;
    private String nameCamelCase;
    private String comment;
    private boolean extendBase;
    private List<ColumnInfo> baseColumnList;
    private List<ColumnInfo> columnInfoList;
}
