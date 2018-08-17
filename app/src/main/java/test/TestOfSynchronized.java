package test;

/**
 * @Class: TestOfSynchronized
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 同步锁测试
 * violate/synchronized
 */
public class TestOfSynchronized {
    int n = 0;
    volatile int m = 0;

    public static void main(String[] args) {
//        new TestOfSynchronized().testOfVolatile();
//        new TestOfSynchronized().testOfVolatile2();
        new TestOfSynchronized().testOfSynchronized();
    }

    public synchronized void plusNSync() {
        n ++;
    }

    // 最终结果不足5000
    public void testOfVolatile() {
        // n是一般的对象属性，启用多线程去增加它的大小，但是最终无法保证是累加的预期值。
        // 因为线程中不同步，并且CPU在执行的时候会使用缓存，指令执行数据是在缓存-内存中执行的，导致
        // 多线程访问的时候数据不是同步状态。
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Thread " + Thread.currentThread().getId() + " started, n: " + n);
                int i = 0;
                while(i < 10 * 1000) {
                    n ++;
                    i ++;
                }
                System.out.println("Thread " + Thread.currentThread().getId() + " finished, n: " + n);
                return i;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread " + Thread.currentThread().getId() + " started, n: " + n);
                int i = 0;
                while(i < 10 * 1000) {
                    n ++;
                    i ++;
                }
                i = 0;
                System.out.println("Thread " + Thread.currentThread().getId() + " finished, n: " + n);
            }
        };
        Thread thread1 = new Thread(futureTask);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);
        Thread thread5 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        // join当前线程
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("final n: " + n);
    }

    // 最终结果不足50000
    private void testOfVolatile2() {
        // m是volatile修饰的对象属性，启用多线程去增加它的大小，但是最终无法保证是累加的预期值。
        // 因为volatile仅保持可见性，但是不保证同步性，所以它不是线程安全的，仅仅保证不会阻塞。
        // volatile保证读取的是最新的值，但是不保证写的时候是同步状态。
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Thread " + Thread.currentThread().getId() + " started, m: " + m);
                int i = 0;
                while(i < 10 * 1000) {
                    m ++;
                    i ++;
                }
                System.out.println("Thread " + Thread.currentThread().getId() + " finished, m: " + m);
                return i;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread " + Thread.currentThread().getId() + " started, m: " + m);
                int i = 0;
                while(i < 10 * 1000) {
                    m ++;
                    i ++;
                }
                i = 0;
                System.out.println("Thread " + Thread.currentThread().getId() + " finished, m: " + m);
            }
        };
        Thread thread1 = new Thread(futureTask);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);
        Thread thread5 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        // join当前线程
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("final m: " + m);
    }

    // 最终结果刚好50000
    private void testOfSynchronized() {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Thread " + Thread.currentThread().getId() + " started, n: " + n);
                int i = 0;
                while(i < 10 * 1000) {
                    plusNSync();
                    i ++;
                }
                System.out.println("Thread " + Thread.currentThread().getId() + " finished, n: " + n);
                return i;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread " + Thread.currentThread().getId() + " started, n: " + n);
                int i = 0;
                while(i < 10 * 1000) {
                    plusNSync();
                    i ++;
                }
                i = 0;
                System.out.println("Thread " + Thread.currentThread().getId() + " finished, n: " + n);
            }
        };
        Thread thread1 = new Thread(futureTask);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);
        Thread thread5 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        // join当前线程
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("final n: " + n);
    }
}
