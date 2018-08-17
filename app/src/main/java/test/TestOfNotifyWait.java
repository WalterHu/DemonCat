package test;

/**
 * @Class: TestOfNotifyWait
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

/**
 * 测试notify／wait等同步方法
 */
public class TestOfNotifyWait {
    Object lock = new Object();
    int index = 0;

    public static void main(String[] args) {
        new TestOfNotifyWait().testNotifyAndWait();
    }

    // 两个线程将index从0增加到20，共20个增量
    // wait()必须和notify()共同出现，如果仅出现wait()调用而没有
    // notify()方法调用，则会导致线程一直为等待锁而挂起，却没其他
    // 线程通知它。wait()方法通常和while循环条件语句结合使用。
    // notify 与 notifyAll 的区别
    // 被notify()唤醒的线程，在执行完成后如果不继续调用notify或者notifyAll，
    // 会导致其他wait线程无法被唤醒；如果线程是被notifyAll调用唤醒，
    // 则在其执行完成后，系统会自动唤醒下一个线程。
    public void testNotifyAndWait(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (index < 9) {
                        // wait for thread 1 to plus index to 10.
                        try {
                            lock.wait(); // release the lock
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("thread 2 index wait: " + index);
                    }
                    System.out.println("thread 2 index begin: " + index);
                    for (int i = 0; i < 10; i ++) {
                        index ++;
                    }
                    System.out.println("thread 2 index end: " + index);
                    lock.notify(); // notify other thread ready to get the lock
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (index >= 9) {
                        try {
                            lock.wait(); // release the lock
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("thread 1 index wait: " + index);
                    }
                    System.out.println("thread 1 index begin: " + index);
                    for (int i = 0; i < 10; i ++) {
                        index ++;
                    }
                    System.out.println("thread 1 index end: " + index);

                    lock.notify(); // notify other thread ready to get the lock
                }
            }
        }).start();

    }
}
