package com.geercode.elehall.codegen.generator;

import com.geercode.elehall.codegen.common.ElehallCodegenException;
import com.geercode.elehall.codegen.config.CodegenConstant;
import com.geercode.elehall.codegen.config.orm.*;
import com.geercode.elehall.codegen.engine.TemplateEngineStepBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description : 单包模式下的jpa代码生成</p>
 * <p>Created on  : 2019-09-29 10:09:33</p>
 *
 * @author jerryniu
 */
public class OrmGeneratorImpl implements OrmGenerator {
    private volatile static OrmGenerator holder;
    private static final char[] HOLDER_MONITOR = new char[0];
    private OrmConfig ormConfig;

    public OrmGeneratorImpl(OrmConfig ormConfig) {
        this.ormConfig = ormConfig;
        executeConfigChange();
    }
    /**
     * <p>description : 获取单模块代码生成器</p>
     * <p>create   on : 2018-09-11 16:14:53</p>
     *
     * @author jerryniu
     */
    public static OrmGenerator getStandaloneModuleHolder() {
        if (holder == null) {
            synchronized (HOLDER_MONITOR) {
                if (holder == null) {
                    OrmConfig ormConfig = OrmConfigBuilder.build();
                    holder = new OrmGeneratorImpl(ormConfig);
                }
            }
        }
        return holder;
    }

    @Override
    public void genBase() {
        throw new ElehallCodegenException("暂不支持base文件生成");
    }

    @Override
    public void genXml() {
        throw new ElehallCodegenException("暂不支持xml文件生成");
    }

    @Override
    public void genEntity() {
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String codeType = optCfg.getCodeType();
        if (CodegenConstant.CODE_TYPE_JPA.equalsIgnoreCase(codeType)) {
            String basicCodePath = pkgCfg.getRootArtifactDir()
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
            OutputCfg entityOutputCfg = new OutputCfg().setTemplatePath("/freemarker/jpa/entity")
                    .setOutputPath(basicCodePath + File.separator + "entity")
                    .setOutputNamePattern(optCfg.getEntityPattern());
            outputCfgList.add(entityOutputCfg);
        } else if (CodegenConstant.CODE_TYPE_MYBATISPLUS.equalsIgnoreCase(codeType)) {
            String basicCodePath = pkgCfg.getRootArtifactDir()
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
            OutputCfg entityOutputCfg = new OutputCfg().setTemplatePath("/freemarker/mybatisplus/entity")
                    .setOutputPath(basicCodePath + File.separator + "entity")
                    .setOutputNamePattern(optCfg.getEntityPattern());
            outputCfgList.add(entityOutputCfg);
        } else {
            throw new ElehallCodegenException("设定的生成代码类型没有得到支持: " + codeType);
        }
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Override
    public void genDao() {

    }

    @Override
    public void genService() {

    }

    @Override
    public void genWeb() {
        throw new ElehallCodegenException("暂不支持web文件生成");
    }

    @Override
    public void genAll() {
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    private void executeConfigChange() {

    }
}
