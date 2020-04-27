package com.geercode.elehall.codegen.config.orm;

import com.geercode.elehall.codegen.config.CodegenConstant;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description : 代码坐标配置</p>
 * <p>Created on  : 2019-09-28 17:09:07</p>
 *
 * @author jerryniu
 */
@Data
@Accessors( chain = true)
public class GavCfg {
    private String groupId;
    private String artifactId;
    private String version = CodegenConstant.DEFAULT_VERSION;
}
