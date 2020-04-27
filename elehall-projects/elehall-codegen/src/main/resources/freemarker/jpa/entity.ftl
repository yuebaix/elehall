${header}

package ${package}.entity;

<#if table.extendBase>
import ${baseCodePackage}.BaseEntity;
</#if>
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
import javax.persistence.*;
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
<#if table.comment != ''>
</#if>
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Accessors(chain = true)
@Table(name = "${table.name}")
<#if table.extendBase>
public class ${entityName} extends BaseEntity implements Serializable {
<#else>
public class ${entityName} implements Serializable {
</#if>
<#if table.columnInfoList??>
    <#list table.columnInfoList as column>
    <#if column.comment != ''>

    /**
    * ${column.comment}
    */
    </#if>
    <#if column.key = 'PRI'>
    @Id
    <#if column.extra = 'auto_increment'>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    </#if>
    @Column(name = "${column.name}")
    private ${column.type} ${column.nameCamelCase};
    </#list>
</#if>
}