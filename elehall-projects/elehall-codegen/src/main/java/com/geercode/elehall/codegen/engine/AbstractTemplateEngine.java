package com.geercode.elehall.codegen.engine;

import com.geercode.elehall.codegen.config.AbstractConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>Description : 抽象模板引擎</p>
 * <p>Created on  : 2019-09-28 17:09:43</p>
 *
 * @author jerryniu
 */
@Slf4j
@Data
public abstract class AbstractTemplateEngine {
    public abstract void init(AbstractConfig configBuilder);
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile);
    public abstract String templateFilePath(String filePath);
}
