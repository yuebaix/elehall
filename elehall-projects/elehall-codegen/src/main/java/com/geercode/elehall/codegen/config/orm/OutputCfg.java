package com.geercode.elehall.codegen.config.orm;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description : 模板配置</p>
 * <p>Created on  : 2019-09-29 10:09:06</p>
 *
 * @author jerryniu
 */
@Data
@Accessors( chain = true)
public class OutputCfg {
    private String templatePath;
    private String outputPath;
    private String outputNamePattern;
}
