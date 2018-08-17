package test;

/**
 * @Class: BlockQueue
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 配合Condition测试如下一个功能的队列：
 * 一个阻塞队列，当队列已满时，add操作被阻塞有其他线程通过remove方法删除元素；
 * 当队列已空时，remove操作被阻塞直到有其他线程通过add方法添加元素。
 *
 * 原理：Condition提供的等待通知功能更强大，
 * 最重要的一点是，一个lock对象可以通过多次调用 lock.newCondition()
 * 获取多个Condition对象，也就是说，在一个lock对象上，
 * 可以有多个等待队列，而Object的等待通知在一个Object上，只能有一个等待队列。
 */
public class BlockQueue<T> {
    private List<T> list = null;
    private int maxSize = 0;
    private Lock lock = new ReentrantLock();
    private Condition addCondition = lock.newCondition();
    private Condition removeCondition = lock.newCondition();

    public BlockQueue(int maxSize) {
        this.maxSize = maxSize;
        this.list = new ArrayList<>(maxSize);
    }

    public void add(T item) {
        lock.lock();
        try {
            while (list.size() >= maxSize) {
                // should wait to remove
                addCondition.await(); // 在addCondition对象上等待
            }
            // add the list item
            list.add(item);
            // notify remove condition
            removeCondition.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public T remove() {
        lock.lock();
        try {
            while (list.size() < maxSize) {
                // should wait for add
                removeCondition.await();
            }
            // remove item on last
            T e = list.remove(list.size() - 1);
            // notify add condition
            addCondition.signal();
            return e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }
}
