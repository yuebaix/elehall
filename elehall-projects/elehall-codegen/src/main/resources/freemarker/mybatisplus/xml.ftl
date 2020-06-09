<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${daoSubPkg}.${daoName}">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package}.${entitySubPkg}.${entityName}">
        <#list table.columnInfoList as column><#--生成普通字段-->
            <result column="${column.name}" property="${column.nameCamelCase}" />
        </#list>
        <#if table.extendBase>
            <#list table.baseColumnList as column><#--生成公共字段-->
                <result column="${column.name}" property="${column.nameCamelCase}" />
            </#list>
        </#if>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        <#list table.columnInfoList as column><#if column_index != 0>, </#if>${column.name}</#list>
        <#if table.extendBase>
            <#list table.baseColumnList as column>,${column.name}</#list>
        </#if>
    </sql>

</mapper>
