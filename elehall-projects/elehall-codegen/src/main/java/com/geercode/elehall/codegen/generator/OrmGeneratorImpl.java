package com.geercode.elehall.codegen.generator;

import com.geercode.elehall.codegen.common.ElehallCodegenException;
import com.geercode.elehall.codegen.config.CodegenConstant;
import com.geercode.elehall.codegen.config.orm.*;
import com.geercode.elehall.codegen.engine.TemplateEngineStepBuilder;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String baseCodePackagePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + optCfg.getBaseCodePackage().replace(CodegenConstant.DOT, File.separator);
        Set<String> baseTemplateFileNameSet = OrmConfigBuilder.getSubFileName(optCfg.getBaseTemplateParentPath());
        for (String templateName : baseTemplateFileNameSet) {
            OutputCfg baseOutputCfg = new OutputCfg().setTemplatePath(optCfg.getBaseTemplateParentPath() + templateName)
                    .setOutputPath(baseCodePackagePath)
                    .setOutputNamePattern("%s");
            outputCfgList.add(baseOutputCfg);
        }
        System.out.println(outputCfgList);
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().genBase().open();
    }

    @Override
    public void genXml() {
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String resourcePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "resources" + File.separator
                + "mapper";
        OutputCfg xmlOutputCfg = new OutputCfg().setTemplatePath(optCfg.getXmlTemplatePath())
                .setOutputPath(resourcePath + File.separator + optCfg.getXmlSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getXmlPattern())
                .setFileExtension(CodegenConstant.FILE_EXT_XML);
        outputCfgList.add(xmlOutputCfg);
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Override
    public void genEntity() {
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String basicCodePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
        OutputCfg entityOutputCfg = new OutputCfg().setTemplatePath(optCfg.getEntityTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getEntitySubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getEntityPattern());
        outputCfgList.add(entityOutputCfg);
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Override
    public void genDao() {
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String basicCodePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
        OutputCfg daoOutputCfg = new OutputCfg().setTemplatePath(optCfg.getDaoTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getDaoSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getDaoPattern());
        outputCfgList.add(daoOutputCfg);
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Override
    public void genService() {
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String basicCodePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
        OutputCfg serviceOutputCfg = new OutputCfg().setTemplatePath(optCfg.getServiceTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getServiceSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getServicePattern());
        OutputCfg serviceImplOutputCfg = new OutputCfg().setTemplatePath(optCfg.getServiceImplTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getServiceImplSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getServiceImplPattern());
        outputCfgList.add(serviceOutputCfg);
        outputCfgList.add(serviceImplOutputCfg);
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Override
    public void genWeb() {
        List<OutputCfg> outputCfgList = new ArrayList();
        PkgCfg pkgCfg = ormConfig.getPkgCfg();
        OptCfg optCfg = ormConfig.getOptCfg();
        String basicCodePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
        OutputCfg controllerOutputCfg = new OutputCfg().setTemplatePath(optCfg.getControllerTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getControllerSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getControllerPattern());
        outputCfgList.add(controllerOutputCfg);
        ormConfig.setOutputCfgList(outputCfgList);
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Override
    public void genAll() {
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    private void executeConfigChange() {

    }
}
