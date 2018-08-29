package test.singleton;

/**
 * @Class: TestOfSingleton5
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */

/**
 * 单例模式-懒汉模式4
 * 经过改良的模式，我们在同步代码块里面再一次做一下null判断。
 * 这种方式就是我们的DCL双重检查锁机制。
 */
public class TestOfSingleton5 {
    // 需要避免JVM指令冲排序而引起的问题
    private volatile static TestOfSingleton5 singleton5;

    private TestOfSingleton5() {
        System.out.println("TestOfSingleton5 constructor on thread: " +
                Thread.currentThread().getName());
    }

    public static TestOfSingleton5 getInstance() {
        if (singleton5 == null) {
            System.out.println("TestOfSingleton5 getInstance on thread: " +
                    Thread.currentThread().getName());
            try {
                Thread.sleep(1000l);
                synchronized (TestOfSingleton5.class) {
                    if (singleton5 == null) {
                        singleton5 = new TestOfSingleton5();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return singleton5;
    }

    public static void main(String[] args) {
        // 使用多线程访问，结果打印：
        // TestOfSingleton5 getInstance on thread: Thread-1
        // TestOfSingleton5 getInstance on thread: Thread-0
        // TestOfSingleton5 getInstance on thread: Thread-2
        // TestOfSingleton5 getInstance on thread: Thread-3
        // TestOfSingleton5 getInstance on thread: Thread-4
        // TestOfSingleton5 getInstance on thread: Thread-5
        // TestOfSingleton5 getInstance on thread: Thread-6
        // TestOfSingleton5 getInstance on thread: Thread-7
        // TestOfSingleton5 getInstance on thread: Thread-8
        // TestOfSingleton5 getInstance on thread: Thread-9
        // TestOfSingleton5 constructor on thread: Thread-1
        // singleton obj: 1564989180, on thread: Thread-1
        // singleton obj: 1564989180, on thread: Thread-2
        // singleton obj: 1564989180, on thread: Thread-3
        // singleton obj: 1564989180, on thread: Thread-4
        // singleton obj: 1564989180, on thread: Thread-0
        // singleton obj: 1564989180, on thread: Thread-6
        // singleton obj: 1564989180, on thread: Thread-7
        // singleton obj: 1564989180, on thread: Thread-5
        // singleton obj: 1564989180, on thread: Thread-9
        // singleton obj: 1564989180, on thread: Thread-8
        // 通过结果可知，已经基本保证了当前对象的创建是单例的。但
        // 是我们需要防止指令重排序，由于Java编译器允许处理器乱序执行（out-of-order），
        // 以及JDK1.5之前JMM（Java Memory Medel）中Cache、寄存器到主内存回写顺序的规定，
        // 创建完毕单例对象内存空间后，初始化构造器、引用指向单例对象这两步，可能会遇到指令
        // 重复排序，导致构造器与引用指向顺序调换，而其他线程调用时已经不为空，
        // 但是此时引用指向的内存地址是空的，即对象还未实际创建，直接会出错。
        for (int i = 0; i < 10; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton5.getInstance().hashCode() + ", on thread: " +
                    Thread.currentThread().getName());
        }
    }
}
