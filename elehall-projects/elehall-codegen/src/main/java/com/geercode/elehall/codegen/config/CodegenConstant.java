package com.geercode.elehall.codegen.config;

import java.nio.charset.Charset;

/**
 * <p>Description : 代码生成器常量</p>
 * <p>Created on  : 2019-09-28 18:09:18</p>
 *
 * @author jerryniu
 */
public class CodegenConstant {
    private CodegenConstant() {}
    public static final String ENGINE_FREEMARKER = "freemarker";

    public static final String BUILDER_TYPE_ORM = "orm";

    public static final String CODE_TYPE_JPA = "JPA";
    public static final String CODE_TYPE_MYBATIS = "mybatis";
    public static final String CODE_TYPE_MYBATISPLUS = "mybatisplus";

    public static final String DEFAULT_VERSION = "1.0.0";

    public static final String APACHE2_LICENCE_HEADER =  "/*\n"
            + " * Copyright 2018-2050 the original author or authors.\n"
            + " *\n"
            + " * Licensed under the Apache License, Version 2.0 (the \"License\");\n"
            + " * you may not use this file except in compliance with the License.\n"
            + " * You may obtain a copy of the License at\n"
            + " *\n"
            + " *      http://www.apache.org/licenses/LICENSE-2.0\n"
            + " *\n"
            + " * Unless required by applicable law or agreed to in writing, software\n"
            + " * distributed under the License is distributed on an \"AS IS\" BASIS,\n"
            + " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
            + " * See the License for the specific language governing permissions and\n"
            + " * limitations under the License.\n"
            + " */";

    public static final String DB_TPYE_MYSQL = "mysql";
    public static final String DB_DRIVER_MYSQL = "com.mysql.jdbc.Driver";

    public static final String UTF8 = Charset.forName("UTF-8").name();
    public static final String SLASH = "/";
    public static final String DOT = ".";
    public static final String COMMA = ",";

    public static final String PK_SIGN_MYSQL = "PRI";
}
