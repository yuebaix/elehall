${header}

package ${package}.${entitySubPkg};

<#if table.extendBase>
import ${baseCodePackage}.AbstractBaseEntity;
import lombok.EqualsAndHashCode;
</#if>
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
<#if table.extendBase == false>
import java.io.Serializable;
</#if>
<#if importPackages??>
<#list importPackages as pkg>
import ${pkg};
</#list>
</#if>

/**
* <p>Description : ${table.comment}</p>
* <p>Created on  : ${date}</p>
*
* @author ${author}
*/
@Data
<#if table.extendBase>
@EqualsAndHashCode(callSuper = true)
</#if>
@Accessors(chain = true)
@TableName("${table.name}")
@ApiModel(value = "${entityName}", description = "${table.comment}")
<#if table.extendBase>
public class ${entityName} extends AbstractBaseEntity<${entityName}> {
<#else>
public class ${entityName} implements Serializable {
</#if>
<#if table.columnInfoList??>
    <#list table.columnInfoList as column>
    <#if column_index != 0>

    </#if>
    <#if column.comment != ''>
    /**
    * ${column.comment}
    */
    </#if>
    @ApiModelProperty(value = "${column.comment}")
    <#if column.key = 'PRI'>
    @TableId("${column.name}")
    <#else>
    @TableField("${column.name}")
    </#if>
    private ${column.type} ${column.nameCamelCase};
    </#list>
</#if>
}
