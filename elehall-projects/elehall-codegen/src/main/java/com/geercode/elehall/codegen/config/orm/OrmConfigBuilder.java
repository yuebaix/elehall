package com.geercode.elehall.codegen.config.orm;

import com.geercode.elehall.codegen.common.ElehallCodegenException;
import com.geercode.elehall.codegen.config.CodegenConstant;
import com.geercode.elehall.codegen.config.orm.convert.IColumnType;
import com.geercode.elehall.codegen.config.orm.convert.ITypeConvert;
import com.geercode.elehall.codegen.config.orm.convert.MySqlTypeConvert;
import com.geercode.elehall.codegen.config.orm.query.AbstractDbInfoQuery;
import com.geercode.elehall.codegen.config.orm.query.MysqlQuery;
import com.geercode.elehall.codegen.config.orm.vo.ColumnInfo;
import com.geercode.elehall.codegen.config.orm.vo.TableInfo;
import com.geercode.elehall.common.util.ResourceUtil;
import com.geercode.elehall.common.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * <p>Description : 配置读取类</p>
 * <p>Created on  : 2019-09-29 11:09:31</p>
 *
 * @author jerryniu
 */
@Slf4j
public class OrmConfigBuilder {
    private static final String CONFIG_FILE_PATH = "codegen-orm";

    //GavCfg
    private static final String GROUPID_KEY = "groupId";
    private static final String ARTIFACTID_KEY = "artifactId";
    private static final String VERSION_KEY = "version";//todo

    //PkgCfg
    private static final String BASE_PACKAGE_KEY = "basePackage";
    private static final String ROOTARTIFACTDIR_KEY = "rootArtifactDir";

    //DcrpCfg
    private static final String AUTHOR_KEY = "author";
    private static final String HEADER_KEY = "header";

    //DbCfg
    private static final String DATABASE_TYPE_KEY = "dbType";//todo
    private static final String DATABASE_DRIVER_CLASS_KEY = "driver";
    private static final String DATABASE_URL_KEY = "url";
    private static final String DATABASE_USER_KEY = "user";
    private static final String DATABASE_PASSWORD_KEY = "pwd";

    //OptCfg
    private static final String ENGINE_TYPE_KEY = "engineType";//todo
    private static final String CODE_TYPE_KEY = "codeType";//todo
    private static final String OVERRIDE_KEY = "override";
    private static final String INCLUDE_TABLES_KEY = "includeTables";
    private static final String BASE_COLUMNS_KEY = "baseColumns";
    private static final String BASE_CODE_PACKAGE_KEY = "baseCodePackage";
    private static final String BASE_TEMPLATE_PARENT_PATH_KEY = "baseTemplateParentPath";

    private static final String ENTITY_SUB_PKG_KEY = "entitySubPkg";
    private static final String DAO_SUB_PKG_KEY = "daoSubPkg";
    private static final String XML_SUB_PKG_KEY = "xmlSubPkg";
    private static final String SERVICE_SUB_PKG_KEY = "serviceSubPkg";
    private static final String SERVICEIMPL_SUB_PKG_KEY = "serviceImplSubPkg";
    private static final String CONTROLLER_SUB_PKG_KEY = "controllerSubPkg";

    private static final String ENTITY_PATTERN_KEY = "entityPattern";
    private static final String DAO_PATTERN_KEY = "daoPattern";
    private static final String XML_PATTERN_KEY = "xmlPattern";
    private static final String SERVICE_PATTERN_KEY = "servicePattern";
    private static final String SERVICEIMPL_PATTERN_KEY = "serviceImplPattern";
    private static final String CONTROLLER_PATTERN_KEY = "controllerPattern";

    private static final String ENTITY_TEMPLATE_PATH_KEY = "entityTemplatePath";
    private static final String DAO_TEMPLATE_PATH_KEY = "daoTemplatePath";
    private static final String XML_TEMPLATE_PATH_KEY = "xmlTemplatePath";
    private static final String SERVICE_TEMPLATE_PATH_KEY = "serviceTemplatePath";
    private static final String SERVICEIMPL_TEMPLATE_PATH_KEY = "serviceImplTemplatePath";
    private static final String CONTROLLER_TEMPLATE_PATH_KEY = "controllerTemplatePath";

    public static OrmConfig build() {
        GavCfg gavCfg = new GavCfg();
        PkgCfg pkgCfg = new PkgCfg();
        DcrpCfg dcrpCfg = new DcrpCfg();
        DbCfg dbCfg = new DbCfg();
        OptCfg optCfg = new OptCfg();
        List<OutputCfg> outputCfgList = new ArrayList();
        List<TableInfo> tableInfoList = new ArrayList();
        //GavCfg
        Map<String, String> properties = ResourceUtil.readPropertiesFromResources(CONFIG_FILE_PATH);
        String groupId = properties.get(GROUPID_KEY);
        String artifactId = properties.get(ARTIFACTID_KEY);
        String version = properties.get(VERSION_KEY);
        if (StringUtil.isEmpty(version)) {
            version = CodegenConstant.DEFAULT_VERSION;
        }
        gavCfg.setGroupId(groupId).setArtifactId(artifactId).setVersion(version);
        //PkgCfg
        String basePackage = properties.get(BASE_PACKAGE_KEY);
        if (StringUtil.isEmpty(basePackage)) {
            basePackage = groupId + CodegenConstant.DOT + artifactId;
        }
        String rootArtifactDir = properties.get(ROOTARTIFACTDIR_KEY);
        if (StringUtil.isEmpty(rootArtifactDir)) {
            rootArtifactDir = new File("").getAbsoluteFile().getParent();
        }
        if (!rootArtifactDir.endsWith(File.separator)) {
            rootArtifactDir += File.separator;
        }
        pkgCfg.setBasePackage(basePackage).setRootArtifactDir(rootArtifactDir);
        //DcrpCfg
        String author = properties.get(AUTHOR_KEY);
        String header = properties.get(HEADER_KEY);
        if (StringUtil.isEmpty(header)) {
            header = CodegenConstant.APACHE2_LICENCE_HEADER;
        }
        dcrpCfg.setAuthor(author).setHeader(header);
        //DbCfg
        String databaseType = properties.get(DATABASE_TYPE_KEY);
        if (StringUtil.isEmpty(databaseType)) {
            databaseType = CodegenConstant.DB_TPYE_MYSQL;
        }
        String databaseDriverClass = properties.get(DATABASE_DRIVER_CLASS_KEY);
        if (StringUtil.isEmpty(databaseDriverClass)) {
            databaseDriverClass = CodegenConstant.DB_DRIVER_MYSQL;
        }
        String databaseUrl = properties.get(DATABASE_URL_KEY);
        String databaseUser = properties.get(DATABASE_USER_KEY);
        String databasePassword = properties.get(DATABASE_PASSWORD_KEY);
        dbCfg.setDbType(databaseType)
                .setDriverClass(databaseDriverClass)
                .setUrl(databaseUrl)
                .setUsername(databaseUser)
                .setPassword(databasePassword);
        //OptCfg
        String engineType = properties.get(ENGINE_TYPE_KEY);
        if (StringUtil.isEmpty(engineType)) {
            engineType = CodegenConstant.ENGINE_FREEMARKER;
        }
        String codeType = properties.get(CODE_TYPE_KEY);
        if (StringUtil.isEmpty(codeType)) {
            codeType = CodegenConstant.CODE_TYPE_JPA;
        } else if (CodegenConstant.CODE_TYPE_MYBATISPLUS.equalsIgnoreCase(codeType)) {
            codeType = CodegenConstant.CODE_TYPE_MYBATISPLUS;
        }
        /**执行时判断*/
        boolean override = true;
        String overrideStr = properties.get(OVERRIDE_KEY);
        if (!StringUtil.isEmpty(overrideStr)) {
            override = Boolean.parseBoolean(overrideStr);
        }
        Set<String> includeTables = new HashSet();
        String includeTablesStr = properties.get(INCLUDE_TABLES_KEY);
        if (!StringUtil.isEmpty(includeTablesStr)) {
            includeTables = Arrays.stream(includeTablesStr.split(CodegenConstant.COMMA, -1)).collect(Collectors.toSet());
        }
        Set<String> baseColumns = new HashSet();
        String baseColumnsStr = properties.get(BASE_COLUMNS_KEY);
        if (!StringUtil.isEmpty(baseColumnsStr)) {
            baseColumns = Arrays.stream(baseColumnsStr.split(CodegenConstant.COMMA, -1)).collect(Collectors.toSet());
        }
        String baseCodePackage = properties.get(BASE_CODE_PACKAGE_KEY);
        if (StringUtil.isEmpty(baseCodePackage)) {
            baseCodePackage = pkgCfg.getBasePackage() + CodegenConstant.DOT + "common";
        }
        String baseTemplateParentPath = properties.get(BASE_TEMPLATE_PARENT_PATH_KEY);
        if (StringUtil.isEmpty(baseTemplateParentPath)) {
            baseTemplateParentPath = engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "base" + CodegenConstant.SLASH;
        }
        //子包
        String entitySubPkg = properties.get(ENTITY_SUB_PKG_KEY);
        if (StringUtil.isEmpty(entitySubPkg)) {
            entitySubPkg = "entity.ftl";
        }
        String daoSubPkg = properties.get(DAO_SUB_PKG_KEY);
        if (StringUtil.isEmpty(daoSubPkg)) {
            daoSubPkg = "dao";
        }
        String xmlSubPkg = properties.get(XML_SUB_PKG_KEY);
        if (StringUtil.isEmpty(xmlSubPkg)) {
            xmlSubPkg = "";
        }
        String serviceSubPkg = properties.get(SERVICE_SUB_PKG_KEY);
        if (StringUtil.isEmpty(serviceSubPkg)) {
            serviceSubPkg = "service";
        }
        String serviceImplSubPkg = properties.get(SERVICEIMPL_SUB_PKG_KEY);
        if (StringUtil.isEmpty(serviceImplSubPkg)) {
            serviceImplSubPkg = "service.impl";
        }
        String controllerSubPkg = properties.get(CONTROLLER_SUB_PKG_KEY);
        if (StringUtil.isEmpty(controllerSubPkg)) {
            controllerSubPkg = "controller";
        }
        //class名规则
        String entityPattern = properties.get(ENTITY_PATTERN_KEY);
        if (StringUtil.isEmpty(entityPattern)) {
            entityPattern = "%sEntity";
        }
        String daoPattern = properties.get(DAO_PATTERN_KEY);
        if (StringUtil.isEmpty(daoPattern)) {
            daoPattern = "%sDao";
        }
        String xmlPattern = properties.get(XML_PATTERN_KEY);
        if (StringUtil.isEmpty(xmlPattern)) {
            xmlPattern = "%sMapper";
        }
        String servicePattern = properties.get(SERVICE_PATTERN_KEY);
        if (StringUtil.isEmpty(servicePattern)) {
            servicePattern = "%sService";
        }
        String serviceImplPattern = properties.get(SERVICEIMPL_PATTERN_KEY);
        if (StringUtil.isEmpty(serviceImplPattern)) {
            serviceImplPattern = "%sServiceImpl";
        }
        String controllerPattern = properties.get(CONTROLLER_PATTERN_KEY);
        if (StringUtil.isEmpty(controllerPattern)) {
            controllerPattern = "%sController";
        }
        //模板路径
        String entityTemplatePath = properties.get(ENTITY_TEMPLATE_PATH_KEY);
        if (StringUtil.isEmpty(entityTemplatePath)) {
            entityTemplatePath = CodegenConstant.SLASH + engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "entity";
        }
        String daoTemplatePath = properties.get(DAO_TEMPLATE_PATH_KEY);
        if (StringUtil.isEmpty(daoTemplatePath)) {
            daoTemplatePath = CodegenConstant.SLASH + engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "dao";
        }
        String xmlTemplatePath = properties.get(XML_TEMPLATE_PATH_KEY);
        if (StringUtil.isEmpty(xmlTemplatePath)) {
            xmlTemplatePath = CodegenConstant.SLASH + engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "xml";
        }
        String serviceTemplatePath = properties.get(SERVICE_TEMPLATE_PATH_KEY);
        if (StringUtil.isEmpty(serviceTemplatePath)) {
            serviceTemplatePath = CodegenConstant.SLASH + engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "service";
        }
        String serviceImplTemplatePath = properties.get(SERVICEIMPL_TEMPLATE_PATH_KEY);
        if (StringUtil.isEmpty(serviceImplTemplatePath)) {
            serviceImplTemplatePath = CodegenConstant.SLASH + engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "serviceImpl";
        }
        String controllerTemplatePath = properties.get(CONTROLLER_TEMPLATE_PATH_KEY);
        if (StringUtil.isEmpty(controllerTemplatePath)) {
            controllerTemplatePath = CodegenConstant.SLASH + engineType + CodegenConstant.SLASH + codeType + CodegenConstant.SLASH + "controller";
        }

        optCfg.setEngineType(engineType).setCodeType(codeType).setOverride(override).setIncludeTables(includeTables)
                .setBaseColumns(baseColumns).setBaseCodePackage(baseCodePackage).setBaseTemplateParentPath(baseTemplateParentPath)
                .setEntitySubPkg(entitySubPkg).setDaoSubPkg(daoSubPkg).setXmlSubPkg(xmlSubPkg).setServiceSubPkg(serviceSubPkg).setServiceImplSubPkg(serviceImplSubPkg).setControllerSubPkg(controllerSubPkg)
                .setEntityPattern(entityPattern).setDaoPattern(daoPattern).setXmlPattern(xmlPattern).setServicePattern(servicePattern).setServiceImplPattern(serviceImplPattern).setControllerPattern(controllerPattern)
                .setEntityTemplatePath(entityTemplatePath).setDaoTemplatePath(daoTemplatePath).setXmlTemplatePath(xmlTemplatePath).setServiceTemplatePath(serviceTemplatePath).setServiceImplTemplatePath(serviceImplTemplatePath).setControllerTemplatePath(controllerTemplatePath);

        //OutputCfg
        String basicCodePath = pkgCfg.getRootArtifactDir()
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
        OutputCfg entityOutputCfg = new OutputCfg().setTemplatePath(optCfg.getEntityTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getEntitySubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getEntityPattern());
        OutputCfg daoOutputCfg = new OutputCfg().setTemplatePath(optCfg.getDaoTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getDaoSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getDaoPattern());
        OutputCfg serviceOutputCfg = new OutputCfg().setTemplatePath(optCfg.getServiceTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getServiceSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getServicePattern());
        OutputCfg serviceImplOutputCfg = new OutputCfg().setTemplatePath(optCfg.getServiceImplTemplatePath())
                .setOutputPath(basicCodePath + File.separator + optCfg.getServiceImplSubPkg().replace(CodegenConstant.DOT, File.separator))
                .setOutputNamePattern(optCfg.getServiceImplPattern());
        outputCfgList.add(entityOutputCfg);
        outputCfgList.add(daoOutputCfg);
        outputCfgList.add(serviceOutputCfg);
        outputCfgList.add(serviceImplOutputCfg);

        //TableInfoCfg
        //todo 目前只支持mysql
        Connection connection = getConnection(dbCfg);
        AbstractDbInfoQuery query = getQuery(dbCfg);

        String tableInfoQuery = query.getTableQuery();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(tableInfoQuery);
            ResultSet tableResults = preparedStatement.executeQuery();

            while (tableResults.next()) {
                String tableName = tableResults.getString("NAME");
                String tableComment = tableResults.getString("COMMENT");
                TableInfo tableInfo = new TableInfo();
                tableInfo.setName(tableName);
                tableInfo.setComment(tableComment);
                tableInfo.setNameCamelCase(StringUtil.capitalFirst(StringUtil.underlineToCamel(tableName)));
                //如果包含该表则继续查询字段信息
                if (includeTables.isEmpty() || includeTables.contains(tableName)) {
                    String columnInfoQuery = String.format(query.getColumnQuery(), tableName);
                    preparedStatement = connection.prepareStatement(columnInfoQuery);
                    ResultSet columnResults = preparedStatement.executeQuery();
                    List<ColumnInfo> columnInfoList = new ArrayList();
                    while (columnResults.next()) {
                        String columnName = columnResults.getString("FIELD");
                        String columnType = columnResults.getString("TYPE");
                        String columnComment = columnResults.getString("COMMENT");
                        String columnKey = columnResults.getString("KEY");
                        String columnExtra = columnResults.getString("EXTRA");

                        ITypeConvert converter = new MySqlTypeConvert();
                        IColumnType type = converter.processTypeConvert(columnType);

                        ColumnInfo columnInfo = new ColumnInfo();
                        columnInfo.setName(columnName);
                        columnInfo.setNameCamelCase(StringUtil.underlineToCamel(columnName));
                        columnInfo.setTypeOrigin(columnType);
                        columnInfo.setType(type.getType());
                        columnInfo.setTypePackage(type.getPkg());
                        columnInfo.setComment(columnComment);
                        columnInfo.setKey(columnKey);
                        columnInfo.setExtra(columnExtra);
                        columnInfoList.add(columnInfo);
                        log.debug(columnInfo.toString());
                    }
                    if (couldExtendBase(columnInfoList, baseColumns)) {
                        List<ColumnInfo> baseColumnList = evalbaseColumn(columnInfoList, baseColumns);
                        tableInfo.setExtendBase(true);
                        tableInfo.setBaseColumnList(baseColumnList);
                        columnInfoList.removeAll(baseColumnList);
                    }
                    tableInfo.setColumnInfoList(columnInfoList);
                    tableInfoList.add(tableInfo);
                    log.debug(tableInfo.toString());
                }
            }
            log.debug(tableInfoList.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return new OrmConfig().setGavCfg(gavCfg)
                .setPkgCfg(pkgCfg)
                .setDcrpCfg(dcrpCfg)
                .setDbCfg(dbCfg)
                .setOptCfg(optCfg)
                .setOutputCfgList(outputCfgList)
                .setTableInfoList(tableInfoList);
    }

    private static Connection getConnection(DbCfg dbCfg) {
        Connection conn = null;
        try {
            Class.forName(dbCfg.getDriverClass());
            conn = DriverManager.getConnection(dbCfg.getUrl(), dbCfg.getUsername(), dbCfg.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static AbstractDbInfoQuery getQuery(DbCfg dbCfg) {
        String dbType = dbCfg.getDbType();
        if (CodegenConstant.DB_TPYE_MYSQL.equalsIgnoreCase(dbType)) {
            return new MysqlQuery();
        } else {
            throw new ElehallCodegenException("设定的数据库类型没有得到支持: " + dbType);
        }
    }

    private static boolean couldExtendBase(List<ColumnInfo> columnInfoList, Set<String> baseColumns) {
        for (String column : baseColumns) {
            if (!containsWithIgnoreCase(columnInfoList, column)) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsWithIgnoreCase(List<ColumnInfo> columnInfoList, String columnName) {
        for (ColumnInfo columnInfo : columnInfoList) {
            if (columnInfo.getName().equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    private static List<ColumnInfo> evalbaseColumn(List<ColumnInfo> columnInfoList, Set<String> baseColumns) {
        List<ColumnInfo> toRemoveColums = new ArrayList();

        for (String columnName : baseColumns) {
            for (ColumnInfo columnInfo : columnInfoList) {
                if (columnInfo.getName().equalsIgnoreCase(columnName)) {
                    toRemoveColums.add(columnInfo);
                    break;
                }
            }
        }
        return toRemoveColums;
    }

    @SneakyThrows
    public static Set<String> getSubFileName(String url) {
        Enumeration<URL> possiblePathList = Thread.currentThread().getContextClassLoader().getResources(url);
        if (possiblePathList == null || !possiblePathList.hasMoreElements()) {
            return Collections.emptySet();//目录不存在
        }
        URL pathUrl = possiblePathList.nextElement();
        Set<String> subFileNameSet = new HashSet<>();
        if (CodegenConstant.FILE_PROTOCOL.equals(pathUrl.getProtocol())) {
            log.info(pathUrl.getPath());
            File file = new File(pathUrl.getPath());
            if (file.isDirectory()) {
                File[] subFileList = file.listFiles();
                for (File subFile : subFileList) {
                    if (!subFile.isDirectory()) {
                        String subNameFile = subFile.getName().substring(0, subFile.getName().indexOf("."));
                        log.info(subNameFile);
                        subFileNameSet.add(subNameFile);
                    }
                }
            }
        } else if (CodegenConstant.JAR_PROTOCOL.equals(pathUrl.getProtocol())) {
            log.info(pathUrl.getPath());
            String jarPath = pathUrl.toString().substring(0, pathUrl.toString().indexOf(CodegenConstant.JAR_FILE_SPLITER) + 2);

            URL jarURL = new URL(jarPath);
            JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
            JarFile jarFile = jarCon.getJarFile();
            Enumeration<JarEntry> jarEntrys = jarFile.entries();

            while (jarEntrys.hasMoreElements()) {
                JarEntry jarEntry = jarEntrys.nextElement();
                String innerPath = jarEntry.getName();
                if(innerPath.startsWith(url)){
                    String pathWithoutParent = innerPath.substring(url.length(), innerPath.length());
                    if (!pathWithoutParent.contains(CodegenConstant.SLASH) && pathWithoutParent.contains(CodegenConstant.DOT)) {
                        String templateFileName = pathWithoutParent.substring(0, pathWithoutParent.indexOf(CodegenConstant.DOT));
                        log.info(templateFileName);
                        subFileNameSet.add(templateFileName);
                    }
                }
            }
        } else {
            log.error("文件协议不支持>" + pathUrl.getProtocol());
        }
        return subFileNameSet;
    }
}
