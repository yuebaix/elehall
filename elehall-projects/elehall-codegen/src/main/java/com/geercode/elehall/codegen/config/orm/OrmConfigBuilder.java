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
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.*;
import java.util.*;
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
        optCfg.setEngineType(engineType).setCodeType(codeType).setOverride(override).setIncludeTables(includeTables).setBaseColumns(baseColumns).setBaseCodePackage(baseCodePackage);

        //OutputCfg
        if (CodegenConstant.CODE_TYPE_JPA.equalsIgnoreCase(codeType)) {
            String basicCodePath = pkgCfg.getRootArtifactDir()
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
            OutputCfg entityOutputCfg = new OutputCfg().setTemplatePath("/freemarker/jpa/entity")
                    .setOutputPath(basicCodePath + File.separator + "entity")
                    .setOutputNamePattern(optCfg.getEntityPattern());
            OutputCfg daoOutputCfg = new OutputCfg().setTemplatePath("/freemarker/jpa/dao")
                    .setOutputPath(basicCodePath + File.separator + "dao")
                    .setOutputNamePattern(optCfg.getDaoPattern());
            OutputCfg serviceOutputCfg = new OutputCfg().setTemplatePath("/freemarker/jpa/service")
                    .setOutputPath(basicCodePath + File.separator + "service")
                    .setOutputNamePattern(optCfg.getServicePattern());
            OutputCfg serviceImplOutputCfg = new OutputCfg().setTemplatePath("/freemarker/jpa/serviceImpl")
                    .setOutputPath(basicCodePath + File.separator + "service" + File.separator + "impl")
                    .setOutputNamePattern(optCfg.getServiceImplPattern());
            outputCfgList.add(entityOutputCfg);
            outputCfgList.add(daoOutputCfg);
            outputCfgList.add(serviceOutputCfg);
            outputCfgList.add(serviceImplOutputCfg);
        } else if (CodegenConstant.CODE_TYPE_MYBATISPLUS.equalsIgnoreCase(codeType)) {
            String basicCodePath = pkgCfg.getRootArtifactDir()
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + pkgCfg.getBasePackage().replace(CodegenConstant.DOT, File.separator);
            OutputCfg entityOutputCfg = new OutputCfg().setTemplatePath("/freemarker/mybatisplus/entity")
                    .setOutputPath(basicCodePath + File.separator + "entity")
                    .setOutputNamePattern(optCfg.getEntityPattern());
            OutputCfg daoOutputCfg = new OutputCfg().setTemplatePath("/freemarker/mybatisplus/dao")
                    .setOutputPath(basicCodePath + File.separator + "dao")
                    .setOutputNamePattern(optCfg.getDaoPattern());
            OutputCfg serviceOutputCfg = new OutputCfg().setTemplatePath("/freemarker/mybatisplus/service")
                    .setOutputPath(basicCodePath + File.separator + "service")
                    .setOutputNamePattern(optCfg.getServicePattern());
            OutputCfg serviceImplOutputCfg = new OutputCfg().setTemplatePath("/freemarker/mybatisplus/serviceImpl")
                    .setOutputPath(basicCodePath + File.separator + "service" + File.separator + "impl")
                    .setOutputNamePattern(optCfg.getServiceImplPattern());
            outputCfgList.add(entityOutputCfg);
            outputCfgList.add(daoOutputCfg);
            outputCfgList.add(serviceOutputCfg);
            outputCfgList.add(serviceImplOutputCfg);
        } else {
            throw new ElehallCodegenException("设定的生成代码类型没有得到支持: " + codeType);
        }
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
                        removebaseColumn(columnInfoList, baseColumns);
                        tableInfo.setExtendBase(true);
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

    private static List<ColumnInfo> removebaseColumn(List<ColumnInfo> columnInfoList, Set<String> baseColumns) {
        List<ColumnInfo> toRemoveColums = new ArrayList();

        for (String columnName : baseColumns) {
            for (ColumnInfo columnInfo : columnInfoList) {
                if (columnInfo.getName().equalsIgnoreCase(columnName)) {
                    toRemoveColums.add(columnInfo);
                    break;
                }
            }
        }
        columnInfoList.removeAll(toRemoveColums);
        return columnInfoList;
    }
}
