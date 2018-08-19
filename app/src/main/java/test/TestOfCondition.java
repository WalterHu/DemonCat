package test;

/**
 * @Class: TestOfCondition
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试Condition的用法
 * 显示锁提供自己的等待/通知机制，即condition，与object的wait、notify、notifyAll对应
 *
 * ReetrantLock能够提供不止一个Condition，即一个锁支持多个Condition，
 * 存在多个等待队列
 *
 * @see BlockQueue
 */
public class TestOfCondition {
    private Lock lock = new ReentrantLock(); // 创建显示锁
    private Condition condition = lock.newCondition(); // 得到新的condition对象
    private volatile int conditions = 0; // thread execute condition

    public static void main(String[] args) {
        // new TestOfCondition().testOfCondition();
        new TestOfCondition().testOfMultiCondition();
    }

    // lockCondition的await()、signal()方法分别对应
    // 之前的Object的wait()和notify()方法。整体上
    // 和Object的等待通知是类似的。
    private void testOfCondition() {
       Thread thread = new Thread() {
           @Override
           public void run() {
               lock.lock();
               try {
                   while (!(conditions == 1)) {
                       System.out.println("线程 " + Thread.currentThread().getName() + " 等待");
                       condition.await();
                   }
               } catch (InterruptedException e) {
                   // 如果当前线程正在condition上await，则中断发生时，其await会被终止，并且interrupt状态会被清理。
                   System.out.println("线程 " + Thread.currentThread().getName() + " 被中断");
                   Thread.currentThread().interrupt();
               } finally {
                   lock.unlock();
               }
               System.out.println("a executed by condition");
           }
        };
        thread.start();
        // current thread execute
        lock.lock(); // 获取锁或者等待
        try {
            Thread.sleep(2000l);
            conditions = 1;
            condition.signal(); // 释放锁
        } catch (InterruptedException e) {
            System.out.println("线程 " + Thread.currentThread().getName() + " 被中断");
        } finally {
            lock.unlock();
        }
        System.out.println("condition already to 1.");
    }

    private void testOfMultiCondition() {
        final BlockQueue<Integer> queue = new BlockQueue<>(3);
        Thread addThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        Thread.sleep(2000l);
                        int value = (int) (Math.random() * 100);
                        queue.add(value);
                        System.out.println("add thread add: " + value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread removeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        Thread.sleep(2000l);
                        int value = queue.remove();
                        System.out.println("remove thread remove: " + value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        addThread.start();
        removeThread.start();
    }
}
