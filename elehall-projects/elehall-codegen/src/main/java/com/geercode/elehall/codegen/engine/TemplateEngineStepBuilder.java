package com.geercode.elehall.codegen.engine;

import com.geercode.elehall.codegen.common.ElehallCodegenException;
import com.geercode.elehall.codegen.config.AbstractConfig;
import com.geercode.elehall.codegen.config.CodegenConstant;
import com.geercode.elehall.codegen.config.orm.OrmConfig;
import com.geercode.elehall.codegen.config.orm.OutputCfg;
import com.geercode.elehall.codegen.config.orm.vo.ColumnInfo;
import com.geercode.elehall.codegen.config.orm.vo.TableInfo;
import com.geercode.elehall.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>Description : 模板引擎执行步骤</p>
 * <p>Created on  : 2019-09-28 18:09:16</p>
 *
 * @author jerryniu
 */
@Slf4j
public class TemplateEngineStepBuilder {
    public static InitStep builder(String builderType) {
        if (CodegenConstant.BUILDER_TYPE_ORM.equalsIgnoreCase(builderType)) {
            return new OrmTemplateBuilder();
        } else {
            throw new ElehallCodegenException("不支持的builderType");
        }
    }
    
    private static final class OrmTemplateBuilder implements InitStep, MkdirsStep, BatchOutputStep,BatchOrOpenStep, OpenStep {
        private AbstractTemplateEngine engine;
        private OrmConfig ormConfig;

        @Override
        public MkdirsStep init(AbstractConfig config) {
            if (!(config instanceof OrmConfig)) {
                throw new ElehallCodegenException("只能接受OrmConfig类型");
            }
            this.ormConfig = (OrmConfig) config;
            String engineType = ormConfig.getOptCfg().getEngineType();
            if (CodegenConstant.ENGINE_FREEMARKER.equalsIgnoreCase(engineType)) {
                engine = new FreemarkerTemplateEngine();
            } else {
                throw new ElehallCodegenException("设定的模板引擎没有得到支持: " + engineType);
            }
            engine.init(config);
            return this;
        }

        @Override
        public BatchOutputStep mkdirs() {
            Set<String> pathInfo = new HashSet();
            List<OutputCfg> outputCfgList = ormConfig.getOutputCfgList();
            for (OutputCfg outputCfg : outputCfgList) {
                pathInfo.add(outputCfg.getOutputPath());
            }
            pathInfo.forEach((value) -> {
                File dir = new File(value);
                if (!dir.exists()) {
                    boolean result = dir.mkdirs();
                    if (result) {
                        log.debug("创建目录： [" + value + "]");
                    }
                }
            });
            return this;
        }

        @Override
        public BatchOrOpenStep genBase() {
            Map<String, Object> basicParams = getBasicParams();
            List<OutputCfg> outputCfgList = ormConfig.getOutputCfgList();
            Map<String, Object> params = new HashMap();
            params.putAll(basicParams);
            for (OutputCfg outputCfg : outputCfgList) {
                String templateStr = outputCfg.getTemplatePath();
                String templateFileName = templateStr;
                if (templateStr.contains(CodegenConstant.SLASH)) {
                    templateFileName = templateStr.substring(templateStr.lastIndexOf(CodegenConstant.SLASH), templateStr.length());
                }
                String templatePath = engine.templateFilePath(outputCfg.getTemplatePath());
                String outputFile = outputCfg.getOutputPath() + File.separator + String.format(outputCfg.getOutputNamePattern(), templateFileName) + ".java";
                if (!fileExist(outputFile) || ormConfig.getOptCfg().isOverride()) {
                    engine.writer(params, templatePath, outputFile);
                } else {
                    log.debug("文件" + outputFile + "已存在并且设置为不可覆盖，不生成该文件");
                }
            }
            return this;
        }

        @Override
        public OpenStep batchOutput() {
            Map<String, Object> basicParams = getBasicParams();
            List<TableInfo> tableInfoList = ormConfig.getTableInfoList();
            List<OutputCfg> outputCfgList = ormConfig.getOutputCfgList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> params = new HashMap();
                params.putAll(basicParams);
                String tableNameCamelCase = tableInfo.getNameCamelCase();
                params.put("table", tableInfo);
                params.put("entityName", String.format(ormConfig.getOptCfg().getEntityPattern(), tableNameCamelCase));
                params.put("daoName", String.format(ormConfig.getOptCfg().getDaoPattern(), tableNameCamelCase));
                params.put("serviceName", String.format(ormConfig.getOptCfg().getServicePattern(), tableNameCamelCase));
                params.put("serviceImplName", String.format(ormConfig.getOptCfg().getServiceImplPattern(), tableNameCamelCase));
                params.put("controllerName", String.format(ormConfig.getOptCfg().getControllerPattern(), tableNameCamelCase));

                params.put("entitySubPkg", ormConfig.getOptCfg().getEntitySubPkg());
                params.put("daoSubPkg", ormConfig.getOptCfg().getDaoSubPkg());
                params.put("serviceSubPkg", ormConfig.getOptCfg().getServiceSubPkg());
                params.put("serviceImplSubPkg", ormConfig.getOptCfg().getServiceImplSubPkg());
                params.put("controllerSubPkg", ormConfig.getOptCfg().getControllerSubPkg());

                //entity.ftl
                params.put("importPackages", getImportPackages(tableInfo));
                //dao
                params.put("pkTypeClass", getPkTypeClass(tableInfo));

                for (OutputCfg outputCfg : outputCfgList) {
                    String templatePath = engine.templateFilePath(outputCfg.getTemplatePath());
                    String outputFile = outputCfg.getOutputPath() + File.separator + String.format(outputCfg.getOutputNamePattern(), tableNameCamelCase) + CodegenConstant.DOT + outputCfg.getFileExtension();
                    if (!fileExist(outputFile) || ormConfig.getOptCfg().isOverride()) {
                        engine.writer(params, templatePath, outputFile);
                    } else {
                        log.debug("文件" + outputFile + "已存在并且设置为不可覆盖，不生成该文件");
                    }
                }
            }
            return this;
        }

        @Override
        public void open() {
            String outDir = ormConfig.getPkgCfg().getRootArtifactDir();
            if (!StringUtil.isEmpty(outDir)) {
                try {
                    String osName = System.getProperty("os.name");
                    if (osName != null) {
                        if (osName.contains("Mac")) {
                            Runtime.getRuntime().exec("open " + outDir);
                        } else if (osName.contains("Windows")) {
                            Runtime.getRuntime().exec("cmd /c start " + outDir);
                        } else {
                            log.debug("文件输出目录:" + outDir);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Map<String, Object> getBasicParams() {
            String nower = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            Map<String, Object> params = new HashMap();
            params.put("date", nower);
            params.put("author", ormConfig.getDcrpCfg().getAuthor());
            params.put("header", ormConfig.getDcrpCfg().getHeader());
            params.put("package", ormConfig.getPkgCfg().getBasePackage());
            params.put("baseCodePackage", ormConfig.getOptCfg().getBaseCodePackage());
            return params;
        }

        private Set<String> getImportPackages(TableInfo tableInfo) {
            List<ColumnInfo> columnInfoList = tableInfo.getColumnInfoList();
            Set<String> importPackagesSet = new HashSet();
            for (ColumnInfo columnInfo : columnInfoList) {
                String pkg = columnInfo.getTypePackage();
                if (!StringUtil.isEmpty(pkg)) {
                    importPackagesSet.add(pkg);
                }
            }
            return importPackagesSet;
        }

        private String getPkTypeClass(TableInfo tableInfo) {
            List<ColumnInfo> columnInfoList = tableInfo.getColumnInfoList();
            for (ColumnInfo columnInfo : columnInfoList) {
                if (CodegenConstant.PK_SIGN_MYSQL.equalsIgnoreCase(columnInfo.getKey())) {
                    return columnInfo.getType();
                }
            }
            throw new ElehallCodegenException(tableInfo.getName() + "没有主键存在，请检查");
        }

        private boolean fileExist(String filePath) {
            File file = new File(filePath);
            boolean exist = file.exists();
            return exist;
        }
    }

    public interface InitStep {
        MkdirsStep init(AbstractConfig config);
    }

    public interface MkdirsStep {
        BatchOutputStep mkdirs();
    }

    public interface BatchOutputStep {
        BatchOrOpenStep genBase();
        OpenStep batchOutput();
    }

    public interface OpenStep {
        void open();
    }

    public interface BatchOrOpenStep extends BatchOutputStep, OpenStep {

    }
}
