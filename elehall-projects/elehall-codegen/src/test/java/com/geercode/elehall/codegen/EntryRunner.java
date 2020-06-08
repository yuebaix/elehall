package com.geercode.elehall.codegen;

import com.geercode.elehall.codegen.common.ElehallCodegenException;
import com.geercode.elehall.codegen.config.CodegenConstant;
import com.geercode.elehall.codegen.config.orm.DbCfg;
import com.geercode.elehall.codegen.config.orm.OrmConfig;
import com.geercode.elehall.codegen.config.orm.OrmConfigBuilder;
import com.geercode.elehall.codegen.config.orm.query.AbstractDbInfoQuery;
import com.geercode.elehall.codegen.config.orm.query.MysqlQuery;
import com.geercode.elehall.codegen.config.orm.vo.ColumnInfo;
import com.geercode.elehall.codegen.config.orm.vo.TableInfo;
import com.geercode.elehall.codegen.engine.TemplateEngineStepBuilder;
import com.geercode.elehall.codegen.facade.Codegen;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>Description : 测试入口</p>
 * <p>Created on  : 2019-09-28 17:09:45</p>
 *
 * @author jerryniu
 */
@Slf4j
public class EntryRunner {
    /**
     * <p>Description : 生成代码入口！！！</p>
     * <p>Created on  : 2019-11-11 18:43:43</p>
     *
     * @author jerryniu
     */
    @Test
    public void fromFacade() {
        //Codegen.orm().genAll();
        Codegen.orm().genEntity();
    }

    @Test
    public void fromEngine() {
        OrmConfig ormConfig = OrmConfigBuilder.build();
        TemplateEngineStepBuilder.builder(CodegenConstant.BUILDER_TYPE_ORM).init(ormConfig).mkdirs().batchOutput().open();
    }

    @Test
    @SneakyThrows
    public void fromGenCfg() {
        OrmConfig ormConfig = OrmConfigBuilder.build();
        String prettyCfg = JacksonUtil.getObjectMapperHolder().writerWithDefaultPrettyPrinter().writeValueAsString(ormConfig);
        System.out.println(prettyCfg);
    }

    @Test
    public void fromDatabase() {
        DbCfg dbCfg = new DbCfg().setUrl("jdbc:mysql://192.168.1.11:13306/tardis?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai")
                .setUsername("root")
                .setPassword("WeAreSuperman");
        Set<String> includeTables = new HashSet();
        Set<String> baseColumns = new HashSet();
        baseColumns.add("CREATOR");
        baseColumns.add("CREATE_TIME");
        baseColumns.add("UPDATER");
        baseColumns.add("UPDATE_TIME");

        List<TableInfo> tableInfoList = new ArrayList();

        Connection connection = getConnection(dbCfg);
        AbstractDbInfoQuery query = getQuery(dbCfg);

        String tableInfoQuery = query.getTableQuery();

        PreparedStatement preparedStatement = null;
        //todo
        try {
            preparedStatement = connection.prepareStatement(tableInfoQuery);
            ResultSet tableResults = preparedStatement.executeQuery();

            List<ColumnInfo> columnInfoList = new ArrayList();
            while (tableResults.next()) {
                String tableName = tableResults.getString("NAME");
                String tableComment = tableResults.getString("COMMENT");
                TableInfo tableInfo = new TableInfo();
                tableInfo.setName(tableName);
                tableInfo.setComment(tableComment);
                //如果包含该表则继续查询字段信息
                if (includeTables.isEmpty() || includeTables.contains(tableName)) {
                    String columnInfoQuery = String.format(query.getColumnQuery(), tableName);
                    preparedStatement = connection.prepareStatement(columnInfoQuery);
                    ResultSet columnResults = preparedStatement.executeQuery();
                    while (columnResults.next()) {
                        String columnName = columnResults.getString("FIELD");
                        String columnType = columnResults.getString("TYPE");
                        String columnComment = columnResults.getString("COMMENT");
                        String columnKey = columnResults.getString("KEY");
                        String columnExtra = columnResults.getString("EXTRA");

                        ColumnInfo columnInfo = new ColumnInfo();
                        columnInfo.setName(columnName);
                        columnInfo.setType(columnType);
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

    @Test
    public void testJarEntry() {
        files();
    }

    @SneakyThrows
    private void files() {
        String url = "freemarker/jpa/";
        Enumeration<URL> possiblePathList = Thread.currentThread().getContextClassLoader().getResources(url);
        if (possiblePathList == null || !possiblePathList.hasMoreElements()) {
            return;//目录不存在
        }
        while (possiblePathList.hasMoreElements()) {
            URL pathUrl = possiblePathList.nextElement();
            if ("file".equals(pathUrl.getProtocol())) {
                System.out.println(pathUrl);
                File file = new File(pathUrl.getPath());
                if (file.isDirectory()) {
                    File[] subFileList = file.listFiles();
                    for (File subFile : subFileList) {
                        if (!subFile.isDirectory()) {
                            String subNameFile = subFile.getName();
                            System.out.println(subNameFile.substring(0, subNameFile.indexOf(".")));
                        }
                    }
                }
            } else if ("jar".equals(pathUrl.getProtocol())) {
                System.out.println(pathUrl);
                String jarPath = pathUrl.toString().substring(0, pathUrl.toString().indexOf("!/") + 2);

                URL jarURL = new URL(jarPath);
                JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
                JarFile jarFile = jarCon.getJarFile();
                Enumeration<JarEntry> jarEntrys = jarFile.entries();

                while (jarEntrys.hasMoreElements()) {
                    JarEntry jarEntry = jarEntrys.nextElement();
                    String innerPath = jarEntry.getName();
                    if(innerPath.startsWith(url)){
                        String pathWithoutParent = innerPath.substring(url.length(), innerPath.length());
//                        System.out.println(innerPath);
//                        System.out.println(pathWithoutParent);
                        if (!pathWithoutParent.contains("/") && pathWithoutParent.contains(".")) {
                            String templateFileName = pathWithoutParent.substring(0, pathWithoutParent.indexOf("."));
                            System.out.println(templateFileName);
                        }
                    }
                }
            }
        }
        /*URL pathUrl = possiblePathList.nextElement();
        if ("file".equals(pathUrl.getProtocol())) {
            System.out.println(pathUrl.getPath());
        } else if ("jar".equals(pathUrl.getProtocol())) {
            System.out.println(pathUrl.getPath());
        } else {
            return;//文件协议不支持
        }*/
    }
}