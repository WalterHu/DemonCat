package test;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Class: MutexLock
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/18
 */

/**
 * 我们用AQS来自定义一个独占锁MutexLock
 * （独占锁就是同一时刻只有一个线程可以获得锁，
 * 而其他线程只能被阻塞，一直到当前线程释放了锁，
 * 其他线程才有机会再获取锁）
 *
 * 为了实现MutexLock中的方法，我们需要调用AQS的
 * acquire、acquireInterruptibly、tryAcquire、tryAcquireNanos、release方法，
 * 这几个方法都是独占式获取、释放同步状态的方法，
 * 因此子类MutexSynchronizer需要重写和
 * 独占同步状态获取、释放相关的钩子操作：tryAcquire、tryRelease。
 *
 * 在java的同步组件中，AQS的子类一般是同步组件的静态内部类。
 *
 * 注意：当前代码为异常代码，运行的时候会报错。
 */
public class MutexLock implements Lock {
    // 只需要以下的代码，我们就拥有了一个可以使用的独占锁。
    // 可以看到，需要我们自己写的主要就是tryAcquire()和tryRelease()这两个方法，
    // 其他的操作，如对获取锁失败线程的阻塞、唤醒，都是AQS替我们实现的。
    private MutexSynchronizer mutexSynchronizer = new MutexSynchronizer();

    private static class MutexSynchronizer extends AbstractQueuedSynchronizer {
        /**
         * @param arg 这个参数是用来传同步状态的累加值的，因为我们实现的是独占锁，
         * 因此这个参数实际用不到，我们在方法里累加值恒为1
         */
        @Override
        protected boolean tryAcquire(int arg) {
            /**
             * 用CAS来更新AQS的同步状态，如果原值是0则更新为1代表已经有线程获取了独占锁
             */
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread()); //设置当前独占锁的所有者线程
                return true;
            }
            return false;
        }

        /**
         * @param arg 这个参数是用来传同步状态的递减值的，因为我们实现的是独占锁，
         * 因此这个参数实际用不到，我们在方法里递减值恒为1
         */
        @Override
        protected boolean tryRelease(int arg) {
            //如果当前AQS同步状态是0，说明试图在没有获得同步状态的情况下释放同步状态，直接抛异常
            if (getState()==0)
                throw new IllegalMonitorStateException();

            //这里不需要CAS而是直接把同步状态设置为0，因为我们实现的是独占锁，
            // 正常情况下会执行释放操作的线程只有同步状态的所有者线程.
            setState(0);
            setExclusiveOwnerThread(null);
            return true;
        }

        protected Condition newCondition() {
            return new ConditionObject();
        }
    }

    /**
     * 获取锁
     */
    @Override
    public void lock() {
        mutexSynchronizer.acquire(1);
    }

    /**
     * 可中断地获取锁，在线程获取锁的过程中可以响应中断
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        mutexSynchronizer.acquireInterruptibly(1);
    }

    /**
     * 尝试非阻塞获取锁，调用方法后立即返回，成功返回true，失败返回false
     * @return
     */
    @Override
    public boolean tryLock() {
        return mutexSynchronizer.tryAcquire(1);
    }

    /**
     * 在超时时间内获取锁，到达超时时间将返回false,也可以响应中断
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, @NonNull TimeUnit unit) throws InterruptedException {
        return mutexSynchronizer.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        mutexSynchronizer.release(1);
    }

    /**
     * 获取等待组件，等待组件实现类似于Object.wait()方法的功能
     * @return
     */
    @NonNull
    @Override
    public Condition newCondition() {
        return mutexSynchronizer.newCondition();
    }
}
