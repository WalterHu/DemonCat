package test.singleton;

/**
 * @Class: TestOfSingleton6
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */
public class TestOfSingleton6 {
    private static TestOfSingleton6 singleton6;

    static {
        singleton6 = new TestOfSingleton6();
        System.out.println("TestOfSingleton6 static block on thread: " +
                Thread.currentThread().getName());
    }

    public static TestOfSingleton6 getInstance() {
        System.out.println("TestOfSingleton6 getInstance on thread: " +
                Thread.currentThread().getName());
        return singleton6;
    }

    private TestOfSingleton6() {
        System.out.println("TestOfSingleton6 constructor on thread: " +
                Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        // 多线程调用，结果打印：
        // TestOfSingleton6 constructor on thread: main
        // TestOfSingleton6 static block on thread: main
        // TestOfSingleton6 getInstance on thread: Thread-0
        // TestOfSingleton6 getInstance on thread: Thread-1
        // singleton obj: 1637022914, on thread: Thread-0
        // singleton obj: 1637022914, on thread: Thread-1
        // TestOfSingleton6 getInstance on thread: Thread-2
        // singleton obj: 1637022914, on thread: Thread-2
        // TestOfSingleton6 getInstance on thread: Thread-3
        // singleton obj: 1637022914, on thread: Thread-3
        // TestOfSingleton6 getInstance on thread: Thread-4
        // singleton obj: 1637022914, on thread: Thread-4
        // TestOfSingleton6 getInstance on thread: Thread-5
        // singleton obj: 1637022914, on thread: Thread-5
        // TestOfSingleton6 getInstance on thread: Thread-6
        // singleton obj: 1637022914, on thread: Thread-6
        // TestOfSingleton6 getInstance on thread: Thread-7
        // singleton obj: 1637022914, on thread: Thread-7
        // TestOfSingleton6 getInstance on thread: Thread-8
        // singleton obj: 1637022914, on thread: Thread-8
        // TestOfSingleton6 getInstance on thread: Thread-9
        // singleton obj: 1637022914, on thread: Thread-9
        // 这里用的也是一种饿汉模式，使用的是静态代码块来完成类
        // 初始化，JVM层保证了线程安全的单例对象创建。
        for (int i = 0; i < 10; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton6.getInstance().hashCode() + ", on thread: " +
                    Thread.currentThread().getName());
        }
    }
}
