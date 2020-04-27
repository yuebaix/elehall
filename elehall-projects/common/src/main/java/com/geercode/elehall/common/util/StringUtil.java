package com.geercode.elehall.common.util;

import cn.hutool.core.util.ReUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

public class StringUtil {

    private StringUtil() {}

    /**
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @author org.apache.commons.lang
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String underlineToCamel(String name) {
        // 快速检查
        if (isEmpty(name)) {
            // 没必要转换
            return "";
        }
        String tempName = name;
        // 大写数字下划线组成转为小写 , 允许混合模式转为小写
        if (isCapitalMode(name) || isMixedMode(name)) {
            tempName = name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String[] camels = tempName.split("_");
        // 跳过原始字符串中开头、结尾的下换线或双重下划线
        // 处理真正的驼峰片段
        Arrays.stream(camels).filter(camel -> !isEmpty(camel)).forEach(camel -> {
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel);
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        });
        return result.toString();
    }

    public static boolean isCapitalMode(String word) {
        return null != word && word.matches("^[0-9A-Z/_]+$");
    }

    public static boolean isMixedMode(String word) {
        return matches(".*[A-Z]+.*", word) && matches(".*[/_]+.*", word);
    }

    public static boolean matches(String regex, String input) {
        return null != regex && null != input ? Pattern.matches(regex, input) : false;
    }

    /**
     * <p>Description : 首字母大写</p>
     * <p>Created on  : 2019-10-08 17:52:56</p>
     *
     * @author jerryniu
     */
    public static String capitalFirst(String name) {
        if (!isEmpty(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return "";
    }

    /**
     * <p>Description : 全部替换为*</p>
     * <p>Created on  : 2019-10-18 19:07:19</p>
     *
     * @author jerryniu
     */
    public static String muteAll(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.replaceAll(".", "*");
    }

    /**
     * <p>Description : [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**></p>
     * <p>Created on  : 2019-10-18 18:51:46</p>
     *
     * @author jerryniu
     */
    public static String muteChineseName(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            return "";
        }
        final String name = StringUtils.left(chineseName, 1);
        return StringUtils.rightPad(name, StringUtils.length(chineseName), "*");
    }

    /**
     * <p>Description : [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762></p>
     * <p>Created on  : 2019-10-18 18:52:39</p>
     *
     * @author jerryniu
     */
    public static String muteIdNo(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        String num = StringUtils.right(id, 4);
        return StringUtils.leftPad(num, StringUtils.length(id), "*");
    }

    /**
     * <p>Description : [固定电话] 后四位，其他隐藏<例子：****1234></p>
     * <p>Created on  : 2019-10-18 18:54:06</p>
     *
     * @author jerryniu
     */
    public static String muteFixedPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone), "*");
    }

    /**
     * <p>Description : [手机号码] 前三位，后四位，其他隐藏<例子:138******1234></p>
     * <p>Created on  : 2019-10-18 18:54:47</p>
     *
     * @author jerryniu
     */
    public static String muteMobilePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        return StringUtils.left(phone, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone), "*"), "***"));
    }

    /**
     * <p>Description : [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****></p>
     * <p>Created on  : 2019-10-18 18:56:14</p>
     *
     * @author jerryniu
     */
    public static String muteAddr(String addr, int sensitiveSize) {
        if (StringUtils.isBlank(addr)) {
            return "";
        }
        int length = StringUtils.length(addr);
        return StringUtils.rightPad(StringUtils.left(addr, length - sensitiveSize), length, "*");
    }

    /**
     * <p>Description : [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com></p>
     * <p>Created on  : 2019-10-18 18:56:44</p>
     *
     * @author jerryniu
     */
    public static String muteEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1)
            return email;
        else
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }

    /**
     * <p>Description : [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234></p>
     * <p>Created on  : 2019-10-18 18:57:47</p>
     *
     * @author jerryniu
     */
    public static String muteBankCard(String bankCard) {
        if (StringUtils.isBlank(bankCard)) {
            return "";
        }
        return StringUtils.left(bankCard, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(bankCard, 4), StringUtils.length(bankCard), "*"), "******"));
    }

    /**
     * <p>Description : [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********></p>
     * <p>Created on  : 2019-10-18 18:58:12</p>
     *
     * @author jerryniu
     */
    public static String muteCnapsCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

    /**
     * <p>Description : 用身份证获取年龄</p>
     * <p>Created on  : 2019-11-15 11:14:20</p>
     *
     * @author jerryniu
     */
    public static int idno2Age(String id){
        Calendar ca =Calendar.getInstance();
        int nowYear= ca.get(Calendar.YEAR);
        int nowMonth= ca.get(Calendar.MONTH)+1;
        int len=id.length();
        if(len==18){
            int IDYear=Integer.parseInt(id.substring(6,10));
            int IDMonth=Integer.parseInt(id.substring(10,12));
            if((IDMonth-nowMonth)>0){
                return nowYear-IDYear-1;
            }else{
                return nowYear-IDYear;
            }
        }else{
            System.out.println("错误的身份证号");
            return 0;
        }
    }

    /**
     * 目前已知的号段
     * 移动号段: 134 135 136 137 138 139 147 148 150 151 152 157 158 159 165 172 178 182 183 184 187 188 198
     * 联通号段: 130 131 132 145 146 155 156 166 170 171 175 176 185 186
     * 电信号段: 133 149 153 170 173 174 177 180 181 189 191 199
     * 虚拟运营商: 170
     *
     */

    public static boolean isMobile(String mobile) {
        if (!isEmpty(mobile) && ReUtil.isMatch("(^$|^((13[0-9])|(14[5,6,7,9])|(15[^4])|(16[5,6])|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$)", mobile)) {
            return true;
        }
        return false;
    }
}
