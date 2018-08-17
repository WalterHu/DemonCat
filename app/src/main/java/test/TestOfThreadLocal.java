package test;

import java.util.concurrent.CountDownLatch;

/**
 * @Class: TestOfThreadLocal
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

// ThreadLocal 适用于每个线程需要自己独立的实例且该实例需要在多个方法中被使用，
// 也即变量在线程间隔离而在方法或类间共享的场景。
// ThreadLocal 提供了线程本地的实例。它与普通变量的区别在于，每个使用该变量的线程
// 都会初始化一个完全独立的实例副本。ThreadLocal 变量通常被
// private static修饰。当一个线程结束时，它所使用的所有 ThreadLocal 相对的实例副本都可被回收。
public class TestOfThreadLocal {

    // Thread name: Thread-1, ThreadLocal hashCode: 1001240990, Instance hashCode: 896159899, Value: 0
    // Thread name: Thread-2, ThreadLocal hashCode: 1001240990, Instance hashCode: 297626270, Value: 0
    // Thread name: Thread-0, ThreadLocal hashCode: 1001240990, Instance hashCode: 413036867, Value: 0
    // Thread name: Thread-2, ThreadLocal hashCode: 1001240990, Instance hashCode: 297626270, Value: 01
    // Thread name: Thread-1, ThreadLocal hashCode: 1001240990, Instance hashCode: 896159899, Value: 01
    // Thread name: Thread-2, ThreadLocal hashCode: 1001240990, Instance hashCode: 297626270, Value: 012
    // Thread name: Thread-0, ThreadLocal hashCode: 1001240990, Instance hashCode: 413036867, Value: 01
    // Thread name: Thread-2, ThreadLocal hashCode: 1001240990, Instance hashCode: 297626270, Value: 0123
    // Thread name: Thread-1, ThreadLocal hashCode: 1001240990, Instance hashCode: 896159899, Value: 012
    // innerClass thread Thread-2 finished.
    // Thread name: Thread-0, ThreadLocal hashCode: 1001240990, Instance hashCode: 413036867, Value: 012
    // Thread name: Thread-1, ThreadLocal hashCode: 1001240990, Instance hashCode: 896159899, Value: 0123
    // Thread name: Thread-0, ThreadLocal hashCode: 1001240990, Instance hashCode: 413036867, Value: 0123
    // innerClass thread Thread-0 finished.
    // innerClass thread Thread-1 finished.
    // Whole finished.

    // 如上述结果所示，在当前测试类中，创建了一个单独的对象InnerClass,
    // 所以在多线程操作innerClass的方法时，实际是同一个InnerClass对象。
    // InnerClass对象内直接引用了静态内部类Counter类的静态变量couner，
    // 类型为ThreadLocal。所以在理论上来说，ThreadLocal是内存共享的。
    // 但是ThreadLocal封装的StringBuilder对象，在三个线程中却包含了3个内容，
    public static void main(String[] args) {
        int thread = 3;
        // start 3 threads
        final CountDownLatch latch = new CountDownLatch(thread);
        final InnerClass innerClass = new InnerClass();
        for (int i = 1 ; i <= thread; i ++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 4; j ++) {
                        innerClass.add(String.valueOf(j));
                        innerClass.print();
                    }
                    System.out.println("innerClass thread " + Thread.currentThread().getName() + " finished.");
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await(); // 等待latch计数到0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Whole finished.");
    }

    private static class InnerClass {
        public void add(String  string) {
            StringBuilder stringBuilder = Counter.counter.get();
            Counter.counter.set(stringBuilder.append(string));
        }

        public void print() {
            System.out.printf("Thread name: %s, ThreadLocal hashCode: %s, Instance hashCode: %s, Value: %s\n",
                    Thread.currentThread().getName(),
                    Counter.counter.hashCode(),
                    Counter.counter.get().hashCode(),
                    Counter.counter.get().toString());
        }
    }

    private static class Counter {
        // counter是Counter静态内部类的静态变量，理论上来说进程中是唯一的
        private static ThreadLocal<StringBuilder> counter = new ThreadLocal<StringBuilder>() {
            @Override
            protected StringBuilder initialValue() {
                return new StringBuilder();
            }
        };
    }
}
