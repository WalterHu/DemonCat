package test;

/**
 * @Class: TestOfAQS
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/18
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试AbstractQueuedSynchronizer的使用。
 * Jdk1.5中构建锁和组件的时候，大多是
 * 以队列同步器(AbstractQueuedSynchronizer)为基础的，
 * 因此AbstractQueuedSynchronizer可以看作是并发包的基础框架。
 *
 * AbstractQueuedSynchronizer使用一个int变量state表示同步状态，
 * 使用一个隐式的FIFO队列(隐式队列就是并没有声明这样一个队列，
 * 只是通过每个节点记录它的上个节点和下个节点来从逻辑上产生一个队列)来完成阻塞线程的排队。
 *
 * AQS是一个抽象类，当我们要构建一个同步组件的时候，
 * 需要定义一个子类继承AQS，这里应用了模板方法设计模式，
 *
 * 模板模式由一个抽象类和一个实现类组成，抽象类中主要有三类方法：
 * 1、模板方法：实现了算法主体框架，供外部调用。里面会调用原语操作和钩子操作。
 * 2、原语操作：即定义的抽象方法，子类必须重写。
 * 3、钩子操作：和原语操作类似，也是供子类重写的，区别是钩子操作子类可以选择重写也可以选择不重写，
 *   如果不重写则使用抽象类默认操作，通常是一个空操作或抛出异常。
 *
 *   在AQS中没有原语操作，也就是说自定义的子类继承AQS后，不会强制子类重写任何方法。
 *   AQS只提供了若干钩子操作，这些钩子操作的默认实现都是直接抛出异常。
 *   子类不需要重写所有的钩子操作，只需要根据要构建的同步组件的
 *   类型来决定要调用AQS中的哪些模板方法，再实现这些模板方法中用到了的钩子操作即可。
 *
 *   @see MutexLock
 */
public class TestOfAQS {
    private volatile int index = 0;
    // private MutexLock lock = new MutexLock(); // some error happens
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Condition condition2 = lock.newCondition();

    public static void main(String[] args) {
        new TestOfAQS().testOfMutexLock();
    }

    private void testOfMutexLock() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                addCondition();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                reduceCondition();
            }
        });
        thread1.start();
        thread2.start();
    }

    private void addCondition() {
        lock.lock();
        Thread t = Thread.currentThread();
        final String threadName = t.getName();
        try {
            while (index >= 1) {
                condition.await(); // 释放锁
            }
            System.out.println("add index begin index : " + index + ", " + threadName);
            index ++;
            condition2.signal();
            System.out.println("add index to index : " + index + ", " + threadName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void reduceCondition() {
        lock.lock();
        Thread t = Thread.currentThread();
        final String threadName = t.getName();
        try {
            while (index < 1) {
                condition2.await(); // 释放锁
            }
            System.out.println("reduce index begin index : " + index + ", " + threadName);
            index --;
            condition.signal();
            System.out.println("reduce index to index : " + index + ", " + threadName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
