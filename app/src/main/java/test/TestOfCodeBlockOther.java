package test;

/**
 * @Class: TestOfCodeBlockOther
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

/**
 * 第三方main函数
 * 父类静态代码块
 * 0
 */
public class TestOfCodeBlockOther {
    public static void main(String[] args) {
        System.out.println("第三方main函数");
        // 如果是直接调用的父类的静态变量，是不会触发子类的静态代码块执行
        System.out.println(TestOfCodeBlockSub.a);
        System.out.println("==================");
        // 如果是类作为数组类型声明或者直接调用静态变量的时候，也不会触发静态代码块执行
        TestOfCodeBlock[] blocks = new TestOfCodeBlock[5];
        System.out.println(TestOfCodeBlock.a); // 0
    }
}
