package test.singleton;

/**
 * @Class: TestOfSingleton3
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */

/**
 * 单例模式-懒汉模式2
 * 我们使用synchronized关键字对getInstance方法进行同步。
 * 但是缺点就是效率太低，是同步运行的，下个线程想要取得对象，
 * 就必须要等上一个线程释放，才可以继续执行。
 */
public class TestOfSingleton3 {
    private static TestOfSingleton3 singleton3;

    private TestOfSingleton3() {
        System.out.println("TestOfSingleton3 constructor on thread: " +
                Thread.currentThread().getName());
    }

    private static synchronized TestOfSingleton3 getInstance() {
        if (singleton3 == null) {
            System.out.println("TestOfSingleton3 getInstance on thread: " +
                    Thread.currentThread().getName());
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            singleton3 = new TestOfSingleton3();
        }
        return singleton3;
    }

    public static void main(String[] args) {
        // 1. 创建多条线程调用单例模式，打印结果：
        // TestOfSingleton3 getInstance on thread: Thread-1
        // TestOfSingleton3 constructor on thread: Thread-1
        // singleton obj: 1564989180, on thread: Thread-1
        // singleton obj: 1564989180, on thread: Thread-8
        // singleton obj: 1564989180, on thread: Thread-9
        // singleton obj: 1564989180, on thread: Thread-6
        // singleton obj: 1564989180, on thread: Thread-3
        // singleton obj: 1564989180, on thread: Thread-7
        // singleton obj: 1564989180, on thread: Thread-0
        // singleton obj: 1564989180, on thread: Thread-2
        // singleton obj: 1564989180, on thread: Thread-4
        // singleton obj: 1564989180, on thread: Thread-5
        // 通过上述结果可以得知，使用synchronized关键字修饰的
        // 静态方法，能够保证当前单例对象的创建过程是线程安全的。
        for (int i = 0; i < 10; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton3.getInstance().hashCode() + ", on thread: " +
                    Thread.currentThread().getName());
        }
    }
}
