package com.geercode.elehall.codegen.config.orm.convert;

/**
 * <p>Description : 数据类型转换接口</p>
 * <p>Created on  : 2019-10-08 11:12:34</p>
 *
 * @author jerryniu
 */
public interface ITypeConvert {
    IColumnType processTypeConvert(String fieldType);
}