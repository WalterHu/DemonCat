package com.demoncat.dcapp;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @Class: CustomRule
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */

/**
 * 我们在@Before与@After注解的方法中加入”测试开始”的提示信息.
 * 假如我们一直需要这样的提示，那是不是需要每次在测试类中去实现它。
 * 这样就会比较麻烦。这时你就可以使用@Rule来解决这个问题，它甚至比@Before与@After还要强大。
 * 自定义@Rule很简单，就是实现TestRule 接口，实现apply方法。
 */
public class CustomRule implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                // evaluate前执行方法相当于@Before
                String methodName = description.getMethodName(); // 获取测试方法的名字
                System.out.println(methodName + "测试开始！");

                base.evaluate();  // 运行的测试方法

                // evaluate后执行方法相当于@After
                System.out.println(methodName + "测试结束！");
            }
        };
    }
}
