package com.geercode.elehall.codegen.config.orm;

import com.geercode.elehall.codegen.config.CodegenConstant;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description : 数据库配置</p>
 * <p>Created on  : 2019-09-28 18:09:35</p>
 *
 * @author jerryniu
 */
@Data
@Accessors( chain = true)
public class DbCfg {
    private String dbType = CodegenConstant.DB_TPYE_MYSQL;
    private String driverClass =CodegenConstant.DB_DRIVER_MYSQL;
    private String url;
    private String username;
    private String password;
}
