package com.demoncat.dcapp;

import com.demoncat.dcapp.utils.DateTimeUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @Class: ExampleUnitCollectionTest
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */

/**
 * 每次测试一个方法都要去设置对应的值，
 * 就不能连续用不不同的值去测试一个方法，省的我们不断地修改。
 * 这时就用到了@RunWith与@Parameters。
 * 1.首先在测试类上添加注解@RunWith(Parameterized.class)
 * 2.在创建一个由 @Parameters 注解的public static方法，让返回一个对应的测试数据集合。
 * 3.最后创建构造方法，方法的参数顺序和类型与测试数据集合一一对应。
 */
@RunWith(Parameterized.class)
public class ExampleUnitCollectionTest {
    // 使用自定义Rules, 必须为public
    @Rule
    public CustomRule mCustomRule = new CustomRule();

    private String time;

    public ExampleUnitCollectionTest(String time) {
        this.time = time;
    }

//    @Before
//    public void testStart() {
//        System.out.println("测试开始");
//    }

//    @After
//    public void testEnd() {
//        System.out.println("测试结束");
//    }

    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new String[] {
                "2018-08-14",
                "2018年8月14日",
                "14/08/2018"
        });
    }

    @Test
    public void testForDateformat() throws Exception {
//        String date = DateTimeUtils.getDateTime(DateTimeUtils.DATE_DAY_FORMAT);
        // assertEquals(date, time); // 判断相等
        // 使用AssertThat断言方法
        // assertThat(String reason, T actual, Matcher<? super T> matcher)
        // 分别表明当前断言失败的原因（自定义），避免仅仅提示"AssertError"
        // 实际的断言参数值，以及断言assertThant匹配器matcher
        // 注意：此时使用assertThat中的匹配器，需要引入hamcrest-core包（更多的需要使用hamcrest-all包）
        // 匹配器并非包含在junit包内
//        assertThat("Not correct formated", date, equalTo(time));
        // 测试自定义匹配器
        assertThat("不是手机号码!!!", "18934565487", new CustomPhoneMatcher());
    }
}
