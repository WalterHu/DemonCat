package test.singleton;

/**
 * @Class: TestOfSingleton1
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */

/**
 * 单例模式-饿汉模式
 * 所谓饿汉模式就是立即加载，一般情况下再调用getInstancef方法
 * 之前就已经产生了实例，也就是在类加载的时候已经产生了。
 */
public class TestOfSingleton1 {
    private static TestOfSingleton1 singleton1 = new TestOfSingleton1(); // 直接初始化

    private TestOfSingleton1() {
        System.out.println("TestOfSingleton1 constructor on thread: " +
                Thread.currentThread().getName());
    }

    public static TestOfSingleton1 getInstance() {
        System.out.println("TestOfSingleton1 getInstance on thread: " +
                Thread.currentThread().getName());
        return singleton1;
    }

    public static void main(String[] args) {
        // 1. 直接运行，结果打印：
        // TestOfSingleton1 constructor on thread: main
        // 因为此时运行了main方法，所以会加载类TestOfSingleton1，
        // 而在类的加载-连接-初始化过程即创建了singleton1静态成员，
        // 而直接调用了TestOfSingleton1的构造函数创建了单例对象。

        // 2. 多线程运行, 结果打印:
        // TestOfSingleton1 constructor on thread: main
        // TestOfSingleton1 getInstance on thread: Thread-0
        // TestOfSingleton1 getInstance on thread: Thread-1
        // singleton obj: 1810344957
        // singleton obj: 1810344957
        // TestOfSingleton1 getInstance on thread: Thread-2
        // singleton obj: 1810344957
        // TestOfSingleton1 getInstance on thread: Thread-3
        // singleton obj: 1810344957
        // TestOfSingleton1 getInstance on thread: Thread-4
        // singleton obj: 1810344957
        // 通过结果得知，当前对象仅创建了一份，其通过JVM基于类加载
        // 的机制来完成对于单例模式的实现
        for (int i = 0; i < 5; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton1.getInstance().hashCode());
        }
    }
}
