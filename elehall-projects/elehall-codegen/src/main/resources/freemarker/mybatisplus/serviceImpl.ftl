${header}

package ${package}.${serviceImplSubPkg};

import ${baseCodePackage}.AbstractBaseService;
import ${package}.${entitySubPkg}.${entityName};
import ${package}.${daoSubPkg}.${daoName};
import ${package}.${serviceSubPkg}.${serviceName};
import org.springframework.stereotype.Service;

/**
* <p>Description : ${table.comment}</p>
* <p>Created on  : ${date}</p>
*
* @author ${author}
*/
@Service
public class ${serviceImplName} extends AbstractBaseService<${daoName}, ${entityName}>
        implements ${serviceName} {
}
