package com.geercode.elehall.codegen.config.orm.convert;

/**
 * <p>Description : 获取实体类字段属性类信息接口</p>
 * <p>Created on  : 2019-10-08 11:10:08</p>
 *
 * @author jerryniu
 */
public interface IColumnType {
    String getType();
    String getPkg();
}
