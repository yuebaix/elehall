package com.geercode.elehall.codegen.config.orm.query;

/**
 * <p>Description : mysql数据库查询类</p>
 * <p>Created on  : 2019-09-29 17:09:36</p>
 *
 * @author jerryniu
 */
public class MysqlQuery extends AbstractDbInfoQuery {
    @Override
    public String getTableQuery() {
        return "show table status";
    }

    @Override
    public String getColumnQuery() {
        return "show full fields from `%s`";
    }
}
