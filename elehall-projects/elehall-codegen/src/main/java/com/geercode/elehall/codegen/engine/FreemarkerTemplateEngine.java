package com.geercode.elehall.codegen.engine;

import com.geercode.elehall.codegen.config.AbstractConfig;
import com.geercode.elehall.codegen.config.CodegenConstant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

/**
 * <p>Description : freemarker引擎</p>
 * <p>Created on  : 2019-09-28 17:09:25</p>
 *
 * @author jerryniu
 */
@Slf4j
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {
    private Configuration configuration;

    @Override
    public void init(AbstractConfig configBuilder) {
        configuration = new Configuration();
        configuration.setDefaultEncoding(CodegenConstant.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class, CodegenConstant.SLASH);
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) {
        Template template = null;
        FileOutputStream fileOutputStream = null;
        try {
            template = configuration.getTemplate(templatePath);
            fileOutputStream = new FileOutputStream(new File(outputFile));
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, CodegenConstant.UTF8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        StringBuilder fp = new StringBuilder();
        fp.append(filePath).append(".ftl");
        return fp.toString();
    }
}
