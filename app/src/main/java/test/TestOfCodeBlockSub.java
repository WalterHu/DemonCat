package test;

/**
 * @Class: TestOfCodeBlockSub
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

/**
 * 父类静态代码块
 * 子类静态代码块
 * 子类main函数
 * 父类非静态代码块
 * 父类构造器函数
 * 子类非静态代码块
 * 子类构造器函数
 */
public class TestOfCodeBlockSub extends TestOfCodeBlock {
    static {
        System.out.println("子类静态代码块");
    }

    public TestOfCodeBlockSub() {
        System.out.println("子类构造器函数");
    }

    {
        System.out.println("子类非静态代码块");
    }

    public static void main(String[] args) {
        System.out.println("子类main函数");
        TestOfCodeBlockSub sub = new TestOfCodeBlockSub();
    }
}
