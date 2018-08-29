package test.singleton;

/**
 * @Class: TestOfSingleton2
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */

/**
 * 单例模式-懒汉模式1
 * 懒汉模式就是延迟加载，也叫懒加载。
 * 在程序需要用到的时候再创建实例，这样保证了内存不会被浪费。
 * 但是在某些情况下，如果实现不当，会出现线程安全的问题。
 */
public class TestOfSingleton2 {
    private static TestOfSingleton2 singleton2;

    private TestOfSingleton2() {
        System.out.println("TestOfSingleton2 constructor on thread: " +
                Thread.currentThread().getName());
    }

    public static TestOfSingleton2 getInstance() {
        if (singleton2 == null) {
            System.out.println("TestOfSingleton2 getInstance on thread: " +
                    Thread.currentThread().getName());
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            singleton2 = new TestOfSingleton2();
        }
        return singleton2;
    }

    public static void main(String[] args) {
        // 1. 直接运行，结果打印：
        // 未打印任何内容，因为没有任何单例调用。
        // 2. 调用过一次单例方法，结果打印：
        //TestOfSingleton2 getInstance on thread: main
        // TestOfSingleton2 constructor on thread: main
        // TestOfSingleton2 main on thread: main, singleton obj: 723074861
//        System.out.println("TestOfSingleton2 main on thread: " +
//                Thread.currentThread().getName() + ", singleton obj: " +
//                TestOfSingleton2.getInstance().hashCode());
        // 3. 多线程调用单例方法，结果打印：
        // TestOfSingleton2 getInstance on thread: Thread-0
        // TestOfSingleton2 getInstance on thread: Thread-1
        // TestOfSingleton2 getInstance on thread: Thread-2
        // TestOfSingleton2 getInstance on thread: Thread-3
        // TestOfSingleton2 getInstance on thread: Thread-4
        // TestOfSingleton2 getInstance on thread: Thread-5
        // TestOfSingleton2 getInstance on thread: Thread-6
        // TestOfSingleton2 getInstance on thread: Thread-7
        // TestOfSingleton2 getInstance on thread: Thread-8
        // TestOfSingleton2 getInstance on thread: Thread-9
        // TestOfSingleton2 constructor on thread: Thread-0
        // TestOfSingleton2 constructor on thread: Thread-3
        // singleton obj: 841746819, on thread: Thread-0
        // TestOfSingleton2 constructor on thread: Thread-7
        // TestOfSingleton2 constructor on thread: Thread-9
        // TestOfSingleton2 constructor on thread: Thread-2
        // TestOfSingleton2 constructor on thread: Thread-1
        // singleton obj: 1427285125, on thread: Thread-2
        // singleton obj: 1267494600, on thread: Thread-9
        // singleton obj: 929967477, on thread: Thread-7
        // TestOfSingleton2 constructor on thread: Thread-8
        // singleton obj: 350646954, on thread: Thread-3
        // TestOfSingleton2 constructor on thread: Thread-6
        // TestOfSingleton2 constructor on thread: Thread-5
        // TestOfSingleton2 constructor on thread: Thread-4
        // singleton obj: 51011016, on thread: Thread-5
        // singleton obj: 686587801, on thread: Thread-6
        // singleton obj: 985556768, on thread: Thread-8
        // singleton obj: 1228857876, on thread: Thread-1
        // singleton obj: 2006299335, on thread: Thread-4
        // 可以很明显看到当前的单例对象不止创建了一次，所以这样的
        // 懒汉写法实际是线程不安全的。
        for (int i = 0; i < 10; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton2.getInstance().hashCode() + ", on thread: " +
                    Thread.currentThread().getName());
        }
    }
}
