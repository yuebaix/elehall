package com.geercode.elehall.codegen.facade;

import com.geercode.elehall.codegen.generator.OrmGenerator;
import com.geercode.elehall.codegen.generator.OrmGeneratorImpl;

/**
 * <p>Description : orm生成</p>
 * <p>Created on  : 2019-09-29 10:09:28</p>
 *
 * @author jerryniu
 */
public interface Codegen {
    /**
     * <p>description : MybatisPlus Generator获取(多模块)</p>
     * <p>create   on : 2018-09-11 16:18:22</p>
     *
     * @author jerryniu
     * @version 1.0.0
     *
     * @return Mpg CreedMybatisPlusGenerator
     */
    static OrmGenerator orm() {
        return OrmGeneratorImpl.getStandaloneModuleHolder();
    }
}
