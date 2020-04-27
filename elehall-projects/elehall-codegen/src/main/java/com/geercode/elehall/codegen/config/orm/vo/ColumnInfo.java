package com.geercode.elehall.codegen.config.orm.vo;

import lombok.Data;

/**
 * <p>Description : 字段信息</p>
 * <p>Created on  : 2019-09-29 17:09:13</p>
 *
 * @author jerryniu
 */
@Data
public class ColumnInfo {
    private String name;
    private String nameCamelCase;
    private String comment;
    private String typeOrigin;
    private String type;
    private String typePackage;
    private String key;
    private String extra;
}
