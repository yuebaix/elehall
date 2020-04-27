package com.geercode.elehall.codegen.config.orm.query;

/**
 * <p>Description : 抽象数据库信息查询类</p>
 * <p>Created on  : 2019-09-29 17:09:03</p>
 *
 * @author jerryniu
 */
public abstract class AbstractDbInfoQuery {
    public abstract String getTableQuery();
    public abstract String getColumnQuery();
}
