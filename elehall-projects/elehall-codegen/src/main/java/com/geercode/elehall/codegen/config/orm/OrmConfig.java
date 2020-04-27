package com.geercode.elehall.codegen.config.orm;

import com.geercode.elehall.codegen.config.AbstractConfig;
import com.geercode.elehall.codegen.config.orm.vo.TableInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>Description : orm配置类</p>
 * <p>Created on  : 2019-09-29 11:09:16</p>
 *
 * @author jerryniu
 */
@Data
@Accessors( chain = true)
public class OrmConfig extends AbstractConfig {
    private DbCfg dbCfg;
    private GavCfg gavCfg;
    private PkgCfg pkgCfg;
    private DcrpCfg dcrpCfg;
    private OptCfg optCfg;
    private List<OutputCfg> outputCfgList;
    private List<TableInfo> tableInfoList;
}
