package com.geercode.elehall.codegen.generator;

/**
 * <p>Description : 代码生成器</p>
 * <p>Created on  : 2019-09-29 10:09:43</p>
 *
 * @author jerryniu
 */
public interface OrmGenerator {
    /**
     * <p>description : 生成基础类</p>
     * <p>create   on : 2018-09-11 16:19:16</p>
     *
     * @author jerryniu
     */
    void genBase();

    /**
     * <p>description : 生成mapper.xml</p>
     * <p>create   on : 2018-09-11 16:19:16</p>
     *
     * @author jerryniu
     */
    void genXml();

    /**
     * <p>Description : 仅生成entity</p>
     * <p>Created on  : 2019-11-20 19:39:53</p>
     *
     * @author jerryniu
     */
    void genEntity();

    /**
     * <p>description : 生成dao</p>
     * <p>create   on : 2018-09-11 16:19:16</p>
     *
     * @author jerryniu
     */
    void genDao();

    /**
     * <p>description : 生成服务类</p>
     * <p>create   on : 2018-09-11 16:19:16</p>
     *
     * @author jerryniu
     */
    void genService();

    /**
     * <p>description : 生成Controller</p>
     * <p>create   on : 2018-09-11 16:19:16</p>
     *
     * @author jerryniu
     */
    void genWeb();

    /**
     * <p>description : 生成base文件、dao层(不包含xml)与service层</p>
     * <p>create   on : 2018-09-11 16:19:16</p>
     *
     * @author jerryniu
     */
    void genAll();
}
