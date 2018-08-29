package test.singleton;

/**
 * @Class: TestOfSingleton4
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */

/**
 * 单例模式-懒汉模式3
 * 针对前一节代码的实现，我们能够保证当前单例在
 * synchronized关键字情况下是能够实现单例同步的，
 * 但是同步粒度较粗，导致效率不高，如果我们将同步
 * 块synchronized放到实际创建对象的代码外，是否
 * 会引起其他问题，我们看看代码。
 */
public class TestOfSingleton4 {
    private static TestOfSingleton4 singleton4;

    private TestOfSingleton4() {
        System.out.println("TestOfSingleton4 constructor on thread: " +
                Thread.currentThread().getName());
    }

    public static TestOfSingleton4 getInstance() {
        if (singleton4 == null) {
            System.out.println("TestOfSingleton4 getInstance on thread: " +
                    Thread.currentThread().getName());
            try {
                Thread.sleep(1000l);
                synchronized (TestOfSingleton4.class) {
                    singleton4 = new TestOfSingleton4();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return singleton4;
    }

    public static void main(String[] args) {
        // 1. 使用多线程调用创建单例子，结果打印：
        // TestOfSingleton4 getInstance on thread: Thread-1
        // TestOfSingleton4 getInstance on thread: Thread-0
        // TestOfSingleton4 getInstance on thread: Thread-2
        // TestOfSingleton4 getInstance on thread: Thread-3
        // TestOfSingleton4 getInstance on thread: Thread-4
        // TestOfSingleton4 getInstance on thread: Thread-5
        // TestOfSingleton4 getInstance on thread: Thread-6
        // TestOfSingleton4 getInstance on thread: Thread-7
        // TestOfSingleton4 getInstance on thread: Thread-8
        // TestOfSingleton4 getInstance on thread: Thread-9
        // TestOfSingleton4 constructor on thread: Thread-0
        // TestOfSingleton4 constructor on thread: Thread-3
        // singleton obj: 1564989180, on thread: Thread-0
        // singleton obj: 943959215, on thread: Thread-3
        // TestOfSingleton4 constructor on thread: Thread-8
        // singleton obj: 1637022914, on thread: Thread-8
        // TestOfSingleton4 constructor on thread: Thread-2
        // singleton obj: 1441549009, on thread: Thread-2
        // TestOfSingleton4 constructor on thread: Thread-1
        // singleton obj: 1380272179, on thread: Thread-1
        // TestOfSingleton4 constructor on thread: Thread-9
        // singleton obj: 807862979, on thread: Thread-9
        // TestOfSingleton4 constructor on thread: Thread-6
        // singleton obj: 1956285374, on thread: Thread-6
        // TestOfSingleton4 constructor on thread: Thread-7
        // singleton obj: 1443567450, on thread: Thread-7
        // TestOfSingleton4 constructor on thread: Thread-5
        // TestOfSingleton4 constructor on thread: Thread-4
        // singleton obj: 60994059, on thread: Thread-5
        // singleton obj: 1705298371, on thread: Thread-4
        // 可以很明显的看到，创建了不止一个单例对象，因为我们将同步
        // 块给放到了创建对象的外面，而此时在此进行同步阻塞，会让
        // 其他线程得到锁之后走对象创建流程，会出现更多的对象。
        for (int i = 0; i < 10; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton4.getInstance().hashCode() + ", on thread: " +
                    Thread.currentThread().getName());
        }
    }
}
