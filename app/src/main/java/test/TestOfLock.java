package test;

/**
 * @Class: TestOfLock
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试Lock类
 * 对于线程来说，interrupt()不能够中断运行中的线程，但是能够中断阻塞状态下的线程。
 * Lock 与 synchronized关键字实现锁的方式不同：
 * 1.Lock是一个接口，而synchronized是Java中的关键字
 * 2.synchronized是内置的语言实现，synchronized是在JVM层面上实现的，
 * 3.可以通过一些监控工具监控synchronized的锁定，而且在代码执行时出现异常，JVM会自动释放锁定
 * 4.使用Lock则不行，lock是通过代码实现的，要保证锁定一定会被释放，就必须将unLock()放到finally{}中
 * 5.Lock可以让等待锁的线程响应中断，线程可以中断去干别的事务，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；
 * 6.通过Lock可以知道有没有成功获取锁，而synchronized却无法办到
 * 7.Lock可以提高多个线程进行读操作的效率
 *
 * 当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized.
 * ReentrantLock是唯一实现了Lock接口的类，并且ReentrantLock提供了更多的方法。
 */
public class TestOfLock {
    public static void main(String[] args) {
//        testOfLock();
//        testOfTryLock();
        testOfInterruptLock();
    }

    private ArrayList<String> list = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    // lock()方法是平常使用得最多的一个方法，就是用来获取锁。如果锁已被其他线程获取，则进行等待。
    private static void testOfLock() {
        final TestOfLock testOfLock = new TestOfLock();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                testOfLock.lockInsert(Thread.currentThread());
            }
        };

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                testOfLock.lockInsert(Thread.currentThread());
            }
        };

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("final list: " + testOfLock.list.toArray());
    }

    public void lockInsert(Thread thread) {
        lock.lock(); // 获取锁
        try {
            System.out.println("线程" + thread.getName() + "得到了锁");
            for (int i = 0; i < 5; i ++) {
                list.add(String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("线程" + thread.getName() + "释放了锁");
        }
    }

    // tryLock()方法是有返回值的，
    // 它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，
    // 也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。而后的动作也就不执行了。
    private static void testOfTryLock() {
        final TestOfLock test = new TestOfLock();
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                test.tryLockInsert(Thread.currentThread());
            }
        };

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                test.tryLockInsert(Thread.currentThread());
            }
        };

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("final list: " + test.list.toArray());
    }

    public void tryLockInsert(Thread thread) {
        if (lock.tryLock()) {
            try {
                System.out.println("线程" + thread.getName() + "得到了锁");
                for (int i = 0; i < 5; i ++) {
                    list.add(String.valueOf(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("线程" + thread.getName() + "释放了锁");
            }
        } else {
            System.out.println("线程" + thread.getName() + "获取锁失败");
        }
    }

    // 当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。
    // 也就使说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，
    // 而线程B只有在等待，那么对线程B调用threadB.interrupt()方法能够中断线程B的等待过程。
    // 由于lockInterruptibly()的声明中抛出了异常，所以lock.lockInterruptibly()必须放在
    // try块中或者在调用lockInterruptibly()的方法外声明抛出InterruptedException.
    private static void testOfInterruptLock() {
        TestOfLock test = new TestOfLock();
        Thread thread1 = test.new MyThread(test); // 必须使用对象new方法来创建内部类
        Thread thread2 = test.new MyThread(test);

        thread1.start();
        thread2.start();

        // 让当前线程等待2秒钟后，直接中断thread2
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
    }

    public void interruptLockInsert(Thread thread) throws InterruptedException {
        lock.lockInterruptibly();  //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {
            System.out.println("线程" + thread.getName() + "得到了锁");
            long startTime = System.currentTimeMillis();
            for (;;) { // 不停循环，尽量延长测试时间
                if (System.currentTimeMillis() - startTime > Integer.MAX_VALUE) {
                    break;
                }
                // 插入数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("线程" + thread.getName()+"执行 finally");
            lock.unlock();
            System.out.println("线程" + thread.getName() + "释放了锁");
        }
    }

    class MyThread extends Thread {
        private TestOfLock mTest = null;

        public MyThread(TestOfLock test) {
            this.mTest = test;
        }

        @Override
        public void run() {
            try {
                mTest.interruptLockInsert(Thread.currentThread());
            } catch (InterruptedException e) {
                // e.printStackTrace();
                System.out.println("线程 " + Thread.currentThread().getName() + " 被中断");
            }
        }
    }
}
