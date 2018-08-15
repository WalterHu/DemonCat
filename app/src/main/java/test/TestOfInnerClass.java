package test;

/**
 * @Class: TestOfInnerClass
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

/**
 * 局部内部类
 */
public class TestOfInnerClass {
    // 内部类
    public class InnerA {
    }

    // 静态内部类
    public static class InnerB {
    }

    // 匿名内部类
    Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
        }
    };

    // 局部内部类，仅作用于method范围
    public void function() {
        class InnerC {

        }
    }
}
