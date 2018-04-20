/**
 * Copyright 2018 hubohua
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demoncat.dcapp.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Class: RegularUtils
 * @Description: Regular expression utils
 * @Author: hubohua
 * @CreateDate: 2018/4/20
 */
public class RegularUtils {
    // vin码正则表达式验证，最简单的验证(17位字母和数字的组合)
    public static final String VIN_SIMPLE_REGEX = "^[a-zA-Z0-9]{17}$";
    // engine码正则表达式验证，最简单的验证(16位字母和数字的组合)
    public static final String ENGINE_SIMPLE_REGEX = "^[a-zA-Z0-9]{8}$";
    // 车牌号正则表达式验证
    public static final String PLATE_REGEX = "^[\u4E00-\u9FFF]{1}+[A-Z0-9]{6}$";
    // 邮箱正则表达式
    public static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    // 手机号正则表达式
    public static final String MOBILE_REGEX = "^[1][34578]\\d{9}$";
    // Pin码正则表达式
    public static final String PIN_REGUX = "^[0-9]{6}$";
    public static final String PIN_REPEAT_REGUX = "^(?=\\d+)(?!([\\d])\\1{5})[\\d]{6}$";
    // Password正则表达式
    public static final String PASSWORD_REGEX = "^(?![a-zA-Z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,14}$";
    // 非英文数字中文正则表达式
    public static final String COMMON_LIMIT_REGEX = "[^a-zA-Z0-9\\u4E00-\\u9FA5]";
    //用户名允许输入字符正则
    public static final String UNAME_ALLOWED_REGEX = "^[\\u4E00-\\u9FA5A-Za-z0-9\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E]+$";
    public static final String NUMBER_REGEX = "^[0-9]+$";

    /**
     * vin码正则表达式验证，最简单的验证(17位字母和数字的组合)
     *
     * @param vin vin码
     * @return true if matched,else false
     */
    public static Boolean vinRegular(String vin) {
        return regexMatch(VIN_SIMPLE_REGEX, vin);
    }

    /**
     * engine码正则表达式验证，最简单的验证(16位字母和数字的组合)
     *
     * @param engine engine码
     * @return true if matched,else false
     */
    public static Boolean engineRegular(String engine) {
        return regexMatch(ENGINE_SIMPLE_REGEX, engine);
    }

    /**
     * 车牌号正则表达式验证
     *
     * @param plate 车牌号
     * @return true if matched,else false
     */
    public static Boolean plateRegular(String plate) {
        return regexMatch(PLATE_REGEX, plate);
    }

    /**
     * Email is regular format
     *
     * @param email
     * @return
     */
    public static Boolean emailRegular(String email) {
        return regexMatch(EMAIL_REGEX, email);
    }

    /**
     * Password regular format
     *
     * @param password
     * @return
     */
    public static Boolean passwordRegular(String password) {
        return regexMatch(PASSWORD_REGEX, password);
    }

    /**
     * Password regular format
     *
     * @param username
     * @return
     */
    public static Boolean usernameRegular(String username) {
        return regexMatch(UNAME_ALLOWED_REGEX, username);
    }

    /**
     * Telephone is regular format
     *
     * @param telephone
     * @return
     */
    public static Boolean mobileRegular(String telephone) {
        return regexMatch(MOBILE_REGEX, telephone);
    }

    /**
     * Input is regular format
     *
     * @param numStr
     * @return
     */
    public static Boolean numberRegular(String numStr) {
        return regexMatch(NUMBER_REGEX, numStr);
    }

    /**
     * Validate pin code
     *
     * @param pinCode
     * @return
     */
    public static Boolean pinCodeRegular(String pinCode) {
        if (TextUtils.isEmpty(pinCode)) {
            return false;
        } else if (pinCode.length() != 6) {
            return false;
        } else {
            boolean sequence = true;
            char[] chars = pinCode.toCharArray();
            int gap = 0;
            for (int i = chars.length - 1; i > 0; i--) {
                if (i == chars.length - 1) {
                    // first time init gap
                    gap = chars[i] - chars[i - 1];
                } else {
                    // other time
                    if (gap != (chars[i] - chars[i - 1])) {
                        sequence = false;
                        break;
                    }
                }
            }
            return regexMatch(PIN_REGUX, pinCode)
                    && regexMatch(PIN_REPEAT_REGUX, pinCode)
                    && !sequence;
        }
    }

    /**
     * 把不符合要求的剔除
     *
     * @param regex
     * @param param
     * @return
     */
    public static String stringFilter(String regex, String param) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(param);
        return m.replaceAll("").trim();
    }

    /**
     * 正则表达式验证
     *
     * @param regex 正则表达式
     * @param param 待匹配的字符串
     * @return true if matched,else false
     */
    public static Boolean regexMatch(String regex, String param) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(param).matches();
    }

    /**
     * 模糊手机号，将中间4位变成*号
     *
     * @param telephone 手机号
     * @return 模糊后的手机号
     */
    public static String blurryTelephone(String telephone) {
        String BLURRY_TELEPHONE = "(\\d{3})\\d{4}(\\d{4})";
        String REGULAR_TELEPHONE = "$1****$2";
        return telephone.replaceAll(BLURRY_TELEPHONE, REGULAR_TELEPHONE);
    }

    /**
     * Has Chinese text
     *
     * @param contents
     * @return
     */
    public static boolean hasChinese(String contents) {
        boolean res = false;
        char[] cTemp = contents.toCharArray();
        for (int i = 0; i < contents.length(); i++) {
            if (isChinese(cTemp[i])) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * Char is chinese char
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }
}
