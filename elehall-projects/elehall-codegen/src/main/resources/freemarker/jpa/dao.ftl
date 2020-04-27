${header}

package ${package}.dao;

import ${baseCodePackage}.BaseDao;
import ${package}.entity.${entityName};
import org.springframework.stereotype.Repository;

/**
* <p>Description : ${table.comment}</p>
* <p>Created on  : ${date}</p>
*
* @author ${author}
*/
@Repository("${package}.dao.${daoName}")
public interface ${daoName} extends BaseDao<${entityName}, ${pkTypeClass}> {
}