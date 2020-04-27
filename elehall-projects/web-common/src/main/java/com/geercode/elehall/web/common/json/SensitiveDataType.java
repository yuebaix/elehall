package com.geercode.elehall.web.common.json;

/**
 * <p>Description : 敏感数据类型枚举类</p>
 * <p>Created on  : 2019-10-18 18:10:26</p>
 *
 * @author jerryniu
 */
public enum SensitiveDataType {
    /**
     * 全部脱敏
     */
    ALL,
    /**
     * 中文名
     */
    CHINESE_NAME,

    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 座机号
     */
    FIXED_PHONE,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     * 公司开户银行联号
     */
    CNAPS_CODE
}
