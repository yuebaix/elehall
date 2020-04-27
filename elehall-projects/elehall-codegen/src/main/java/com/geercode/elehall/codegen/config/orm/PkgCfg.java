package com.geercode.elehall.codegen.config.orm;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description : 包配置</p>
 * <p>Created on  : 2019-09-28 18:09:51</p>
 *
 * @author jerryniu
 */
@Data
@Accessors( chain = true)
public class PkgCfg {
    private String basePackage;
    private String rootArtifactDir;
}
