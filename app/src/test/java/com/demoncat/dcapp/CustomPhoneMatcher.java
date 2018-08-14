package com.demoncat.dcapp;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Class: CustomPhoneMatcher
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */

/**
 * 自定义断言assertThat匹配器。
 * 只需要继承BaseMatcher抽象类，实现matches与describeTo方法。
 */
public class CustomPhoneMatcher extends BaseMatcher<String> {

    /**
     * 进行断言判定，返回true则断言成功，否则断言失败
     */
    @Override
    public boolean matches(Object item) {
        if (item == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("(1|861)(3|5|7|8)\\d{9}$*");
        Matcher matcher = pattern.matcher((String) item);
        return matcher.find();
    }

    /**
     * 给期待断言成功的对象增加描述
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("预计此字符串是手机号码！");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText(item.toString() + "不是手机号码！");
    }
}
