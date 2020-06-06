${header}

package ${package}.service.impl;

import ${baseCodePackage}.BaseServiceImpl;
import ${package}.entity.${entityName};
import ${package}.service.${serviceName};
import org.springframework.stereotype.Service;

/**
* <p>Description : ${table.comment}</p>
* <p>Created on  : ${date}</p>
*
* @author ${author}
*/
@Service("${package}.service.impl.${serviceImplName}")
public class ${serviceImplName} extends BaseServiceImpl<${entityName}, ${pkTypeClass}> implements ${serviceName} {
}