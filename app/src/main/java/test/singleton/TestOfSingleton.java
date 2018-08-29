package test.singleton;

/**
 * @Class: TestOfSingleton
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/28
 */

/**
 * 单例模式-静态内部类
 * 使用这种方式我们没有显式的进行任何同步操作，
 * 是靠JVM保证类的静态成员只能被加载一次的特点，
 * 这样就从JVM层面保证了只会有一个实例对象。加载一个类时，其内部类不会同时被加载。
 * 一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生。
 * 可以说这种方式是实现单例模式的最优解。
 */
public class TestOfSingleton {

    private static class Singleton {
        private static TestOfSingleton singleton = new TestOfSingleton();
    }

    private TestOfSingleton() {
        System.out.println("TestOfSingleton constructor on thread: " +
                Thread.currentThread().getName());
    }

    // 调用这个类的时候会触发Singleton类加载。在加载该类时，
    // 会创建其静态成员singleton，而在该类被初始化时，会调用
    // TestOfSingleton类的构造器，而创建对象。
    public static TestOfSingleton getInstance() {
        System.out.println("TestOfSingleton getInstance on thread: " +
                Thread.currentThread().getName());
        return Singleton.singleton;
    }

    public static void main(String[] args) {
        System.out.println("TestOfSingleton main.");
        // 1. 直接运行，结果打印：
        // TestOfSingleton main.
        // 因为虽然运行了该类的main方法，加载了TestOfSingleton类，
        // 但是没有因为其静态内部类没有被调用，也不会被加载，
        // 而调用了Singleton静态内部类的singleton成员，
        // 则导致其加载-连接-初始化，而singleton对象则创建了
        // TestOfSingleton对象。

        // 2. 多线程运行，打印结果：
        // TestOfSingleton main.
        // TestOfSingleton getInstance on thread: Thread-0
        // TestOfSingleton getInstance on thread: Thread-1
        // TestOfSingleton getInstance on thread: Thread-2
        // TestOfSingleton getInstance on thread: Thread-3
        // TestOfSingleton constructor on thread: Thread-2
        // singleton obj: 109900682
        // singleton obj: 109900682
        // singleton obj: 109900682
        // singleton obj: 109900682
        // TestOfSingleton getInstance on thread: Thread-4
        // singleton obj: 109900682
        // 结果说明，当前singleton类仅仅在其被getInstance方法被调用，
        // 之后才被加载Singleton类，否则不会加载。而创建TestOfSingleton
        // 对象的时候，也是直接基于JVM类加载的机制来保证仅被加载一次。
        for (int i = 0; i < 5; i ++) {
            new PrintThread().start();
        }
    }

    static class PrintThread extends Thread {
        @Override
        public void run() {
            System.out.println("singleton obj: " +
                    TestOfSingleton.getInstance().hashCode());
        }
    }
}


