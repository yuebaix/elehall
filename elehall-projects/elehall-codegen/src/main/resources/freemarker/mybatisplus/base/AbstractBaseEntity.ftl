${header}

package ${baseCodePackage};

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
* <p>Description : 基础实体</p>
* <p>Created on  : ${date}</p>
*
* @author ${author}
*/
public abstract class AbstractBaseEntity<T extends Model<?>> extends Model<T> {
}