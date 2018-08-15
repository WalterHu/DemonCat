package test;

/**
 * @Class: TestOfCodeBlock
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */
public class TestOfCodeBlock {
    public static int a;

    static {
        System.out.println("父类静态代码块");
    }

    public TestOfCodeBlock() {
        System.out.println("父类构造器函数");
    }

    {
        System.out.println("父类非静态代码块");
    }
}
