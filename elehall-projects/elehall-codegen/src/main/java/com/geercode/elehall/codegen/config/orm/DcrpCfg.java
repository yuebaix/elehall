package com.geercode.elehall.codegen.config.orm;

import com.geercode.elehall.codegen.config.CodegenConstant;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description : 描述配置</p>
 * <p>Created on  : 2019-09-28 18:09:17</p>
 *
 * @author jerryniu
 */
@Data
@Accessors( chain = true)
public class DcrpCfg {
    private String author;
    private String header = CodegenConstant.APACHE2_LICENCE_HEADER;
}
